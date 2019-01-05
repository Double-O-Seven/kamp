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
        val className = listenerDefinition.type.simpleName.removeSuffix("Listener") + "Handler"
        val fileSpec = buildFileSpec(className, listenerDefinition)
        writeFile(listenerDefinition, outputDirectory, className, fileSpec)
    }

    private fun buildFileSpec(className: String, listenerDefinition: CallbackListenerDefinition): FileSpec {
        return FileSpec
                .builder(listenerDefinition.runtimePackageName, className)
                .addCallbackHandlerClass(className, listenerDefinition)
                .build()
    }

    private fun writeFile(listenerDefinition: CallbackListenerDefinition, outputDirectory: Path, className: String, fileSpec: FileSpec) {
        val packageDirectory = listenerDefinition.runtimePackageName.split('.').fold(outputDirectory, Path::resolve)
        Files.createDirectories(packageDirectory)
        val outputFile = packageDirectory.resolve("$className.kt")
        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            fileSpec.writeTo(it)
        }
    }

    private fun FileSpec.Builder.addCallbackHandlerClass(className: String, listenerDefinition: CallbackListenerDefinition): FileSpec.Builder {
        val typeSpecBuilder = TypeSpec
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
        typeSpecBuilder.addCallbackFunction(listenerDefinition)
        addType(typeSpecBuilder.build())
        return this
    }

    private fun TypeSpec.Builder.addCallbackFunction(listenerDefinition: CallbackListenerDefinition) {
        val ignoredReturnValueTypeMirror = listenerDefinition.ignoredReturnValueType
        if (ignoredReturnValueTypeMirror != null) {
            addCancellableCallbackFunction(listenerDefinition, ignoredReturnValueTypeMirror)
        } else {
            addUnitCallbackFunction(listenerDefinition)
        }
    }

    private fun TypeSpec.Builder.addCancellableCallbackFunction(listenerDefinition: CallbackListenerDefinition, ignoredReturnValueTypeMirror: TypeName) {
        val funSpecBuilder = FunSpec
                .overriding(listenerDefinition.method)
                .fixParameterTypes(listenerDefinition)
                .beginControlFlow("return listeners.map")
        addFunction(funSpecBuilder
                .addStatement("it.%N(${funSpecBuilder.parameters.joinToString(", ") { it.name }})", listenerDefinition.method.simpleName)
                .endControlFlow()
                .beginControlFlow(".firstOrNull")
                .addStatement("it != %T", ignoredReturnValueTypeMirror)
                .endControlFlow()
                .addStatement("?: %T", ignoredReturnValueTypeMirror)
                .build())
    }

    private fun FunSpec.Builder.fixParameterTypes(listenerDefinition: CallbackListenerDefinition): FunSpec.Builder {
        (0 until parameters.size).forEach { i ->
            val parameterSpec = parameters[i]
            val parameterElement = listenerDefinition.method.parameters[i]
            parameters[i] = parameterSpec.fixType(parameterElement)
        }
        return this
    }

    private fun TypeSpec.Builder.addUnitCallbackFunction(listenerDefinition: CallbackListenerDefinition) {
        val funSpecBuilder = FunSpec
                .overriding(listenerDefinition.method)
                .fixParameterTypes(listenerDefinition)
                .beginControlFlow("listeners.forEach")
        addFunction(funSpecBuilder
                .addStatement("it.%N(${funSpecBuilder.parameters.joinToString(", ") { it.name }})", listenerDefinition.method.simpleName)
                .endControlFlow()
                .build())
    }

}