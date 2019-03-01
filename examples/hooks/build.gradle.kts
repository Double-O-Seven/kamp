dependencies {
    implementation(project(":kamp-core"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
}

plugins {
    id("kamp-serverstarter")
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.hooks.HooksGameMode"
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
