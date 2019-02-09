import groovy.lang.Closure

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(group = "ch.leadrian.samp.kamp", name = "kamp-gradle-plugins", version = "1.0.0-rc1")
    }
}

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
    `build-scan`
    jacoco
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

val gitVersion: Closure<String> by extra

allprojects {
    version = gitVersion()

    group = "ch.leadrian.samp.kamp"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

configure(subprojects - project("kamp-plugin")) {
    apply(plugin = "kotlin")
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")

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

        dokka {
            reportUndocumented = false
        }
    }

    dependencies {
        api(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.11")
        api(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.3.11")
        api(group = "com.netflix.governator", name = "governator", version = "1.17.5")
        api(group = "com.google.inject", name = "guice", version = "4.2.2")
        api(group = "javax.inject", name = "javax.inject", version = "1")
        api(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")

        testImplementation(group = "io.mockk", name = "mockk", version = "1.9")
        testImplementation(group = "org.assertj", name = "assertj-core", version = "3.11.1")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = "5.3.2")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.3.2")

        testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.2")
    }

}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}
