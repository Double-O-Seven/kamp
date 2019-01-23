import org.gradle.internal.jvm.Jvm

plugins {
    id("kamp-cpp-codegen")
}

val runtimeCodegen: Configuration by configurations.creating

dependencies {
    runtimeCodegen(project(":kamp-core"))
}

val runtimePackageName = "ch.leadrian.samp.kamp.core.runtime"

val srcMainCppDir = "$projectDir/src/main/cpp"

val sampgdkDir = "$srcMainCppDir/sampgdk"

val versionIDLFile = "$srcMainCppDir/idl/version.idl"

val sampgdkIDLFilesDir = "$sampgdkDir/lib/sampgdk"

val actorIDLFile = "$sampgdkIDLFilesDir/a_actor.idl"
val objectsIDLFile = "$sampgdkIDLFilesDir/a_objects.idl"
val playersIDLFile = "$sampgdkIDLFilesDir/a_players.idl"
val sampIDLFile = "$sampgdkIDLFilesDir/a_samp.idl"
val vehiclesIDLFile = "$sampgdkIDLFilesDir/a_vehicles.idl"

val kampSrcDir = "$sampgdkDir/plugins/kamp"

val sampgdkBuildDir = "$buildDir/sampgdk"

val sampPluginSdkDir = "$srcMainCppDir/samp-plugin-sdk-original"

kampCppCodegen {
    version = project.version.toString()
    runtimeJavaPackageName = runtimePackageName
    outputDirectoryPath = kampSrcDir
    interfaceDefinitionFiles(actorIDLFile, objectsIDLFile, playersIDLFile, sampIDLFile, vehiclesIDLFile, versionIDLFile)
}

tasks {
    val generateSAMPNativeFunctionsHeaderFile: Task by creating {
        val kampCoreMainOutput = project(":kamp-core").the<JavaPluginConvention>().sourceSets["main"].output
        val sampNativeFunctionsHeaderFile = "$kampSrcDir/SAMPNativeFunctions.h"

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

    val makePlugin: Task by creating {
        inputs.dir(sampgdkDir)
        inputs.dir(sampPluginSdkDir)
        outputs.dir(sampgdkBuildDir)

        dependsOn(generateSAMPNativeFunctionsHeaderFile, generateKampCppFiles)

        doFirst {
            file(sampgdkBuildDir).mkdirs()
        }

        doLast {
            exec {
                workingDir(sampgdkBuildDir)

                val command = "cmake \"$sampgdkDir\" -DSAMP_SDK_ROOT=\"$sampPluginSdkDir\" -DSAMPGDK_STATIC=ON -DSAMPGDK_BUILD_PLUGINS=ON"
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    commandLine("cmd", "/c", command)
                } else {
                    commandLine(command)
                }
            }

            exec {
                workingDir(sampgdkBuildDir)

                val command = "cmake --build . --config Release"
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    commandLine("cmd", "/c", command)
                } else {
                    commandLine(command)
                }
            }
        }
    }

    clean {
        dependsOn("cleanGenerateKampCppFiles", "cleanMakePlugin", "cleanGenerateSAMPNativeFunctionsHeaderFile")
    }

    build {
        dependsOn(makePlugin)
    }
}
