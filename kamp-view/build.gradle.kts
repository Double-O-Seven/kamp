plugins {
    id("kamp-textkeygen")
}

dependencies {
    api(project(":kamp-core"))
}

val generatedSrcJavaDir = "$buildDir/generated-src/main/java"

sourceSets {
    main {
        java {
            srcDir(generatedSrcJavaDir)
        }
    }
}

textKeyGenerator {
    outputDirectory = generatedSrcJavaDir
    packageName("ch.leadrian.samp.kamp.view")
    resourcesDirectory = projectDir.absolutePath + "/src/main/resources"
}