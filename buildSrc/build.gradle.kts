import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        repositories {
            mavenCentral()
            maven {
                setUrl("https://plugins.gradle.org/m2/")
            }
        }

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
    }
}

plugins {
    java
    kotlin("jvm") version "1.3.11"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version = "1.3.11")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.11")
    implementation(group = "ch.leadrian.samp.cidl", name = "cidl-kotlin", version = "0.0.3")
    implementation(group = "com.squareup", name = "javapoet", version = "1.11.1")
    implementation(group = "com.squareup", name = "kotlinpoet", version = "1.0.1")
    implementation(gradleApi())
}

tasks {
    compileKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
