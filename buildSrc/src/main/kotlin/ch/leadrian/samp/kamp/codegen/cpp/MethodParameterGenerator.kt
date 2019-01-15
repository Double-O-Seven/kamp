package ch.leadrian.samp.kamp.codegen.cpp

internal interface MethodParameterGenerator {

    fun generateMethodCallSetup(): String?

    fun generateMethodInvocationParameter(): String

    fun generateMethodCallResultProcessing(): String?
}