package ch.leadrian.samp.kamp.codegen.cpp

internal class JbooleanMethodParameterGenerator(private val parameterName: String) : MethodParameterGenerator {

    override fun generateMethodCallSetup(): String? = null

    override fun generateMethodInvocationParameter(): String = "($parameterName ? true : false)"

    override fun generateMethodCallResultProcessing(): String? = null
}