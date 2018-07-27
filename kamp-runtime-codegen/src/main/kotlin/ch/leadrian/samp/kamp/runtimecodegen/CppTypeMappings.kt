package ch.leadrian.samp.kamp.runtimecodegen

import ch.leadrian.samp.cidl.model.Types

val CPP_TYPE_MAPPING = mapOf(
        Types.BOOL to "jboolean",
        Types.CHAR to "jchar",
        Types.FLOAT to "jfloat",
        Types.INT to "jint",
        Types.STRING to "jstring",
        Types.VOID to "void"
)

val CPP_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "jobject",
        Types.INT to "jobject",
        Types.STRING to "jobject"
)

fun getCppType(typeName: String) =
        CPP_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown C++ type: $typeName")

fun getCppOutType(typeName: String) =
        CPP_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown C++ out type: $typeName")
