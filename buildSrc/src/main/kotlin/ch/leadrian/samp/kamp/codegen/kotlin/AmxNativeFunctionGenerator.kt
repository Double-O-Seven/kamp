package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import java.io.File
import java.io.Writer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

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

    private val baseAmxNativeFunctionClassName = ClassName("ch.leadrian.samp.kamp.core.api.amx", "AmxNativeFunction")

    private val amxNativeFunctionClassName = ClassName(javaPackageName, "AmxNativeFunction$numberOfParameters")

    override val fileName: String = "${amxNativeFunctionClassName.simpleName}.kt"

    private val parameterTypeVariables: List<TypeVariableName> = (1..numberOfParameters).map { i ->
        TypeVariableName("T$i", Any::class)
    }

    private val parameterizedAmxNativeFunctionTypeName: TypeName =
            when {
                parameterTypeVariables.isNotEmpty() -> amxNativeFunctionClassName.parameterizedBy(*parameterTypeVariables.toTypedArray())
                else -> amxNativeFunctionClassName
            }

    private val nullableAnyClassName = Any::class.asClassName().copy(nullable = true)

    override fun generate(writer: Writer) {
        FileSpec
                .builder(amxNativeFunctionClassName.packageName, amxNativeFunctionClassName.simpleName)
                .addAmxNativeFunctionTypeSpec()
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addAmxNativeFunctionTypeSpec(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .classBuilder(amxNativeFunctionClassName)
                .superclass(baseAmxNativeFunctionClassName)
        parameterTypeVariables.forEach { typeSpecBuilder.addTypeVariable(it) }
        typeSpecBuilder
                .addConstructor()
                .addInvokeFunction()
                .addFactoryType()
                .addCompanionObject()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addConstructor(): TypeSpec.Builder {
        val nameParameterSpec = ParameterSpec.builder("name", String::class).build()
        val funSpecBuilder = FunSpec
                .constructorBuilder()
                .addParameter(nameParameterSpec)
        addSuperclassConstructorParameter("%N", nameParameterSpec)
        addSuperclassConstructorParameter("%T", amxNativeFunctionInvokerImplClassName)
        val typeParameterSpecs = parameterTypeVariables.mapIndexed { i, typeVariable ->
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
        val typeParameterSpecs = parameterTypeVariables.mapIndexed { i, typeVariable ->
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

    private fun TypeSpec.Builder.addFactoryType(): TypeSpec.Builder {
        val createdInstancesProperty = getCreatedInstancesProperty()
        val factoryTypeSpecBuilder = TypeSpec
                .classBuilder("Factory")
                .addProperty(createdInstancesProperty)
        parameterTypeVariables.forEach { factoryTypeSpecBuilder.addTypeVariable(it) }
        factoryTypeSpecBuilder
                .addReadOnlyPropertyInterface()
                .addFactoryConstructor()
        val parameterTypeProperties = getParameterTypeProperties()
        parameterTypeProperties.forEach { factoryTypeSpecBuilder.addProperty(it) }
        factoryTypeSpecBuilder.addGetValueFunction(parameterTypeProperties, createdInstancesProperty)
        return addType(factoryTypeSpecBuilder.build())
    }

    private fun getCreatedInstancesProperty(): PropertySpec {
        val hashMapType = HashMap::class
                .asClassName()
                .parameterizedBy(String::class.asClassName(), parameterizedAmxNativeFunctionTypeName)
        return PropertySpec
                .builder("createdInstances", hashMapType)
                .addModifiers(KModifier.PRIVATE)
                .initializer(CodeBlock.of("%T()", HashMap::class))
                .build()
    }

    private fun getParameterTypeProperties(): List<PropertySpec> {
        return parameterTypeVariables.mapIndexed { i, typeVariable ->
            val name = "parameterType${i + 1}"
            PropertySpec
                    .builder(name, amxNativeFunctionParameterType.parameterizedBy(typeVariable))
                    .mutable(false)
                    .initializer(name)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
        }
    }

    private fun TypeSpec.Builder.addReadOnlyPropertyInterface(): TypeSpec.Builder {
        val readOnlyPropertyInterface = ReadOnlyProperty::class
                .asClassName()
                .parameterizedBy(nullableAnyClassName, parameterizedAmxNativeFunctionTypeName)
        return addSuperinterface(readOnlyPropertyInterface)
    }

    private fun TypeSpec.Builder.addFactoryConstructor(): TypeSpec.Builder {
        val funSpecBuilder = FunSpec.constructorBuilder()
        parameterTypeVariables.forEachIndexed { i, typeVariable ->
            funSpecBuilder.addParameter(
                    "parameterType${i + 1}",
                    amxNativeFunctionParameterType.parameterizedBy(typeVariable)
            )
        }
        return primaryConstructor(funSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addGetValueFunction(
            parameterTypeProperties: List<PropertySpec>,
            createdInstancesProperty: PropertySpec
    ): TypeSpec.Builder {
        val thisRefParameter = ParameterSpec
                .builder("thisRef", nullableAnyClassName)
                .build()
        val propertyParameter = ParameterSpec
                .builder("property", KProperty::class.asClassName().parameterizedBy(STAR))
                .build()
        val arguments = mutableListOf<Any>(amxNativeFunctionClassName)
        arguments.addAll(parameterTypeProperties)
        val format = "%T(property.name" + ", %N".repeat(parameterTypeVariables.size) + ")"
        addFunction(
                FunSpec
                        .builder("getValue")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(thisRefParameter)
                        .addParameter(propertyParameter)
                        .returns(parameterizedAmxNativeFunctionTypeName)
                        .beginControlFlow("return %N.computeIfAbsent(property.name)", createdInstancesProperty)
                        .addStatement(format, *arguments.toTypedArray())
                        .endControlFlow()
                        .build()
        )
        return this
    }

    private fun TypeSpec.Builder.addCompanionObject(): TypeSpec.Builder {
        val companionObjectBuilder = TypeSpec.companionObjectBuilder()
        companionObjectBuilder.addNonInlineInvokeFunction()
        if (parameterTypeVariables.isNotEmpty()) {
            companionObjectBuilder.addInlineInvokeFunction()
        }
        return addType(companionObjectBuilder.build())
    }

    private fun TypeSpec.Builder.addNonInlineInvokeFunction(): TypeSpec.Builder {
        val nonInlineInvoke = FunSpec
                .builder("invoke")
                .addModifiers(KModifier.OPERATOR)
                .addTypeVariables(parameterTypeVariables)
        val parameters = parameterTypeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec
                    .builder("parameterType${i + 1}", KClass::class.asClassName().parameterizedBy(typeVariable))
                    .build()
        }
        nonInlineInvoke.addParameters(parameters)
        val factoryClassName = amxNativeFunctionClassName.nestedClass("Factory")
        val arguments = mutableListOf<Any>(factoryClassName)
        parameters.forEach {
            arguments.add(amxNativeFunctionParameterType)
            arguments.add(it)
        }
        val format = "return %T(" + parameters.joinToString(", ") { "%T[%N]" } + ")"
        val returnType = when {
            parameterTypeVariables.isEmpty() -> factoryClassName
            else -> factoryClassName.parameterizedBy(*parameterTypeVariables.toTypedArray())
        }
        nonInlineInvoke
                .returns(returnType)
                .addStatement(format, *arguments.toTypedArray())
        return addFunction(nonInlineInvoke.build())
    }

    private fun TypeSpec.Builder.addInlineInvokeFunction(): TypeSpec.Builder {
        val nonInlineInvoke = FunSpec
                .builder("invoke")
                .addModifiers(KModifier.OPERATOR, KModifier.INLINE)
                .addTypeVariables(parameterTypeVariables.map { it.copy(reified = true) })
        val factoryClassName = amxNativeFunctionClassName.nestedClass("Factory")
        val format = "return invoke(" + parameterTypeVariables.joinToString(", ") { "%T::class" } + ")"
        val returnType = when {
            parameterTypeVariables.isEmpty() -> factoryClassName
            else -> factoryClassName.parameterizedBy(*parameterTypeVariables.toTypedArray())
        }
        nonInlineInvoke
                .returns(returnType)
                .addStatement(format, *parameterTypeVariables.toTypedArray())
        return addFunction(nonInlineInvoke.build())
    }

}