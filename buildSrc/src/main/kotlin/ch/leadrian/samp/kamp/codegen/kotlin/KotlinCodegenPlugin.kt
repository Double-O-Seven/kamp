package ch.leadrian.samp.kamp.codegen.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class KotlinCodegenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("kampKotlinCodegen", KotlinCodegenExtension::class.java)
        val generateKampKotlinFilesTask = project.tasks.create("generateKampKotlinFiles", KotlinCodegenTask::class.java)
        project.tasks.withType(JavaCompile::class.java) { it.dependsOn(generateKampKotlinFilesTask) }
        project.tasks.withType(KotlinCompile::class.java) { it.dependsOn(generateKampKotlinFilesTask) }
    }
}