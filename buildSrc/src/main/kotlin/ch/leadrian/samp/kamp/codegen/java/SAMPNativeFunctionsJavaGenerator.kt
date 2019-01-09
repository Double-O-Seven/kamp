package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.hasNoImplementation
import ch.leadrian.samp.kamp.codegen.isNative
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.Writer
import java.time.LocalDateTime
import javax.annotation.Generated
import javax.lang.model.element.Modifier

internal class SAMPNativeFunctionsJavaGenerator(
        private val functions: List<Function>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPNativeFunctions.java"

    override fun generate(writer: Writer) {
        val sampNativeFunctionsTypeSpecBuilder = TypeSpec
                .classBuilder("SAMPNativeFunctions")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addGeneratedAnnotation()
                .addPrivateConstructor()
                .addNativeMethods()
        writer.writeJavaFile(sampNativeFunctionsTypeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        return addAnnotation(
                AnnotationSpec
                        .builder(Generated::class.java)
                        .addMember("value", "\$S", this@SAMPNativeFunctionsJavaGenerator::class.java.name)
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

    private fun TypeSpec.Builder.addNativeMethods(): TypeSpec.Builder {
        functions
                .filter { it.isNative && !it.hasNoImplementation }
                .forEach { function -> addNativeMethod(function) }
        return this
    }

    private fun TypeSpec.Builder.addNativeMethod(function: Function) {
        val returnType = getJavaType(function.type)
        val methodSpecBuilder = MethodSpec
                .methodBuilder(function.camelCaseName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.NATIVE)
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
        val parameterType = when {
            parameter.hasAttribute("out") -> getJavaOutType(parameter.type)
            else -> getJavaType(parameter.type)
        }
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