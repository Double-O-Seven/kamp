package ch.leadrian.samp.kamp.codegen.cpp

internal class DefaultMethodParameterGenerator(private val parameterName: String) : MethodParameterGenerator {

    override fun generateMethodCallSetup(): String? = null

    override fun generateJniMethodCallParameter(): String = parameterName

    override fun generateMethodCallResultProcessing(): String? = null
}