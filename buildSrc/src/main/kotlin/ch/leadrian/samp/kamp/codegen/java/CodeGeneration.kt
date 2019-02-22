package ch.leadrian.samp.kamp.codegen.java

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import java.time.LocalDateTime
import kotlin.reflect.KClass

val generatedAnnotation = ClassName.get("ch.leadrian.samp.kamp.annotations", "Generated")

fun TypeSpec.Builder.addGeneratedAnnotation(generatedBy: KClass<*>): TypeSpec.Builder {
    return addAnnotation(
            AnnotationSpec
                    .builder(generatedAnnotation)
                    .addMember("value", "\$S", generatedBy.java.name)
                    .addMember("date", "\$S", LocalDateTime.now().toString())
                    .build()
    )
}