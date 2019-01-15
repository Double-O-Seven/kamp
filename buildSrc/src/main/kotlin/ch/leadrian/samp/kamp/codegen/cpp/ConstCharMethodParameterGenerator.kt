package ch.leadrian.samp.kamp.codegen.cpp

internal class ConstCharMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val parameterJbyteArrayVariable = "${parameterName}JbyteArray"
    private val parameterCharsVariable = "${parameterName}Chars"
    private val parameterArrayLengthVariable = "${parameterName}Length"

    override fun generateMethodCallSetup(): String? = "" +
            "${indentation}auto $parameterArrayLengthVariable = env->GetArrayLength($parameterName);\n" +
            "${indentation}jbyte *$parameterJbyteArrayVariable = env->GetByteArrayElements($parameterName, 0);\n" +
            "${indentation}char *$parameterCharsVariable = new char[$parameterArrayLengthVariable + 1];\n" +
            "${indentation}std::memcpy($parameterCharsVariable, $parameterJbyteArrayVariable, $parameterArrayLengthVariable);\n" +
            "$indentation$parameterCharsVariable[$parameterArrayLengthVariable] = 0;\n"

    override fun generateMethodInvocationParameter(): String = parameterCharsVariable

    override fun generateMethodCallResultProcessing(): String? = "" +
            "${indentation}delete[] $parameterCharsVariable;\n" +
            "${indentation}env->ReleaseByteArrayElements($parameterName, $parameterJbyteArrayVariable, JNI_ABORT);\n"
}