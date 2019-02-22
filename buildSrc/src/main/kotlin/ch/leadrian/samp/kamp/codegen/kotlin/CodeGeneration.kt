package ch.leadrian.samp.kamp.codegen.kotlin

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import java.time.LocalDateTime
import kotlin.reflect.KClass

val generatedAnnotation = ClassName("ch.leadrian.samp.kamp.annotations", "Generated")

fun TypeSpec.Builder.addGeneratedAnnotation(generatedBy: KClass<*>): TypeSpec.Builder {
    return addAnnotation(
            AnnotationSpec
                    .builder(generatedAnnotation)
                    .addMember("value = [%S]", generatedBy.java.name)
                    .addMember("date = %S", LocalDateTime.now().toString())
                    .build()
    )
}
