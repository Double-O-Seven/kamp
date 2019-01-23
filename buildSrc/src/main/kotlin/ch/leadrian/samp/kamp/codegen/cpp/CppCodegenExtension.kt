package ch.leadrian.samp.kamp.codegen.cpp

open class CppCodegenExtension {

    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    var cppOutputDirectoryPath: Any? = null

    var headersOutputDirectoryPath: Any? = null

    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}