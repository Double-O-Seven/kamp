package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import java.io.File
import java.io.Writer

internal class AmxNativeFunctionGenerator(
        numberOfParameters: Int,
        private val javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    private val amxNativeFunctionInvokerImplClassName = ClassName(
            "ch.leadrian.samp.kamp.core.runtime.amx",
            "AmxNativeFunctionInvokerImpl"
    )

    private val amxNativeFunctionParameterType = ClassName(
            "ch.leadrian.samp.kamp.core.api.amx",
            "AmxNativeFunctionParameterType"
    )

    private val className = "AmxNativeFunction$numberOfParameters"

    override val fileName: String = "$className.kt"

    private val typeVariables: List<TypeVariableName> = (1..numberOfParameters).map { i ->
        TypeVariableName("T$i", Any::class)
    }

    override fun generate(writer: Writer) {
        FileSpec
                .builder(javaPackageName, className)
                .addAmxNativeFunctionTypeSpec()
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addAmxNativeFunctionTypeSpec(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .classBuilder(className)
                .superclass(ClassName("ch.leadrian.samp.kamp.core.api.amx", "AmxNativeFunction"))
        typeVariables.forEach { typeSpecBuilder.addTypeVariable(it) }
        typeSpecBuilder
                .addConstructor()
                .addInvokeFunction()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addConstructor(): TypeSpec.Builder {
        val nameParameterSpec = ParameterSpec.builder("name", String::class).build()
        val funSpecBuilder = FunSpec
                .constructorBuilder()
                .addParameter(nameParameterSpec)
        addSuperclassConstructorParameter("%N", nameParameterSpec)
        addSuperclassConstructorParameter("%T", amxNativeFunctionInvokerImplClassName)
        val typeParameterSpecs = typeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec
                    .builder("parameterType${i + 1}", amxNativeFunctionParameterType.parameterizedBy(typeVariable))
                    .build()
        }
        typeParameterSpecs.forEach {
            funSpecBuilder.addParameter(it)
            addSuperclassConstructorParameter("%N", it)
        }
        return primaryConstructor(funSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addInvokeFunction(): TypeSpec.Builder {
        val funSpecBuilder = FunSpec
                .builder("invoke")
                .addModifiers(KModifier.OPERATOR)
        val typeParameterSpecs = typeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec
                    .builder("param${i + 1}", typeVariable)
                    .build()
        }
        typeParameterSpecs.forEach {
            funSpecBuilder.addParameter(it)
        }
        val format = "return invokeInternal(" + typeParameterSpecs.joinToString(", ") { "%N" } + ")"
        funSpecBuilder
                .addStatement(format, *typeParameterSpecs.toTypedArray())
                .returns(Int::class)
        return addFunction(funSpecBuilder.build())
    }

}