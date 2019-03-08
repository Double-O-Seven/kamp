import groovy.lang.Closure
import org.gradle.api.tasks.SourceSet.*

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(group = "ch.leadrian.samp.kamp", name = "kamp-textkey-generator", version = "1.0.0-rc2")
    }
}

plugins {
    kotlin("jvm")
    java
    `maven-publish`
    signing
    `build-scan`
    jacoco
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

val codacyCoverageReport: Configuration = configurations.create("codacyCoverageReport")

dependencies {
    codacyCoverageReport(group = "com.codacy", name = "codacy-coverage-reporter", version = "5.0.310")
}

val gitVersion: Closure<String> by extra

jacoco {
    toolVersion = "0.8.3"
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

allprojects {
    version = gitVersion()

    group = "ch.leadrian.samp.kamp"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks {
    jacocoTestReport {
        val projects = subprojects
                .minus(project("kamp-plugin"))
                .minus(project("kamp-annotations"))
                .minus(project("kamp-annotation-processor"))
        projects.forEach { dependsOn(it.tasks.test) }
        executionData.setFrom(projects.map { file("${it.buildDir}/jacoco/test.exec") })
        additionalSourceDirs.setFrom(projects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].allSource.sourceDirectories })
        sourceDirectories.setFrom(projects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].allSource.sourceDirectories })
        classDirectories.setFrom(projects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].output  })
        reports {
            xml.isEnabled = true
        }
    }
}

task<JavaExec>("codacyCoverageReport") {
    dependsOn(tasks.jacocoTestReport)
    main = "com.codacy.CodacyCoverageReporter"
    classpath = codacyCoverageReport
    args(
            "report",
            "-l",
            "Java",
            "-l",
            "Kotlin",
            "-r",
            "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
    )
}

configure(subprojects - project("kamp-plugin")) {
    apply(plugin = "kotlin")
    apply(plugin = "java")
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
        }

        dokka {
            reportUndocumented = false
        }
    }

    jacoco {
        toolVersion = "0.8.3"
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

configure(subprojects - project("kamp-plugin")) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    tasks.register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    tasks.register<Jar>("javadocJar") {
        from(tasks.dokka)
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])

                pom {
                    name.set("Kamp for San Andreas Multiplayer: Component ${project.name}")
                    description.set("Kotlin API for creating SA-MP gamemodes")
                    url.set("https://github.com/Double-O-Seven/kamp")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("Double-O-Seven")
                            name.set("Adrian-Philipp Leuenberger")
                            email.set("thewishwithin@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/Double-O-Seven/kamp.git")
                        developerConnection.set("scm:git:ssh://github.com/Double-O-Seven/kamp.git")
                        url.set("https://github.com/Double-O-Seven/kamp")
                    }
                }
            }
        }
        repositories {
            maven {
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                url = if (version.toString().contains("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                credentials {
                    val ossrhUsername: String? by extra
                    val ossrhPassword: String? by extra
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
    }
}
