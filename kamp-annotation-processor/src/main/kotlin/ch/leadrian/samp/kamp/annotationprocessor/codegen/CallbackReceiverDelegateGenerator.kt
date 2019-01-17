package ch.leadrian.samp.kamp.annotationprocessor.codegen

import ch.leadrian.samp.kamp.annotationprocessor.model.CallbackListenerDefinition
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class CallbackReceiverDelegateGenerator {

    fun generate(listenerDefinition: CallbackListenerDefinition, outputDirectory: Path) {
        val className = ClassName(
                packageName = listenerDefinition.runtimePackageName,
                simpleName = listenerDefinition.type.simpleName.removeSuffix("Listener") + "ReceiverDelegate"
        )
        val fileSpec = buildFileSpec(className, listenerDefinition)
        writeFile(outputDirectory, className, fileSpec)
    }

    private fun buildFileSpec(className: ClassName, listenerDefinition: CallbackListenerDefinition): FileSpec {
        return FileSpec
                .builder(listenerDefinition.runtimePackageName, className.simpleName)
                .addCallbackReceiverDelegateTypeSpec(className, listenerDefinition)
                .build()
    }

    private fun writeFile(outputDirectory: Path, className: ClassName, fileSpec: FileSpec) {
        val packageDirectory = className.packageName.split('.').fold(outputDirectory, Path::resolve)
        Files.createDirectories(packageDirectory)
        val outputFile = packageDirectory.resolve("${className.simpleName}.kt")
        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            fileSpec.writeTo(it)
        }
    }

    private fun FileSpec.Builder.addCallbackReceiverDelegateTypeSpec(
            className: ClassName,
            listenerDefinition: CallbackListenerDefinition
    ): FileSpec.Builder {
        val listenersPropertySpec = PropertySpec
                .builder("listeners", LinkedHashSet::class.asClassName().parameterizedBy(listenerDefinition.type))
                .addModifiers(KModifier.PRIVATE)
                .mutable(false)
                .initializer("%T()", LinkedHashSet::class)
                .build()
        val receiverClassName = ClassName(
                packageName = listenerDefinition.apiPackageName,
                simpleName = listenerDefinition.type.simpleName.removeSuffix("Listener") + "Receiver"
        )
        val typeSpec = TypeSpec
                .classBuilder(className)
                .addModifiers(KModifier.INTERNAL)
                .addSuperinterface(receiverClassName)
                .addSuperinterface(listenerDefinition.type)
                .addGeneratedAnnotation(this@CallbackReceiverDelegateGenerator::class)
                .addProperty(listenersPropertySpec)
                .addAddListenerFunction(listenerDefinition, listenersPropertySpec)
                .addRemoveListenerFunction(listenerDefinition, listenersPropertySpec)
                .addCallbackFunction(listenerDefinition)
                .build()
        addType(typeSpec)
        return this
    }

    private fun TypeSpec.Builder.addAddListenerFunction(
            listenerDefinition: CallbackListenerDefinition,
            listenersPropertySpec: PropertySpec
    ): TypeSpec.Builder {
        return addFunction(
                FunSpec
                        .builder("add${listenerDefinition.type.simpleName}")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(ParameterSpec.builder("listener", listenerDefinition.type).build())
                        .addStatement("%N += listener", listenersPropertySpec)
                        .build()
        )
    }

    private fun TypeSpec.Builder.addRemoveListenerFunction(
            listenerDefinition: CallbackListenerDefinition,
            listenersPropertySpec: PropertySpec
    ): TypeSpec.Builder {
        return addFunction(
                FunSpec
                        .builder("remove${listenerDefinition.type.simpleName}")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(ParameterSpec.builder("listener", listenerDefinition.type).build())
                        .addStatement("%N -= listener", listenersPropertySpec)
                        .build()
        )
    }

    private fun TypeSpec.Builder.addCallbackFunction(listenerDefinition: CallbackListenerDefinition): TypeSpec.Builder {
        val ignoredReturnValueType = listenerDefinition.ignoredReturnValueType
        if (ignoredReturnValueType != null) {
            addCancellableCallbackFunction(listenerDefinition, ignoredReturnValueType)
        } else {
            addUnitCallbackFunction(listenerDefinition)
        }
        return this
    }

    private fun TypeSpec.Builder.addCancellableCallbackFunction(
            listenerDefinition: CallbackListenerDefinition,
            ignoredReturnValueType: TypeName
    ) {
        val funSpec = FunSpec
                .overriding(listenerDefinition.method)
                .kotlinizeParameterTypes(listenerDefinition)
                .beginControlFlow("return listeners.map")
                .addListenerFunctionCall(listenerDefinition)
                .endControlFlow()
                .beginControlFlow(".firstOrNull")
                .addStatement("it != %T", ignoredReturnValueType)
                .endControlFlow()
                .addStatement("?: %T", ignoredReturnValueType)
                .build()
        addFunction(funSpec)
    }

    private fun TypeSpec.Builder.addUnitCallbackFunction(listenerDefinition: CallbackListenerDefinition) {
        val funSpec = FunSpec
                .overriding(listenerDefinition.method)
                .kotlinizeParameterTypes(listenerDefinition)
                .beginControlFlow("listeners.forEach")
                .addListenerFunctionCall(listenerDefinition)
                .endControlFlow()
                .build()
        addFunction(funSpec)
    }

    private fun FunSpec.Builder.addListenerFunctionCall(listenerDefinition: CallbackListenerDefinition): FunSpec.Builder =
            addStatement("it.%N(${parameters.joinToString(", ") { it.name }})", listenerDefinition.method.simpleName)

    private fun FunSpec.Builder.kotlinizeParameterTypes(listenerDefinition: CallbackListenerDefinition): FunSpec.Builder {
        (0 until parameters.size).forEach { i ->
            val kotlinType = listenerDefinition.method.parameters[i].getKotlinTypeName()
            parameters[i] = parameters[i].toBuilder(type = kotlinType).build()
        }
        return this
    }

}