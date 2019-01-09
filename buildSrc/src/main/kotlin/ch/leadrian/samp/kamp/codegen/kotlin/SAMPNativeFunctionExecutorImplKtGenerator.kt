package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import ch.leadrian.samp.kamp.codegen.hasNoImplementation
import ch.leadrian.samp.kamp.codegen.isNative
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import java.io.File
import java.io.Writer
import java.time.LocalDateTime
import javax.annotation.Generated

internal class SAMPNativeFunctionExecutorImplKtGenerator(
        private val functions: List<Function>,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    private val mainThreadPropertySpec: PropertySpec by lazy {
        PropertySpec
                .builder("mainThread", Thread::class, KModifier.PRIVATE, KModifier.LATEINIT)
                .mutable(true)
                .build()
    }

    private val isInitializedPropertySpec: PropertySpec by lazy {
        PropertySpec
                .builder("isInitialized", Boolean::class, KModifier.PRIVATE)
                .mutable(true)
                .initializer("%L", false)
                .build()
    }

    private val isOnMainThreadFunSpec: FunSpec by lazy {
        FunSpec
                .builder("isOnMainThread")
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("return Thread.currentThread() == %N", mainThreadPropertySpec)
                .returns(Boolean::class)
                .build()
    }

    private val requireOnMainThreadFunSpec: FunSpec by lazy {
        val typeVariable = TypeVariableName("T")
        val blockParameter = ParameterSpec.builder("block", LambdaTypeName.get(returnType = typeVariable)).build()
        FunSpec
                .builder("requireOnMainThread")
                .addModifiers(KModifier.PRIVATE, KModifier.INLINE)
                .addTypeVariable(typeVariable)
                .addParameter(blockParameter)
                .beginControlFlow("if (!%N())", isOnMainThreadFunSpec)
                .addStatement("throw IllegalStateException(\"Can only execute native functions on main thread\")")
                .endControlFlow()
                .addStatement("return %N()", blockParameter)
                .returns(typeVariable)
                .build()
    }

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
                .addSuperinterface(ClassName(javaPackageName, "SAMPNativeFunctionExecutor"))
                .addModifiers(KModifier.PUBLIC)
                .addGeneratedAnnotation()
                .addProperty(mainThreadPropertySpec)
                .addProperty(isInitializedPropertySpec)
                .addNativeFunctions()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        return addAnnotation(AnnotationSpec
                .builder(Generated::class)
                .addMember("value = [%S]", this@SAMPNativeFunctionExecutorImplKtGenerator::class.java.name)
                .addMember("date = %S", LocalDateTime.now().toString())
                .build())
    }

    private fun TypeSpec.Builder.addNativeFunctions(): TypeSpec.Builder {
        addInitializeFunction()
        addFunction(isOnMainThreadFunSpec)
        addFunction(requireOnMainThreadFunSpec)
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
                .beginControlFlow("if (%N)", isInitializedPropertySpec)
                .addStatement("return")
                .endControlFlow()
                .addStatement("%N = Thread.currentThread()", mainThreadPropertySpec)
                .addStatement("%N = true", isInitializedPropertySpec)
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addNativeFunction(function: Function) {
        val parameters = function.parameters.joinToString(", ") { it.name }
        val funSpec = FunSpec
                .builder(function.camelCaseName)
                .addModifiers(KModifier.OVERRIDE)
                .addFunctionParameters(function.parameters)
                .returns(getKotlinType(function.type))
                .beginControlFlow("%N", requireOnMainThreadFunSpec)
                .addStatement("return SAMPNativeFunctions.${function.camelCaseName}($parameters)")
                .endControlFlow()
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
