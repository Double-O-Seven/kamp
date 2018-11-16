package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Types
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

val JAVA_TYPE_MAPPING: Map<String, TypeName> = mapOf(
        Types.BOOL to TypeName.BOOLEAN,
        Types.CHAR to TypeName.CHAR,
        Types.FLOAT to TypeName.FLOAT,
        Types.INT to TypeName.INT,
        Types.STRING to ClassName.get(String::class.javaObjectType),
        Types.VOID to TypeName.VOID
)

val JAVA_OUT_TYPE_MAPPING: Map<String, TypeName> = mapOf(
        Types.FLOAT to ClassName.get("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceFloat"),
        Types.INT to ClassName.get("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceInt"),
        Types.STRING to ClassName.get("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceString")
)

fun getJavaType(typeName: String): TypeName =
        JAVA_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown type: $typeName")

fun getJavaOutType(typeName: String): TypeName =
        JAVA_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown type: $typeName")
