package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Types

val PRIMITIVE_JAVA_TYPES = setOf(
        "boolean",
        "byte",
        "char",
        "short",
        "int",
        "long",
        "double",
        "float",
        "void"
)

val JAVA_TYPE_MAPPING = mapOf(
        Types.BOOL to "boolean",
        Types.CHAR to "char",
        Types.FLOAT to "float",
        Types.INT to "int",
        Types.STRING to "java.lang.String",
        Types.VOID to "void"
)

val JAVA_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat",
        Types.INT to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt",
        Types.STRING to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceString"
)

val KOTLIN_TYPE_MAPPING = mapOf(
        Types.BOOL to "Boolean",
        Types.CHAR to "Char",
        Types.FLOAT to "Float",
        Types.INT to "Int",
        Types.STRING to "kotlin.String",
        Types.VOID to "Unit"
)

val KOTLIN_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat",
        Types.INT to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt",
        Types.STRING to "ch.leadrian.samp.kamp.core.runtime.types.ReferenceString"
)

fun getJavaType(typeName: String) =
        JAVA_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Java type: $typeName")

fun getJavaOutType(typeName: String) =
        JAVA_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Java out type: $typeName")

fun getKotlinType(typeName: String) =
        KOTLIN_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Kotlin type: $typeName")

fun getKotlinOutType(typeName: String) =
        KOTLIN_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Kotlin out type: $typeName")

fun isPrimitiveJavaType(typeName: String): Boolean = PRIMITIVE_JAVA_TYPES.contains(typeName)