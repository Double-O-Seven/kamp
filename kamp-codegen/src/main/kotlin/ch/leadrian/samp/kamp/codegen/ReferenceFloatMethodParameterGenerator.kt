package ch.leadrian.samp.kamp.codegen

class ReferenceFloatMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterValueFieldIDVariable = "${parameterName}ValueFieldID"
    private val parameterOutVariable = "${parameterName}Out"

    override fun generatePreCallSetup(): String? =
            """
                |${indentation}jfieldID $parameterValueFieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceFloatValueFieldID();
                |${indentation}float $parameterOutVariable;
                |""".trimMargin()

    override fun generateMethodCallParameter(): String = "&$parameterOutVariable"

    override fun generateResultProcessing(): String? =
            "${indentation}env->SetFloatField($parameterName, $parameterValueFieldIDVariable, $parameterOutVariable);\n"
}