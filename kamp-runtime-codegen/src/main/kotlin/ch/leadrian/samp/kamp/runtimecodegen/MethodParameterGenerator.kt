package ch.leadrian.samp.kamp.runtimecodegen

internal interface MethodParameterGenerator {

    fun generatePreCallSetup(): String?

    fun generateMethodCallParameter(): String

    fun generateResultProcessing(): String?
}