plugins {
    `kotlin-kapt`
}

dependencies {
    compileOnly(project(":kamp-annotations"))

    implementation("com.conversantmedia:rtree:1.0.5")

    api(project(":kamp-core"))

    kapt(project(":kamp-annotation-processor"))
}