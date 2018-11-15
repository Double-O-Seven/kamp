package ch.leadrian.samp.kamp.codegen.cpp

internal class ReferenceFloatMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val fieldIDVariable = "${parameterName}ValueFieldID"
    private val outVariable = "${parameterName}Out"

    override fun generateMethodCallSetup(): String? =
            """
                |${indentation}jfieldID $fieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceFloatValueFieldID();
                |${indentation}float $outVariable;
                |""".trimMargin()

    override fun generateJniMethodCallParameter(): String = "&$outVariable"

    override fun generateMethodCallResultProcessing(): String? =
            "${indentation}env->SetFloatField($parameterName, $fieldIDVariable, $outVariable);\n"
}