dependencies {
    implementation(project(":kamp-core"))
    implementation(project(":kamp-common"))
    implementation(group = "org.slf4j", name = "slf4j-log4j12", version = "1.7.25")
}

plugins {
    id("kamp-serverstarter")
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.amxinteroptest.AmxInteropTestGameMode"
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.ignore.version.mismatch", "true")
    configProperty("lvdm.respawn.cash.amount", "2500")
}
