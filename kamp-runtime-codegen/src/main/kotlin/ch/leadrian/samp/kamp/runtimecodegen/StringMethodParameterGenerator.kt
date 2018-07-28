package ch.leadrian.samp.kamp.runtimecodegen

class StringMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterCharsVariable = "${parameterName}Chars"

    override fun generatePreCallSetup(): String? =
            "${indentation}const char *$parameterCharsVariable = env->GetStringUTFChars($parameterName, nullptr);\n"

    override fun generateMethodCallParameter(): String = parameterCharsVariable

    override fun generateResultProcessing(): String? =
            "${indentation}env->ReleaseStringUTFChars($parameterName, $parameterCharsVariable);\n"
}