package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.model.Types


val JAVA_TYPE_MAPPING = mapOf(
        Types.BOOL to "boolean",
        Types.CHAR to "char",
        Types.FLOAT to "float",
        Types.INT to "int",
        Types.STRING to "String",
        Types.VOID to "void"
)

val JAVA_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "ch.leadrian.samp.kamp.api.types.ReferenceFloat",
        Types.INT to "ch.leadrian.samp.kamp.api.types.ReferenceInt",
        Types.STRING to "ch.leadrian.samp.kamp.api.types.ReferenceString"
)

fun getJavaType(typeName: String) =
        JAVA_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Java type: $typeName")

fun getJavaOutType(typeName: String) =
        JAVA_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Java out type: $typeName")
