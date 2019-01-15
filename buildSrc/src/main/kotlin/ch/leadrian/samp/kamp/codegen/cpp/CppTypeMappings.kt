package ch.leadrian.samp.kamp.codegen.cpp

import ch.leadrian.samp.cidl.model.Types

private val JNI_TYPE_MAPPING = mapOf(
        Types.BOOL to "jboolean",
        Types.CHAR to "jchar",
        Types.FLOAT to "jfloat",
        Types.INT to "jint",
        Types.STRING to "jbyteArray",
        Types.VOID to "void"
)

private val CPP_TYPE_MAPPING = mapOf(
        Types.BOOL to "bool",
        Types.CHAR to "char",
        Types.FLOAT to "float",
        Types.INT to "int",
        Types.STRING to "const char *",
        Types.VOID to "void"
)

private val JNI_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "jobject",
        Types.INT to "jobject",
        Types.STRING to "jobject"
)

private val JVM_TYPE_SIGNATURES = mapOf(
        Types.BOOL to "Z",
        Types.CHAR to "C",
        Types.FLOAT to "F",
        Types.INT to "I",
        Types.STRING to "[B",
        Types.VOID to "V"
)

fun getJniType(typeName: String): String =
        JNI_TYPE_MAPPING[typeName] ?: throwException(typeName)

fun getJniOutType(typeName: String): String =
        JNI_OUT_TYPE_MAPPING[typeName] ?: throwException(typeName)

fun getCppType(typeName: String): String =
        CPP_TYPE_MAPPING[typeName] ?: throwException(typeName)

fun getJvmTypeSignature(typeName: String): String =
        JVM_TYPE_SIGNATURES[typeName] ?: throwException(typeName)

private fun throwException(typeName: String): Nothing {
    throw IllegalArgumentException("Unknown type: $typeName")
}
