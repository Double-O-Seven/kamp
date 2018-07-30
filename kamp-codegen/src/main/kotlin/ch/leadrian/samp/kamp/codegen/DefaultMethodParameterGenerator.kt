package ch.leadrian.samp.kamp.codegen

internal class DefaultMethodParameterGenerator(private val parameterName: String) : MethodParameterGenerator {

    override fun generatePreCallSetup(): String? = null

    override fun generateMethodCallParameter(): String = parameterName

    override fun generateResultProcessing(): String? = null
}