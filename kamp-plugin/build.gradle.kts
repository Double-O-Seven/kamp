import org.gradle.internal.jvm.Jvm
import org.gradle.internal.os.OperatingSystem

plugins {
    base
    `cpp-library`
    `visual-studio`
    id("kamp-cpp-codegen")
}

val runtimePackageName = "ch.leadrian.samp.kamp.core.runtime"

val amxRuntimePackageName = "ch.leadrian.samp.kamp.core.runtime.amx"

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
val javaHome: File by lazy { Jvm.current().javaHome }

library {
    baseName.set("kamp")

    targetMachines.addAll(machines.linux.x86, machines.windows.x86)

    toolChains {
        withType<VisualCpp> {
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
                    add("-I$javaHome/include")
                    add("-I$javaHome/include/linux")
                    add("-I$srcMainHeadersDir/amx")
                    add("-DLINUX")
                }
                linker.withArguments {
                    add("-L$javaHome/jre/lib/i386/server")
                    add("-ljvm")
                    add("-lsampgdk")
                }
            }
        }
    }
}

tasks {

    val generateSAMPNativeFunctionsHeaderFile: Task by creating {
        val kampCoreMainOutput = project(":kamp-core").the<JavaPluginConvention>().sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].output
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

    val generateAmxNativeFunctionInvokerHeaderFile: Task by creating {
        val kampCoreMainOutput = project(":kamp-core").the<JavaPluginConvention>().sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].output
        val amxNativeFunctionInvokerHeaderFile = "$srcMainHeadersDir/AmxNativeFunctionInvoker.h"

        kampCoreMainOutput.classesDirs.files.forEach(inputs::dir)
        outputs.file(amxNativeFunctionInvokerHeaderFile)

        dependsOn(":kamp-core:classes")

        doLast {
            file(amxNativeFunctionInvokerHeaderFile).parentFile.mkdirs()
            exec {
                executable(Jvm.current().getExecutable("javah"))
                args(
                        "-o", amxNativeFunctionInvokerHeaderFile,
                        "-classpath", kampCoreMainOutput.asPath,
                        "-jni",
                        "-verbose",
                        "$amxRuntimePackageName.AmxNativeFunctionInvokerImpl"
                )
            }
        }
    }

    assemble {
        dependsOn(generateSAMPNativeFunctionsHeaderFile, generateAmxNativeFunctionInvokerHeaderFile, generateKampCppFiles)
    }

    clean {
        dependsOn("cleanGenerateKampCppFiles", "cleanGenerateSAMPNativeFunctionsHeaderFile", "cleanGenerateAmxNativeFunctionInvokerImplHeaderFile")
    }
}
