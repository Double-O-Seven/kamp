package ch.leadrian.samp.kamp.codegen.java

open class JavaCodegenExtension {

    var constantsJavaPackageName: String = "ch.leadrian.samp.kamp.core.api.constants"

    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    var outputDirectoryPath: Any? = null

    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}