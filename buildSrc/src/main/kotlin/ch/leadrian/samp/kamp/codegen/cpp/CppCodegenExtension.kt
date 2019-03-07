package ch.leadrian.samp.kamp.codegen.cpp

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional

open class CppCodegenExtension {

    @get:Input
    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    @get:[Optional Input]
    var cppOutputDirectoryPath: Any? = null

    @get:[Optional Input]
    var headersOutputDirectoryPath: Any? = null

    @get:InputFiles
    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}