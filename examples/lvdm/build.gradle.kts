import org.gradle.internal.os.OperatingSystem

dependencies {
    implementation(project(":kamp-core"))
    implementation(project(":kamp-common"))
    implementation(group = "org.slf4j", name = "slf4j-log4j12", version = "1.7.25")
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
    configProperty("lvdm.respawn.cash.amount", "2500")
}
