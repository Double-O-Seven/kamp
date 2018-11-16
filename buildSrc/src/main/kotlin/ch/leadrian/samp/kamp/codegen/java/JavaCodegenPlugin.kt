package ch.leadrian.samp.kamp.codegen.java

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class JavaCodegenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("kampJavaCodegen", JavaCodegenExtension::class.java)
        val generateKampJavaFilesTask = project.tasks.create("generateKampJavaFiles", JavaCodegenTask::class.java)
        project.tasks.withType(JavaCompile::class.java) { it.dependsOn(generateKampJavaFilesTask) }
        project.tasks.withType(KotlinCompile::class.java) { it.dependsOn(generateKampJavaFilesTask) }
    }
}