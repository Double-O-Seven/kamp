package ch.leadrian.samp.kamp.annotationprocessor.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import java.time.LocalDateTime
import javax.annotation.Generated
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