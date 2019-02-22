package ch.leadrian.samp.kamp.annotationprocessor.codegen

import ch.leadrian.samp.kamp.annotations.Generated
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.time.LocalDateTime
import kotlin.reflect.KClass

fun TypeSpec.Builder.addGeneratedAnnotation(generatedBy: KClass<*>): TypeSpec.Builder {
    return addAnnotation(
            AnnotationSpec
                    .builder(Generated::class)
                    .addMember("value = [%S]", generatedBy.java.name)
                    .addMember("date = %S", LocalDateTime.now().toString())
                    .build()
    )
}

fun FunSpec.Builder.addGeneratedAnnotation(generatedBy: KClass<*>): FunSpec.Builder {
    return addAnnotation(
            AnnotationSpec
                    .builder(Generated::class)
                    .addMember("value = [%S]", generatedBy.java.name)
                    .addMember("date = %S", LocalDateTime.now().toString())
                    .build()
    )
}