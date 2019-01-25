import org.gradle.internal.jvm.Jvm
import org.gradle.internal.os.OperatingSystem

plugins {
    base
    `cpp-library`
    `visual-studio`
    id("kamp-cpp-codegen")
}

val runtimeCodegen: Configuration by configurations.creating

dependencies {
    runtimeCodegen(project(":kamp-core"))
}

val runtimePackageName = "ch.leadrian.samp.kamp.core.runtime"

val srcMainCppDir = "$projectDir/src/main/cpp"
val srcMainHeadersDir = "$projectDir/src/main/headers"
val idlFilesDir = "$projectDir/src/main/idl"
val actorIDLFile = "$idlFilesDir/a_actor.idl"
val objectsIDLFile = "$idlFilesDir/a_objects.idl"
val playersIDLFile = "$idlFilesDir/a_players.idl"
val sampIDLFile = "$idlFilesDir/a_samp.idl"
val vehiclesIDLFile = "$idlFilesDir/a_vehicles.idl"
val versionIDLFile = "$idlFilesDir/version.idl"

kampCppCodegen {
    version = project.version.toString()
    runtimeJavaPackageName = runtimePackageName
    cppOutputDirectoryPath = srcMainCppDir
    headersOutputDirectoryPath = srcMainHeadersDir
    interfaceDefinitionFiles(actorIDLFile, objectsIDLFile, playersIDLFile, sampIDLFile, vehiclesIDLFile, versionIDLFile)
}

val sampgdkHome: String by lazy { System.getenv("SAMPGDK_HOME") }
val javaHome: File by lazy { org.gradle.internal.jvm.Jvm.current().javaHome }

library {
    baseName.set("Kamp")

    targetMachines.addAll(machines.linux.x86, machines.windows.x86)

    privateHeaders {
        from(srcMainHeadersDir)
        from("$javaHome/include")
        from("$sampgdkHome/include")
    }

    binaries.whenElementFinalized {
        when {
            targetPlatform.operatingSystem.isWindows -> {
                privateHeaders {
                    from("$javaHome/include/win32")
                }
            }
            targetPlatform.operatingSystem.isLinux -> {
                privateHeaders {
                    from("$javaHome/include/linux")
                }
            }
        }
    }

    dependencies {
        val sampgdk4LibraryPath = file("$sampgdkHome/lib/sampgdk4".toStaticLibraryName())
        val jdkLibraryPath = file("$javaHome/lib/jvm".toStaticLibraryName())
        implementation(files(sampgdk4LibraryPath, jdkLibraryPath))
    }

    toolChains {
        withType<VisualCpp> {
            eachPlatform {
                linker.withArguments {
                    add("/DEF:\"$srcMainCppDir/SAMPCallbacks.def\"")
                }
            }
        }
    }
}

fun String.toStaticLibraryName(): String = OperatingSystem.current().getStaticLibraryName(this)

tasks {

    val generateSAMPNativeFunctionsHeaderFile: Task by creating {
        val kampCoreMainOutput = project(":kamp-core").the<JavaPluginConvention>().sourceSets["main"].output
        val sampNativeFunctionsHeaderFile = "$srcMainHeadersDir/SAMPNativeFunctions.h"

        kampCoreMainOutput.classesDirs.files.forEach(inputs::dir)
        outputs.file(sampNativeFunctionsHeaderFile)

        dependsOn(":kamp-core:classes")

        doLast {
            file(sampNativeFunctionsHeaderFile).parentFile.mkdirs()
            exec {
                executable(Jvm.current().getExecutable("javah"))
                args(
                        "-o", sampNativeFunctionsHeaderFile,
                        "-classpath", kampCoreMainOutput.asPath,
                        "-jni",
                        "-verbose",
                        "$runtimePackageName.SAMPNativeFunctions"
                )
            }
        }
    }

    assemble {
        dependsOn(generateSAMPNativeFunctionsHeaderFile, generateKampCppFiles)
    }

    clean {
        dependsOn("cleanGenerateKampCppFiles", "cleanGenerateSAMPNativeFunctionsHeaderFile")
    }
}
