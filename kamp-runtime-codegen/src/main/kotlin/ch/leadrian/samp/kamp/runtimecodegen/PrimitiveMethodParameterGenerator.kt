package ch.leadrian.samp.kamp.runtimecodegen

internal class PrimitiveMethodParameterGenerator(private val parameterName: String) : MethodParameterGenerator {

    override fun generatePreCallSetup(): String? = null

    override fun generateMethodCallParameter(): String = parameterName

    override fun generateResultProcessing(): String? = null
}