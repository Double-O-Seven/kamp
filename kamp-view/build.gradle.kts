plugins {
    id("kamp-textkeygen")
}

dependencies {
    api(project(":kamp-core"))
}

textKeyGenerator {
    className = "KampViewTextKeys"
    packageName("ch.leadrian.samp.kamp.view")
}