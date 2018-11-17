package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.hasNoImplementation
import ch.leadrian.samp.kamp.codegen.isNative
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.io.Writer
import java.time.LocalDateTime
import javax.annotation.Generated

internal class SAMPNativeFunctionExecutorImplKtGenerator(
        private val functions: List<Function>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPNativeFunctionExecutorImpl.kt"

    override fun generate(writer: Writer) {
        FileSpec
                .builder(javaPackageName, "SAMPNativeFunctionExecutorImpl")
                .addSAMPNativeFunctionExecutorType()
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addSAMPNativeFunctionExecutorType(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .classBuilder("SAMPNativeFunctionExecutorImpl")
                .addModifiers(KModifier.PUBLIC)
                .addGeneratedAnnotation()
                .addMainThreadProperty()
                .addIsInitializedProperty()
                .addNativeFunctions()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        return addAnnotation(AnnotationSpec
                .builder(Generated::class)
                .addMember("value", "%S", this@SAMPNativeFunctionExecutorImplKtGenerator::class.java.name)
                .addMember("date", "%S", LocalDateTime.now().toString())
                .build())
    }

    private fun TypeSpec.Builder.addMainThreadProperty(): TypeSpec.Builder {
        return addProperty(PropertySpec
                .builder("mainThread", Thread::class, KModifier.PRIVATE, KModifier.LATEINIT)
                .mutable(true)
                .build())
    }

    private fun TypeSpec.Builder.addIsInitializedProperty(): TypeSpec.Builder {
        return addProperty(PropertySpec
                .builder("isInitialized", Boolean::class, KModifier.PRIVATE)
                .mutable(true)
                .initializer("%L", false)
                .build())
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
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(Synchronized::class)
                .beginControlFlow("if (isInitialized)")
                .addStatement("return")
                .endControlFlow()
                .addStatement("mainThread = Thread.currentThread()")
                .addStatement("isInitialized = true")
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addIsOnMainThreadFunction() {
        val funSpec = FunSpec
                .builder("isOnMainThread")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Boolean::class, "Thread.currentThread() == mainThread")
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.requireOnMainThread() {
        val funSpec = FunSpec
                .builder("isOnMainThread")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Boolean::class, "Thread.currentThread() == mainThread")
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addNativeFunction(function: Function) {
        val funSpec = FunSpec
                .builder(function.camelCaseName)
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .addFunctionParameters(function.parameters)
                .build()
        addFunction(funSpec)
    }

    private fun FunSpec.Builder.addFunctionParameters(parameters: List<Parameter>): FunSpec.Builder {
        parameters.forEach { addFunctionParameter(it) }
        return this
    }

    private fun FunSpec.Builder.addFunctionParameter(parameter: Parameter) {
        val parameterType = when {
            parameter.hasAttribute("out") -> getKotlinOutType(parameter.type)
            else -> getKotlinType(parameter.type)
        }
        val parameterSpec = ParameterSpec.builder(parameter.name, parameterType).build()
        addParameter(parameterSpec)
    }

}