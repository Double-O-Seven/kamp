package ch.leadrian.samp.kamp.codegen.kotlin

open class KotlinCodegenExtension {

    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    var amxApiJavaPackageName: String = "ch.leadrian.samp.kamp.core.api.amx"

    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}