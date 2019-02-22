package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.Parameter
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.hasNoImplementation
import ch.leadrian.samp.kamp.codegen.isNative
import ch.leadrian.samp.kamp.codegen.isOutParameter
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.io.Writer

internal class SAMPNativeFunctionExecutorKtGenerator(
        private val functions: List<Function>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPNativeFunctionExecutor.kt"

    override fun generate(writer: Writer) {
        FileSpec
                .builder(javaPackageName, "SAMPNativeFunctionExecutor")
                .addSAMPNativeFunctionExecutorType()
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addSAMPNativeFunctionExecutorType(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .interfaceBuilder("SAMPNativeFunctionExecutor")
                .addModifiers(KModifier.PUBLIC)
                .addGeneratedAnnotation(this@SAMPNativeFunctionExecutorKtGenerator::class)
                .addNativeFunctions()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addNativeFunctions(): TypeSpec.Builder {
        addInitializeFunction()
        addIsOnMainThreadFunction()
        functions
                .filter { it.isNative && !it.hasNoImplementation }
                .forEach { addNativeFunction(it) }
        return this
    }

    private fun TypeSpec.Builder.addInitializeFunction() {
        val funSpec = FunSpec
                .builder("initialize")
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addIsOnMainThreadFunction() {
        val funSpec = FunSpec
                .builder("isOnMainThread")
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .returns(Boolean::class)
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addNativeFunction(function: Function) {
        val funSpec = FunSpec
                .builder(function.camelCaseName)
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .addFunctionParameters(function.parameters)
                .returns(getKotlinType(function.type))
                .build()
        addFunction(funSpec)
    }

    private fun FunSpec.Builder.addFunctionParameters(parameters: List<Parameter>): FunSpec.Builder {
        parameters.forEach { addFunctionParameter(it) }
        return this
    }

    private fun FunSpec.Builder.addFunctionParameter(parameter: Parameter) {
        val parameterType = when {
            parameter.isOutParameter -> getKotlinOutType(parameter.type)
            else -> getKotlinType(parameter.type)
        }
        val parameterSpec = ParameterSpec.builder(parameter.name, parameterType).build()
        addParameter(parameterSpec)
    }

}