plugins {
    id("kamp-textkeygen")
}

dependencies {
    api(project(":kamp-core"))
}

textKeyGenerator {
    packageName("ch.leadrian.samp.kamp.view")
}