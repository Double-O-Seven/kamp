package ch.leadrian.samp.kamp.runtimecodegen

class JstringMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterJstringVariable = "${parameterName}Jstring"

    override fun generatePreCallSetup(): String? =
            "${indentation}jstring $parameterJstringVariable = jniEnv->NewStringUTF($parameterName);"

    override fun generateMethodCallParameter(): String = parameterJstringVariable

    override fun generateResultProcessing(): String? =
            "${indentation}jniEnv->DeleteLocalRef($parameterJstringVariable);"
}