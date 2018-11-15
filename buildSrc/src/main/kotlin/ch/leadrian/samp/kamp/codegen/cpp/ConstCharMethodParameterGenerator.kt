package ch.leadrian.samp.kamp.codegen.cpp

internal class ConstCharMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterCharsVariable = "${parameterName}Chars"

    override fun generateMethodCallSetup(): String? =
            "${indentation}const char *$parameterCharsVariable = env->GetStringUTFChars($parameterName, nullptr);\n"

    override fun generateJniMethodCallParameter(): String = parameterCharsVariable

    override fun generateMethodCallResultProcessing(): String? =
            "${indentation}env->ReleaseStringUTFChars($parameterName, $parameterCharsVariable);\n"
}