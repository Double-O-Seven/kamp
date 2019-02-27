package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import java.io.File
import java.io.Writer
import javax.inject.Inject

internal class AmxCallbackFactoryGenerator(
        numberOfMethods: Int,
        javaPackageName: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    private val amxCallbackFactoryClassName = ClassName(javaPackageName, "AmxCallbackFactory")

    override val fileName: String = "${amxCallbackFactoryClassName.simpleName}.kt"

    private val amxCallbackExecutorClass = ClassName("ch.leadrian.samp.kamp.core.runtime.amx", "AmxCallbackExecutor")

    private val amxCallbackExecutorParameterSpec: ParameterSpec =
            ParameterSpec.builder("amxCallbackExecutor", amxCallbackExecutorClass).build()

    private val amxCallbackExecutorPropertySpec: PropertySpec =
            PropertySpec
                    .builder(amxCallbackExecutorParameterSpec.name, amxCallbackExecutorParameterSpec.type)
                    .initializer("%N", amxCallbackExecutorParameterSpec)
                    .addModifiers(KModifier.PRIVATE)
                    .build()

    private val registerFunSpec: FunSpec =
            FunSpec
                    .builder("register")
                    .addParameter("amxCallback", ClassName("ch.leadrian.samp.kamp.core.api.amx", "AmxCallback"))
                    .addStatement("%N.register(amxCallback)", amxCallbackExecutorPropertySpec)
                    .build()

    private val parameterTypeVariables: List<TypeVariableName> = (1..numberOfMethods).map { i ->
        TypeVariableName("T$i", Any::class)
    }

    override fun generate(writer: Writer) {
        FileSpec
                .builder(amxCallbackFactoryClassName.packageName, amxCallbackFactoryClassName.simpleName)
                .addAmxCallbackFactoryTypeSpec()
                .build()
                .writeTo(writer)
    }

    private fun FileSpec.Builder.addAmxCallbackFactoryTypeSpec(): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
                .classBuilder(amxCallbackFactoryClassName)
                .addConstructor()
                .addProperty(amxCallbackExecutorPropertySpec)
                .addFunction(registerFunSpec)
                .addCreateFunctions()
        return addType(typeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addConstructor(): TypeSpec.Builder {
        val constructorBuilder = FunSpec
                .constructorBuilder()
                .addModifiers(KModifier.INTERNAL)
                .addAnnotation(Inject::class)
                .addParameter(amxCallbackExecutorParameterSpec)
        return primaryConstructor(constructorBuilder.build())
    }

    private fun TypeSpec.Builder.addCreateFunctions(): TypeSpec.Builder {
        (0..parameterTypeVariables.size).forEach { numberOfParameters ->
            addCreateFunction(numberOfParameters)
        }
        return this
    }

    private fun TypeSpec.Builder.addCreateFunction(numberOfParameters: Int) {
        val nameParameterSpec = ParameterSpec.builder("name", String::class).build()
        val relevantTypeVariables = parameterTypeVariables.subList(0, numberOfParameters)
        val parameters = relevantTypeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec.builder("parameter${i + 1}", typeVariable).build()
        }
        val actionTypeName = LambdaTypeName.get(parameters = parameters, returnType = Int::class.asClassName())
        val actionParameterSpec = ParameterSpec
                .builder("action", actionTypeName)
                .addModifiers(KModifier.CROSSINLINE)
                .build()
        val amxCallbackClass = ClassName("ch.leadrian.samp.kamp.core.api.amx", "AmxCallback$numberOfParameters")
        val parameterizedAmxCallbackClass = when {
            relevantTypeVariables.isNotEmpty() -> amxCallbackClass.parameterizedBy(*relevantTypeVariables.toTypedArray())
            else -> amxCallbackClass
        }
        val anonymousAmxCallbackImplementation = buildAnonymousAmxCallbackImplementation(
                parameterizedAmxCallbackClass,
                nameParameterSpec,
                actionParameterSpec,
                relevantTypeVariables
        )
        val createFunSpec = FunSpec
                .builder("create$numberOfParameters")
                .addTypeVariables(relevantTypeVariables.map { it.copy(reified = true) })
                .addModifiers(KModifier.INLINE)
                .addParameter(nameParameterSpec)
                .addParameter(actionParameterSpec)
                .returns(parameterizedAmxCallbackClass)
                .addStatement("val amxCallback = %L", anonymousAmxCallbackImplementation)
                .addStatement("register(amxCallback)")
                .addStatement("return amxCallback")
                .build()
        addFunction(createFunSpec)
    }

    private fun buildAnonymousAmxCallbackImplementation(
            amxCallbackClass: TypeName,
            nameParameterSpec: ParameterSpec,
            actionParameterSpec: ParameterSpec,
            relevantTypeVariables: List<TypeVariableName>
    ): TypeSpec {
        val parameters = relevantTypeVariables.mapIndexed { i, typeVariable ->
            ParameterSpec.builder("parameter${i + 1}", typeVariable).build()
        }
        val args = mutableListOf<Any>(actionParameterSpec)
        args.addAll(parameters)
        val format = "return %N(" + parameters.joinToString(", ") { "%N" } + ")"
        val typeSpecBuilder = TypeSpec
                .anonymousClassBuilder()
                .superclass(amxCallbackClass)
                .addSuperclassConstructorParameter("%N", nameParameterSpec)
        relevantTypeVariables.forEach {
            typeSpecBuilder.addSuperclassConstructorParameter("%T::class", it)
        }
        return typeSpecBuilder
                .addFunction(
                        FunSpec
                                .builder("invoke")
                                .addModifiers(KModifier.OVERRIDE)
                                .returns(Int::class)
                                .addParameters(parameters)
                                .addStatement(format, *args.toTypedArray())
                                .build()
                )
                .build()
    }

}
