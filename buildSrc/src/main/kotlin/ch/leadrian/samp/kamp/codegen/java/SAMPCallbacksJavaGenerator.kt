package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.isCallback
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.Writer
import java.time.LocalDateTime
import javax.annotation.Generated
import javax.lang.model.element.Modifier

internal class SAMPCallbacksJavaGenerator(
        private val functions: List<Function>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPCallbacks.java"

    override fun generate(writer: Writer) {
        val sampNativeFunctionsTypeSpecBuilder = TypeSpec
                .interfaceBuilder("SAMPCallbacks")
                .addModifiers(Modifier.PUBLIC)
                .addGeneratedAnnotation()
                .addMethods()
        writer.writeJavaFile(sampNativeFunctionsTypeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        return addAnnotation(AnnotationSpec
                .builder(Generated::class.java)
                .addMember("value", "\$S", this@SAMPCallbacksJavaGenerator::class.java.name)
                .addMember("date", "\$S", LocalDateTime.now().toString())
                .build())
    }

    private fun TypeSpec.Builder.addMethods(): TypeSpec.Builder {
        addOnProcessTickMethod()
        functions.filter { it.isCallback }.forEach { function -> addMethod(function) }
        return this
    }

    private fun TypeSpec.Builder.addOnProcessTickMethod() {
        val methodSpec = MethodSpec
                .methodBuilder("onProcessTick")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build()
        addMethod(methodSpec)
    }

    private fun TypeSpec.Builder.addMethod(function: Function) {
        val returnType = getJavaType(function.type)
        val methodSpecBuilder = MethodSpec
                .methodBuilder(function.camelCaseName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(returnType)
                .addParameters(function.parameters)
        if (!returnType.isPrimitive) {
            methodSpecBuilder.addAnnotation(NotNull::class.java)
        }
        addMethod(methodSpecBuilder.build())
    }

    private fun MethodSpec.Builder.addParameters(parameters: List<Parameter>): MethodSpec.Builder {
        parameters.forEach { parameter -> addParameter(parameter) }
        return this
    }

    private fun MethodSpec.Builder.addParameter(parameter: Parameter) {
        val parameterType = getJavaType(parameter.type)
        val parameterSpecBuilder = ParameterSpec.builder(parameterType, parameter.name)
        if (!parameterType.isPrimitive) {
            parameterSpecBuilder.addAnnotation(NotNull::class.java)
        }
        addParameter(parameterSpecBuilder.build())
    }

    private fun Writer.writeJavaFile(typeSpec: TypeSpec) {
        JavaFile
                .builder(javaPackageName, typeSpec)
                .skipJavaLangImports(true)
                .build()
                .writeTo(this)
    }

}