plugins {
    id("kamp-textkey-generator")
}

dependencies {
    api(project(":kamp-core"))
}

textKeyGenerator {
    className = "KampViewTextKeys"
}