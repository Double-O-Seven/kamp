package ch.leadrian.samp.kamp.codegen.cpp

internal interface MethodParameterGenerator {

    fun generateMethodCallSetup(): String?

    fun generateJniMethodCallParameter(): String

    fun generateMethodCallResultProcessing(): String?
}