package ch.leadrian.samp.kamp.runtimecodegen

class ReferenceStringMethodParameterGenerator(
        private val parameterName: String,
        private val sizeParameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterJavaClassVariable = "${parameterName}Class"
    private val parameterValueFieldIDVariable = "${parameterName}ValueFieldID"
    private val parameterOutVariable = "${parameterName}Out"

    override fun generatePreCallSetup(): String? =
            """
                |$indentation// TODO this might not be the best option in case of a server restart!
                |${indentation}static auto $parameterJavaClassVariable = env->GetObjectClass($parameterName);
                |${indentation}static auto $parameterValueFieldIDVariable = env->GetFieldID(${parameterName}Class, "value", "Ljava/lang/String;");
                |${indentation}char *$parameterOutVariable = new char[$sizeParameterName];
            """.trimMargin()

    override fun generateMethodCallParameter(): String = "$parameterOutVariable, sizeof(char) * $sizeParameterName"

    override fun generateResultProcessing(): String? =
            """
                |${indentation}env->SetObjectField($parameterJavaClassVariable, $parameterValueFieldIDVariable, env->NewStringUTF($parameterOutVariable));
                |${indentation}delete[] $parameterOutVariable;
                |
            """.trimMargin()
}