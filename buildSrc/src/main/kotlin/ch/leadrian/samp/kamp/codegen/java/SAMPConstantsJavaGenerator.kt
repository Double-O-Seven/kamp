package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Constant
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.io.File
import java.io.Writer
import java.time.LocalDateTime
import javax.annotation.Generated
import javax.lang.model.element.Modifier

internal class SAMPConstantsJavaGenerator(
        private val constants: List<Constant>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPConstants.java"

    override fun generate(writer: Writer) {
        val sampConstantsTypeSpecBuilder = TypeSpec
                .classBuilder("SAMPConstants")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addGeneratedAnnotation()
                .addPrivateConstructor()
                .addFields()
        writer.writeJavaFile(sampConstantsTypeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        return addAnnotation(
                AnnotationSpec
                        .builder(Generated::class.java)
                        .addMember("value", "\$S", this@SAMPConstantsJavaGenerator::class.java.name)
                        .addMember("date", "\$S", LocalDateTime.now().toString())
                        .build()
        )
    }

    private fun TypeSpec.Builder.addPrivateConstructor(): TypeSpec.Builder {
        return addMethod(
                MethodSpec
                        .constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build()
        )
    }

    private fun TypeSpec.Builder.addFields(): TypeSpec.Builder {
        constants.forEach { addField(it.toFieldSpec()) }
        return this
    }

    private fun Constant.toFieldSpec(): FieldSpec {
        return FieldSpec
                .builder(getJavaType(type), name, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", value.data)
                .build()
    }

    private fun Writer.writeJavaFile(typeSpec: TypeSpec) {
        JavaFile
                .builder(javaPackageName, typeSpec)
                .skipJavaLangImports(true)
                .build()
                .writeTo(this)
    }

}