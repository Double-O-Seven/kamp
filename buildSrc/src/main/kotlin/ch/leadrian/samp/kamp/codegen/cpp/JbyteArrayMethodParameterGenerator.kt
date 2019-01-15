package ch.leadrian.samp.kamp.codegen.cpp

internal class JbyteArrayMethodParameterGenerator(
        private val parameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val jbyteArrayVariable = "${parameterName}JbyteArray"
    private val jbyteArrayLengthVariable = "${parameterName}Length"

    override fun generateMethodCallSetup(): String? = "" +
            "${indentation}auto $jbyteArrayLengthVariable = std::strlen($parameterName);\n" +
            "${indentation}jbyteArray $jbyteArrayVariable = jniEnv->NewByteArray($jbyteArrayLengthVariable);\n" +
            "${indentation}jniEnv->SetByteArrayRegion($jbyteArrayVariable, 0, $jbyteArrayLengthVariable, (const jbyte *) $parameterName);\n"

    override fun generateMethodInvocationParameter(): String = jbyteArrayVariable

    override fun generateMethodCallResultProcessing(): String? =
            "${indentation}jniEnv->DeleteLocalRef($jbyteArrayVariable);"
}