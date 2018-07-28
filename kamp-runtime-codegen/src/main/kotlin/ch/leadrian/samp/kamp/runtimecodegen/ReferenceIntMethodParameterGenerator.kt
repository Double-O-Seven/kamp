package ch.leadrian.samp.kamp.runtimecodegen

class ReferenceIntMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterJavaClassVariable = "${parameterName}Class"
    private val parameterValueFieldIDVariable = "${parameterName}ValueFieldID"
    private val parameterOutVariable = "${parameterName}Out"

    override fun generatePreCallSetup(): String? =
            """
                |$indentation// TODO this might not be the best option in case of a server restart!
                |${indentation}static auto $parameterJavaClassVariable = env->GetObjectClass($parameterName);
                |${indentation}static auto $parameterValueFieldIDVariable = env->GetFieldID($parameterJavaClassVariable, "value", "I");
                |${indentation}int $parameterOutVariable;
                |""".trimMargin()

    override fun generateMethodCallParameter(): String = "&$parameterOutVariable"

    override fun generateResultProcessing(): String? =
            "${indentation}env->SetIntField($parameterName, $parameterValueFieldIDVariable, $parameterOutVariable);\n"
}