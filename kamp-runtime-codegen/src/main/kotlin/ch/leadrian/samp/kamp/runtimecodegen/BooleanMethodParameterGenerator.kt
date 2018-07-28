package ch.leadrian.samp.kamp.runtimecodegen

internal class BooleanMethodParameterGenerator(private val parameterName: String) : MethodParameterGenerator {

    override fun generatePreCallSetup(): String? = null

    override fun generateMethodCallParameter(): String = "($parameterName ? true : false)"

    override fun generateResultProcessing(): String? = null
}