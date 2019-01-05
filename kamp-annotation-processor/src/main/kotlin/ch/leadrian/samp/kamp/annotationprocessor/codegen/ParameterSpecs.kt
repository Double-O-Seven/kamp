package ch.leadrian.samp.kamp.annotationprocessor.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.VariableElement
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

fun ParameterSpec.fixType(variable: VariableElement): ParameterSpec {
    val kotlinClassName = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(variable.asType().toString()))?.asSingleFqName()?.asString()
    val kotlinType = kotlinClassName?.let { ClassName.bestGuess(it) } ?: type
    val nullable = when {
        variable.getAnnotation(Nullable::class.java) != null -> true
        variable.getAnnotation(NotNull::class.java) != null -> false
        else -> type.isNullable
    }
    return toBuilder(type = kotlinType.copy(nullable = nullable)).build()
}