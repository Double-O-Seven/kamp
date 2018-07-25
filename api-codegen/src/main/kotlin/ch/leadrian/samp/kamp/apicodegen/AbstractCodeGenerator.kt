package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.model.Constant
import ch.leadrian.samp.cidl.model.Declarations
import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.parser.DeclarationsParser

abstract class AbstractCodeGenerator {

    private val declarationsParser = DeclarationsParser()

    fun generate(vararg sources: InterfaceDefinitionSource) {
        val constants: MutableList<Constant> = mutableListOf()
        val functions: MutableList<Function> = mutableListOf()
        sources.forEach { source ->
            source.getInputStream().use {
                val declarations = declarationsParser.parse(it)
                constants += declarations.constants
                functions += declarations.functions
            }
        }
        generate(Declarations(constants, functions))
    }

    abstract fun generate(declarations: Declarations)

}