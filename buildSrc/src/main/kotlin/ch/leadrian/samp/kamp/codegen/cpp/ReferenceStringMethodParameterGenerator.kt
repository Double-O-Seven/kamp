package ch.leadrian.samp.kamp.codegen.cpp

internal class ReferenceStringMethodParameterGenerator(
        private val parameterName: String,
        private val sizeParameterName: String,
        private val indentation: String
) : MethodParameterGenerator {

    private val fieldIDVariable = "${parameterName}ValueFieldID"
    private val outVariable = "${parameterName}Out"
    private val numberOfCharsVariable = "${parameterName}NumChars"
    private val jbyteArrayVariable = "${parameterName}JbyteArray"
    private val jbyteArrayLengthVariable = "${parameterName}Length"

    override fun generateMethodCallSetup(): String? =
            """
                |${indentation}jfieldID $fieldIDVariable = Kamp::GetInstance().GetFieldCache().GetReferenceStringValueFieldID();
                |${indentation}auto $numberOfCharsVariable = $sizeParameterName * sizeof(jchar) / sizeof(char);
                |${indentation}char *$outVariable = new char[$numberOfCharsVariable];
                |
            """.trimMargin()

    override fun generateMethodInvocationParameter(): String = "$outVariable, $numberOfCharsVariable"

    override fun generateMethodCallResultProcessing(): String? =
            """
                |${indentation}auto $jbyteArrayLengthVariable = std::strlen($outVariable);
                |${indentation}jbyteArray $jbyteArrayVariable = env->NewByteArray($jbyteArrayLengthVariable);
                |${indentation}env->SetByteArrayRegion($jbyteArrayVariable, 0, $jbyteArrayLengthVariable, (const jbyte *) $outVariable);
                |${indentation}env->SetObjectField($parameterName, $fieldIDVariable, $jbyteArrayVariable);
                |${indentation}env->DeleteLocalRef($jbyteArrayVariable);
                |${indentation}delete[] $outVariable;
                |
            """.trimMargin()
}