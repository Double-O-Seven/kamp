package ch.leadrian.samp.kamp.codegen.cpp

internal class JstringMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val jstringVariable = "${parameterName}Jstring"

    override fun generateMethodCallSetup(): String? =
            "${indentation}jstring $jstringVariable = jniEnv->NewStringUTF($parameterName);"

    override fun generateJniMethodCallParameter(): String = jstringVariable

    override fun generateMethodCallResultProcessing(): String? =
            "${indentation}jniEnv->DeleteLocalRef($jstringVariable);"
}