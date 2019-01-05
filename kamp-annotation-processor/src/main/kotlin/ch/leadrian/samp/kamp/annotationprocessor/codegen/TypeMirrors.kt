package ch.leadrian.samp.kamp.annotationprocessor.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

fun TypeMirror.toKotlinTypeName(): TypeName {
    val kotlinClassName = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(this.toString()))?.asSingleFqName()?.asString()
    return kotlinClassName?.let { ClassName.bestGuess(it) } ?: this.asTypeName()
}

fun VariableElement.getKotlinTypeName(): TypeName {
    val kotlinType = asType().toKotlinTypeName()
    val nullable = when {
        getAnnotation(Nullable::class.java) != null -> true
        getAnnotation(NotNull::class.java) != null -> false
        else -> false
    }
    return kotlinType.copy(nullable)
}