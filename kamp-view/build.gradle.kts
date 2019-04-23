plugins {
    id("ch.leadrian.samp.kamp.kamp-textkey-generator") version "1.0.0-rc2"
}

dependencies {
    api(project(":kamp-core"))
}

textKeyGenerator {
    className = "KampViewTextKeys"
}