package ch.leadrian.samp.kamp.annotationprocessor.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.VariableElement

private val JAVA_TO_KOTLIN_TYPE_MAPPINGS = mapOf(
        "java.lang.String" to ClassName("kotlin", "String"),
        "java.lang.Integer" to ClassName("kotlin", "Int"),
        "java.lang.Float" to ClassName("kotlin", "Float")
)

fun ParameterSpec.fixType(variable: VariableElement): ParameterSpec {
    val kotlinType = JAVA_TO_KOTLIN_TYPE_MAPPINGS[variable.asType().toString()] ?: type
    val nullable = when {
        variable.getAnnotation(Nullable::class.java) != null -> true
        variable.getAnnotation(NotNull::class.java) != null -> false
        else -> type.isNullable
    }
    return toBuilder(type = kotlinType.copy(nullable = nullable)).build()
}