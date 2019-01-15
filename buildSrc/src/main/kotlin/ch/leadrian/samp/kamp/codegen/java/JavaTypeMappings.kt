package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Types
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

val REFERENCE_INT_TYPE: ClassName = ClassName.get(
        "ch.leadrian.samp.kamp.core.runtime.types",
        "ReferenceInt"
)

val REFERENCE_FLOAT_TYPE: ClassName = ClassName.get(
        "ch.leadrian.samp.kamp.core.runtime.types",
        "ReferenceFloat"
)

val REFERENCE_STRING_TYPE: ClassName = ClassName.get(
        "ch.leadrian.samp.kamp.core.runtime.types",
        "ReferenceString"
)

val STRING_ENCODING_TYPE: ClassName = ClassName.get(
        "ch.leadrian.samp.kamp.core.runtime",
        "StringEncoding"
)

val JAVA_TYPE_MAPPING: Map<String, TypeName> by lazy {
    mapOf(
            Types.BOOL to TypeName.BOOLEAN,
            Types.CHAR to TypeName.CHAR,
            Types.FLOAT to TypeName.FLOAT,
            Types.INT to TypeName.INT,
            Types.STRING to ArrayTypeName.of(Byte::class.javaPrimitiveType),
            Types.VOID to TypeName.VOID
    )
}

fun getJavaType(typeName: String): TypeName =
        JAVA_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown type: $typeName")
