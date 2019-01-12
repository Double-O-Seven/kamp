val version: String by project

setVersion(version)

buildscript {
    dependencies {
        repositories {
            mavenCentral()
            mavenLocal()
            maven {
                setUrl("https://plugins.gradle.org/m2/")
            }
        }

        classpath(group = "ch.leadrian.samp.kamp", name = "kamp-gradle-plugins", version = "1.0.3")
    }
}

plugins {
    java
    kotlin("jvm")
    jacoco
}

allprojects {
    group = "ch.leadrian.samp.kamp"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")

    tasks {
        compileKotlin {
            sourceCompatibility = "1.8"
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=compatibility")
            }
        }

        compileTestKotlin {
            sourceCompatibility = "1.8"
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=compatibility")
            }
        }

        test {
            useJUnitPlatform()

            finalizedBy(jacocoTestReport)

            jacoco {
                toolVersion = "0.8.2"
            }
        }
    }

    dependencies {
        api(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.11")
        api(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.3.11")
        api(group = "com.netflix.governator", name = "governator", version = "1.17.5")
        api(group = "com.google.inject", name = "guice", version = "4.2.2")
        api(group = "javax.inject", name = "javax.inject", version = "1")
        api(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")

        implementation(group = "com.google.guava", name = "guava", version = "27.0.1-jre")

        testImplementation(group = "io.mockk", name = "mockk", version = "1.9")
        testImplementation(group = "org.assertj", name = "assertj-core", version = "3.11.1")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = "5.3.2")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.3.2")

        testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.2")
    }

}
