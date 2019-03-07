package ch.leadrian.samp.kamp.codegen.java

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles

open class JavaCodegenExtension {

    @get:Input
    var constantsJavaPackageName: String = "ch.leadrian.samp.kamp.core.api.constants"

    @get:Input
    var runtimeJavaPackageName: String = "ch.leadrian.samp.kamp.core.runtime"

    @get:InputFiles
    val interfaceDefinitionFiles: MutableList<Any> = mutableListOf()

    fun interfaceDefinitionFiles(vararg values: Any) {
        interfaceDefinitionFiles.addAll(values)
    }

    fun interfaceDefintionFile(value: Any) {
        interfaceDefinitionFiles.add(value)
    }

}