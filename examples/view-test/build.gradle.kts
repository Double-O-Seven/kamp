import org.gradle.internal.os.OperatingSystem

dependencies {
    implementation(project(":kamp-core"))
    implementation(project(":kamp-view"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
}

plugins {
    id("kamp-serverstarter")
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.viewtest.ViewTestGameMode"
    kampPluginBinaryPath = getKampPluginBinaryPath()
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.streamer.rate.ms", "300")
}

fun getKampPluginBinaryPath(): String {
    return OperatingSystem
            .current()
            .getSharedLibraryName("${project(":kamp-plugin").buildDir}/lib/main/release/${OperatingSystem.current().familyName}/Kamp")
}

tasks {
    configureServer {
        dependsOn(project(":kamp-plugin").tasks.getByName("build"))
    }
}
