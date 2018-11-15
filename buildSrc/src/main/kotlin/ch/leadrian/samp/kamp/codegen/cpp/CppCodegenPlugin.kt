package ch.leadrian.samp.kamp.codegen.cpp

import org.gradle.api.Plugin
import org.gradle.api.Project

open class CppCodegenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("kampCppCodegen", CppCodegenExtension::class.java)
        project.tasks.create("generateKampCppFiles", CppCodegenTask::class.java)
    }
}