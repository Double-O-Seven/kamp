package ch.leadrian.samp.kamp.codegen.kotlin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles

open class KotlinCodegenExtension {

    @get:Input
    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    @get:Input
    var amxApiJavaPackageName: String = "ch.leadrian.samp.kamp.core.api.amx"

    @get:InputFiles
    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}