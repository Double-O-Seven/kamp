package ch.leadrian.samp.kamp.codegen

internal interface MethodParameterGenerator {

    fun generatePreCallSetup(): String?

    fun generateMethodCallParameter(): String

    fun generateResultProcessing(): String?
}