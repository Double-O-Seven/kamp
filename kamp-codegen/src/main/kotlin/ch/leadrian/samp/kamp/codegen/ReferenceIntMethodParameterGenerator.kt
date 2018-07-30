package ch.leadrian.samp.kamp.codegen

class ReferenceIntMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterValueFieldIDVariable = "${parameterName}ValueFieldID"
    private val parameterOutVariable = "${parameterName}Out"

    override fun generatePreCallSetup(): String? =
            """
                |${indentation}jfieldID $parameterValueFieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceIntValueFieldID();
                |${indentation}int $parameterOutVariable;
                |""".trimMargin()

    override fun generateMethodCallParameter(): String = "&$parameterOutVariable"

    override fun generateResultProcessing(): String? =
            "${indentation}env->SetIntField($parameterName, $parameterValueFieldIDVariable, $parameterOutVariable);\n"
}