package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.Types
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.hasNoImplementation
import ch.leadrian.samp.kamp.codegen.isNative
import ch.leadrian.samp.kamp.codegen.isOutParameter
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
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

    private val sampNativeFunctionsParameterGeneratorFactory = SAMPNativeFunctionsParameterGeneratorFactory()

    override val fileName: String = "SAMPNativeFunctions.java"

    override fun generate(writer: Writer) {
        val sampNativeFunctionsTypeSpecBuilder = TypeSpec
                .classBuilder("SAMPNativeFunctions")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addGeneratedAnnotation()
                .addPrivateConstructor()
                .addMethods()
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

    private fun TypeSpec.Builder.addMethods(): TypeSpec.Builder {
        functions
                .filter { it.isNative && !it.hasNoImplementation }
                .forEach { addMethod(it) }
        return this
    }

    private fun TypeSpec.Builder.addMethod(function: Function) {
        val parameterGenerators: List<SAMPNativeFunctionsParameterGenerator> = function
                .parameters
                .map { sampNativeFunctionsParameterGeneratorFactory.create(it) }

        val isWrapperMethodRequired = parameterGenerators.any { it.isWrapperMethodRequired }

        addNativeMethod(function, parameterGenerators)
        if (isWrapperMethodRequired) {
            addWrapperMethod(function, parameterGenerators)
        }
    }

    private fun TypeSpec.Builder.addNativeMethod(
            function: Function,
            parameterGenerators: List<SAMPNativeFunctionsParameterGenerator>
    ) {
        val returnType = getJavaType(function.type)
        val methodSpecBuilder = MethodSpec
                .methodBuilder(function.camelCaseName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.NATIVE)
                .returns(returnType)

        parameterGenerators.forEach {
            methodSpecBuilder.addParameter(it.generateNativeMethodParameterSpec())
        }

        if (!returnType.isPrimitive && returnType != TypeName.VOID) {
            methodSpecBuilder.addAnnotation(NotNull::class.java)
        }
        addMethod(methodSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addWrapperMethod(
            function: Function,
            parameterGenerators: List<SAMPNativeFunctionsParameterGenerator>
    ) {
        val returnType = getJavaType(function.type)
        val methodSpecBuilder = MethodSpec
                .methodBuilder(function.camelCaseName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(returnType)

        parameterGenerators.forEach {
            methodSpecBuilder.addParameter(it.generateWrapperMethodParameterSpec())
        }

        methodSpecBuilder.addMethodBody(parameterGenerators, function, returnType)

        if (!returnType.isPrimitive && returnType != TypeName.VOID) {
            methodSpecBuilder.addAnnotation(NotNull::class.java)
        }
        addMethod(methodSpecBuilder.build())
    }

    private fun MethodSpec.Builder.addMethodBody(
            parameterGenerators: List<SAMPNativeFunctionsParameterGenerator>,
            function: Function,
            returnType: TypeName
    ) {
        val parameterArguments = parameterGenerators
                .map { it.generateNativeMethodInvocationParameterCode() }
                .toTypedArray()
        val parameters = (0 until parameterArguments.size).joinToString(", ") { "\$L" }
        val statement = CodeBlock.of("${function.camelCaseName}($parameters)", *parameterArguments)
        if (returnType != TypeName.VOID) {
            addStatement("return \$L", statement)
        } else {
            addStatement(statement)
        }
    }

    private fun MethodSpec.Builder.addNativeInvocation(function: Function): MethodSpec.Builder {
        val parameters = function.parameters.joinToString(", ") {
            when {
                it.type == Types.STRING && !it.isOutParameter -> "${it.name}.getBytes(\$T.getCharset())"
                else -> it.name
            }
        }
        val arguments = function
                .parameters
                .filter { it.type == Types.STRING && !it.isOutParameter }
                .map { STRING_ENCODING_TYPE }
                .toTypedArray()
        return addStatement("return ${function.camelCaseName}($parameters)", *arguments)
    }

    private fun Writer.writeJavaFile(typeSpec: TypeSpec) {
        JavaFile
                .builder(javaPackageName, typeSpec)
                .skipJavaLangImports(true)
                .build()
                .writeTo(this)
    }

}