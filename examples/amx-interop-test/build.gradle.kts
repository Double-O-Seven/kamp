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
    windowsKampPluginFile = project(":kamp-plugin").buildDir.resolve("lib/main/release/windows/kamp.dll")
    linuxKampPluginFile = project(":kamp-plugin").buildDir.resolve("lib/main/release/linux/libkamp.so")
}

tasks {
    configureServer {
        dependsOn(project(":kamp-plugin").tasks.assemble)
    }
}
