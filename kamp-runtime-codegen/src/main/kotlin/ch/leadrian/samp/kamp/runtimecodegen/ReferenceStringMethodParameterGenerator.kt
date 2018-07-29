package ch.leadrian.samp.kamp.runtimecodegen

class ReferenceStringMethodParameterGenerator(
        private val parameterName: String,
        private val sizeParameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterValueFieldIDVariable = "${parameterName}ValueFieldID"
    private val parameterOutVariable = "${parameterName}Out"

    override fun generatePreCallSetup(): String? =
            """
                |${indentation}jfieldID $parameterValueFieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceStringValueFieldID();
                |${indentation}char *$parameterOutVariable = new char[$sizeParameterName];
            """.trimMargin()

    override fun generateMethodCallParameter(): String = "$parameterOutVariable, sizeof(char) * $sizeParameterName"

    override fun generateResultProcessing(): String? =
            """
                |${indentation}env->SetObjectField($parameterName, $parameterValueFieldIDVariable, env->NewStringUTF($parameterOutVariable));
                |${indentation}delete[] $parameterOutVariable;
                |
            """.trimMargin()
}