package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.cidl.model.Types
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName

private val KOTLIN_TYPE_MAPPING: Map<String, ClassName> = mapOf(
        Types.BOOL to BOOLEAN,
        Types.CHAR to CHAR,
        Types.FLOAT to FLOAT,
        Types.INT to INT,
        Types.STRING to String::class.asClassName(),
        Types.VOID to UNIT
)

private val KOTLIN_OUT_TYPE_MAPPING: Map<String, ClassName> = mapOf(
        Types.FLOAT to ClassName("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceFloat"),
        Types.INT to ClassName("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceInt"),
        Types.STRING to ClassName("ch.leadrian.samp.kamp.core.runtime.types", "ReferenceString")
)

fun getKotlinType(typeName: String): ClassName =
        KOTLIN_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Kotlin type: $typeName")

fun getKotlinOutType(typeName: String): ClassName =
        KOTLIN_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown Kotlin out type: $typeName")
