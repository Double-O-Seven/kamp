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
    className = "LvdmTextKeys"
    packageName("ch.leadrian.samp.kamp.examples.lvdm")
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.lvdm.LvdmGameMode"
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.ignore.version.mismatch", "true")
    configProperty("lvdm.respawn.cash.amout", "2500")
}

tasks {
    configureServer {
        dependsOn(project(":kamp-plugin").tasks.getByName("build"))
    }
}
