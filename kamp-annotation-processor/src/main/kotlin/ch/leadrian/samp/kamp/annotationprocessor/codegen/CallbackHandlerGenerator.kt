package ch.leadrian.samp.kamp.annotationprocessor.codegen

import ch.leadrian.samp.kamp.annotationprocessor.model.CallbackListenerDefinition
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import javax.inject.Inject
import javax.inject.Singleton

class CallbackHandlerGenerator {

    fun generate(listenerDefinition: CallbackListenerDefinition, outputDirectory: Path) {
        val className = ClassName(
                packageName = listenerDefinition.runtimePackageName,
                simpleName = listenerDefinition.type.simpleName.removeSuffix("Listener") + "Handler"
        )
        val fileSpec = buildFileSpec(className, listenerDefinition)
        writeFile(outputDirectory, className, fileSpec)
    }

    private fun buildFileSpec(className: ClassName, listenerDefinition: CallbackListenerDefinition): FileSpec {
        return FileSpec
                .builder(listenerDefinition.runtimePackageName, className.simpleName)
                .addCallbackHandlerClass(className, listenerDefinition)
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

    private fun FileSpec.Builder.addCallbackHandlerClass(className: ClassName, listenerDefinition: CallbackListenerDefinition): FileSpec.Builder {
        val typeSpec = TypeSpec
                .classBuilder(className)
                .addModifiers(KModifier.INTERNAL)
                .addAnnotation(Singleton::class)
                .primaryConstructor(FunSpec
                        .constructorBuilder()
                        .addAnnotation(Inject::class)
                        .build())
                .superclass(ClassName(
                        "ch.leadrian.samp.kamp.core.api.callback",
                        "CallbackListenerRegistry"
                ).parameterizedBy(listenerDefinition.type))
                .addSuperclassConstructorParameter("%T::class", listenerDefinition.type)
                .addSuperinterface(listenerDefinition.type)
                .addCallbackFunction(listenerDefinition)
                .build()
        return addType(typeSpec)
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

    private fun TypeSpec.Builder.addCancellableCallbackFunction(listenerDefinition: CallbackListenerDefinition, ignoredReturnValueType: TypeName) {
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