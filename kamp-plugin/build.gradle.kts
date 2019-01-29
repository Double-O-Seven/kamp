import org.gradle.internal.jvm.Jvm
import org.gradle.internal.os.OperatingSystem

plugins {
    base
    `cpp-library`
    `visual-studio`
    id("kamp-cpp-codegen")
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

library {
    baseName.set("Kamp")

    targetMachines.addAll(machines.linux.x86, machines.windows.x86)

    toolChains {
        withType<VisualCpp> {
            val sampgdkHome: String by lazy { System.getenv("SAMPGDK_HOME") }
            val javaHome: File by lazy { Jvm.current().javaHome }

            eachPlatform {
                cppCompiler.withArguments {
                    add("/I$javaHome\\include")
                    add("/I$javaHome\\include\\win32")
                    add("/I$sampgdkHome\\include")
                }
                linker.withArguments {
                    add("/LIBPATH:$javaHome\\lib")
                    add("/LIBPATH:$sampgdkHome\\lib")
                    add("/DEF:$srcMainCppDir\\SAMPCallbacks.def")
                    add("$sampgdkHome\\lib\\sampgdk4.lib")
                    add("$javaHome\\lib\\jvm.lib")
                }
            }
        }

        withType<Gcc> {
            eachPlatform {
                cppCompiler.withArguments {
                    add("-std=c++11")
                }
            }
        }
    }
}

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
