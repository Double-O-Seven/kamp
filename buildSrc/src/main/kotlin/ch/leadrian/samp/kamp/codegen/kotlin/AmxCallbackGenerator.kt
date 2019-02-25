package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import java.io.File
import java.io.Writer
import kotlin.reflect.KClass

internal class AmxCallbackGenerator(
        numberOfParameters: Int,
        javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    private val amxCallbackParameterResolverImplClassName = ClassName(
            "ch.leadrian.samp.kamp.core.runtime.amx",
            "AmxCallbackParameterResolverImpl"
    )

    private val baseAmxCallbackClassName = ClassName("ch.leadrian.samp.kamp.core.api.amx", "AmxCallback")

    private val amxCallbackClassName = ClassName(javaPackageName, "AmxCallback$numberOfParameters")

    override val fileName: String = "${amxCallbackClassName.simpleName}.kt"

    private val parameterTypeVariables: List<TypeVariableName> = (1..numberOfParameters).map { i ->
        TypeVariableName("T$i", Any::class)
    }

    private val constructorParameterSpecs: List<ParameterSpec> by lazy {
        parameterTypeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec
                    .builder("parameterType${i + 1}", KClass::class.asClassName().parameterizedBy(typeVariable))
                    .build()
        }
    }

    private val parameterTypePropertySpecs: List<PropertySpec> by lazy {
        constructorParameterSpecs.map { parameterSpec ->
            PropertySpec
                    .builder(parameterSpec.name, parameterSpec.type)
                    .initializer("%N", parameterSpec)
                    .build()
        }
    }

    override fun generate(writer: Writer) {
        FileSpec
                .builder(amxCallbackClassName.packageName, amxCallbackClassName.simpleName)
                .addAmxCallbackTypeSpec()
                .addImport("kotlin.reflect.full", "cast")
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addAmxCallbackTypeSpec(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .classBuilder(amxCallbackClassName)
                .superclass(baseAmxCallbackClassName)
                .addModifiers(KModifier.ABSTRACT)
        parameterTypeVariables.forEach { typeSpecBuilder.addTypeVariable(it) }
        typeSpecBuilder
                .addGeneratedAnnotation(this@AmxCallbackGenerator::class)
                .addConstructor()
                .addOnPublicCallImplementation()
                .addAbstractInvokeFunction()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addConstructor(): TypeSpec.Builder {
        val nameParameterSpec = ParameterSpec.builder("name", String::class).build()
        val funSpecBuilder = FunSpec
                .constructorBuilder()
                .addParameter(nameParameterSpec)
        addSuperclassConstructorParameter("%N", nameParameterSpec)
        addSuperclassConstructorParameter("%T", amxCallbackParameterResolverImplClassName)
        constructorParameterSpecs.forEach {
            funSpecBuilder.addParameter(it)
            addSuperclassConstructorParameter("%N", it)
        }
        addProperties(parameterTypePropertySpecs)
        return primaryConstructor(funSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addOnPublicCallImplementation(): TypeSpec.Builder {
        val parameterSpec = ParameterSpec
                .builder("parameterValues", Array<Any>::class.parameterizedBy(Any::class))
                .build()
        val onPublicCallBuilder = FunSpec
                .builder("onPublicCall")
                .addParameter(parameterSpec)
                .addModifiers(KModifier.OVERRIDE)
                .returns(Int::class)
        val format = "return invoke(" + parameterTypePropertySpecs.mapIndexed { i, _ -> i }.joinToString(", ") { "%N.cast(%N[$it])" } + ")"
        val args = mutableListOf<Any>()
        parameterTypePropertySpecs.forEach {
            args.add(it)
            args.add(parameterSpec)
        }
        onPublicCallBuilder.addStatement(format, *args.toTypedArray())
        return addFunction(onPublicCallBuilder.build())
    }

    private fun TypeSpec.Builder.addAbstractInvokeFunction(): TypeSpec.Builder {
        val invokeBuilder = FunSpec
                .builder("invoke")
                .addModifiers(KModifier.ABSTRACT, KModifier.OPERATOR)
                .returns(Int::class)
        parameterTypeVariables.forEachIndexed { index, typeVariable ->
            invokeBuilder.addParameter("parameter$index", typeVariable)
        }
        return addFunction(invokeBuilder.build())
    }
}