dependencies {
    implementation(project(":kamp-core"))
    implementation(project(":kamp-streamer"))
    implementation(project(":kamp-geodata"))
    implementation(project(":kamp-view"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
}

plugins {
    id("kamp-textkeygen")
    id("kamp-serverstarter")
}

val generatedSrcJavaDir = "$buildDir/generated-src/main/java"

sourceSets {
    main {
        java {
            srcDir(generatedSrcJavaDir)
        }
    }
}

textKeyGenerator {
    outputDirectory = generatedSrcJavaDir
    packageName("ch.leadrian.samp.kamp.examples.lvdm")
    resourcesDirectory = projectDir.absolutePath + "/src/main/resources"
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.lvdm.LvdmGameMode"
    kampPluginBinaryPath = project(":kamp-plugin").buildDir.toString() + "/sampgdk/plugins/kamp/Release/kamp.dll"
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.streamer.rate.ms", "300")
}

val configureServer by tasks

configureServer.dependsOn(project(":kamp-plugin").tasks.getByName("build"))
