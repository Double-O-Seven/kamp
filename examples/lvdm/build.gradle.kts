import org.gradle.internal.os.OperatingSystem

dependencies {
    implementation(project(":kamp-core"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
}

plugins {
    id("kamp-textkeygen")
    id("kamp-serverstarter")
}

textKeyGenerator {
    packageName("ch.leadrian.samp.kamp.examples.lvdm")
    resourcesDirectory = projectDir.absolutePath + "/src/main/resources"
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.lvdm.LvdmGameMode"
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.ignore.version.mismatch", "true")
}

tasks {
    configureServer {
        dependsOn(project(":kamp-plugin").tasks.getByName("build"))
    }
}
