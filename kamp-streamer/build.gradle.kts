plugins {
    `kotlin-kapt`
}

dependencies {
    compileOnly(project(":kamp-annotations"))

    implementation("com.conversantmedia:rtree:1.0.5")
    implementation(group = "com.google.guava", name = "guava", version = "27.0.1-jre")

    api(project(":kamp-core"))

    kapt(project(":kamp-annotation-processor"))
}