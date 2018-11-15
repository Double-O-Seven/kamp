package ch.leadrian.samp.kamp.codegen.cpp

import org.gradle.api.NonNullApi

@NonNullApi
open class CppCodegenExtension {

    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core"

    var outputDirectoryPath: Any? = null

    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}