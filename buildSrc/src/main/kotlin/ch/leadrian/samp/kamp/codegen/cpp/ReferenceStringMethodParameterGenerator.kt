package ch.leadrian.samp.kamp.codegen.cpp

internal class ReferenceStringMethodParameterGenerator(
        private val parameterName: String,
        private val sizeParameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val fieldIDVariable = "${parameterName}ValueFieldID"
    private val outVariable = "${parameterName}Out"

    override fun generateMethodCallSetup(): String? =
            """
                |${indentation}jfieldID $fieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceStringValueFieldID();
                |${indentation}char *$outVariable = new char[$sizeParameterName];
            """.trimMargin()

    override fun generateJniMethodCallParameter(): String = "$outVariable, sizeof(char) * $sizeParameterName"

    override fun generateMethodCallResultProcessing(): String? =
            """
                |${indentation}env->SetObjectField($parameterName, $fieldIDVariable, env->NewStringUTF($outVariable));
                |${indentation}delete[] $outVariable;
                |
            """.trimMargin()
}