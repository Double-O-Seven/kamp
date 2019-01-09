package ch.leadrian.samp.kamp.annotationprocessor.codegen

import ch.leadrian.samp.kamp.annotationprocessor.CallbackAnnotationProcessorException
import ch.leadrian.samp.kamp.annotationprocessor.model.CallbackListenerDefinition
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import javax.lang.model.element.VariableElement

class CallbackReceiverGenerator {

    fun generate(listenerDefinition: CallbackListenerDefinition, outputDirectory: Path) {
        val className = ClassName(
                packageName = listenerDefinition.apiPackageName,
                simpleName = listenerDefinition.type.simpleName.removeSuffix("Listener") + "Receiver"
        )
        val fileSpec = buildFileSpec(className, listenerDefinition)
        writeFile(outputDirectory, className, fileSpec)
    }

    private fun buildFileSpec(className: ClassName, listenerDefinition: CallbackListenerDefinition): FileSpec {
        return FileSpec
                .builder(className.packageName, className.simpleName)
                .addCallbackReceiverClass(className, listenerDefinition)
                .addInlineCallbackFunction(listenerDefinition, className)
                .build()
    }

    private fun FileSpec.Builder.addCallbackReceiverClass(
            className: ClassName,
            listenerDefinition: CallbackListenerDefinition
    ): FileSpec.Builder {
        val typeSpec = TypeSpec
                .interfaceBuilder(className)
                .addAddListenerFunction(listenerDefinition)
                .addRemoveListenerFunction(listenerDefinition)
                .build()
        return addType(typeSpec)
    }

    private fun TypeSpec.Builder.addAddListenerFunction(listenerDefinition: CallbackListenerDefinition): TypeSpec.Builder {
        return addFunction(
                FunSpec
                        .builder("add${listenerDefinition.type.simpleName}")
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter(ParameterSpec.builder("listener", listenerDefinition.type).build())
                        .build()
        )
    }

    private fun TypeSpec.Builder.addRemoveListenerFunction(listenerDefinition: CallbackListenerDefinition): TypeSpec.Builder {
        return addFunction(
                FunSpec
                        .builder("remove${listenerDefinition.type.simpleName}")
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter(ParameterSpec.builder("listener", listenerDefinition.type).build())
                        .build()
        )
    }

    private fun FileSpec.Builder.addInlineCallbackFunction(
            listenerDefinition: CallbackListenerDefinition,
            className: ClassName
    ): FileSpec.Builder {
        val actionReceiver = listenerDefinition.method.parameters.firstOrNull { it.isReceiver }
                ?: throw CallbackAnnotationProcessorException("Could not find ${Receiver::class.java} annotation on ${listenerDefinition.type}")
        val actionParameters = listenerDefinition.method.parameters.filter { !it.isReceiver }
        val actionParameterTypes = actionParameters.map { it.getKotlinTypeName() }.toTypedArray()
        val parameterSpec = buildActionParameterSpec(actionReceiver, actionParameterTypes, listenerDefinition)
        val listenerTypeSpec = buildInlineListenerTypeSpec(listenerDefinition, actionReceiver, actionParameters)
        val functionName = listenerDefinition.method.getAnnotation(InlineCallback::class.java).name
        val funSpec = FunSpec
                .builder(functionName)
                .addModifiers(KModifier.INLINE)
                .receiver(className)
                .addParameter(parameterSpec)
                .returns(listenerDefinition.type)
                .addStatement("val listener = %L", listenerTypeSpec)
                .addStatement("add${listenerDefinition.type.simpleName}(listener)")
                .addStatement("return listener")
                .build()
        return addFunction(funSpec)
    }

    private fun buildActionParameterSpec(
            actionReceiver: VariableElement,
            actionParameterTypes: Array<TypeName>,
            listenerDefinition: CallbackListenerDefinition
    ): ParameterSpec {
        return ParameterSpec
                .builder(
                        "action", LambdaTypeName.get(
                        receiver = actionReceiver.asType().toKotlinTypeName(),
                        parameters = *actionParameterTypes,
                        returnType = listenerDefinition.method.returnType.asTypeName()
                )
                )
                .addModifiers(KModifier.CROSSINLINE)
                .build()
    }

    private fun buildInlineListenerTypeSpec(
            listenerDefinition: CallbackListenerDefinition,
            actionReceiver: VariableElement,
            actionParameters: List<VariableElement>
    ): TypeSpec {
        return TypeSpec
                .anonymousClassBuilder()
                .addSuperinterface(listenerDefinition.type)
                .addInlineCallbackFunction(listenerDefinition, actionReceiver, actionParameters)
                .build()
    }

    private fun TypeSpec.Builder.addInlineCallbackFunction(
            listenerDefinition: CallbackListenerDefinition,
            actionReceiver: VariableElement,
            actionParameters: List<VariableElement>
    ): TypeSpec.Builder {
        val hasReturnValue = listenerDefinition.ignoredReturnValueType != null
        val funSpec = FunSpec
                .overriding(listenerDefinition.method)
                .kotlinizeParameterTypes(listenerDefinition)
                .addActionInvocation(actionReceiver, actionParameters, hasReturnValue)
                .build()
        return addFunction(funSpec)
    }

    private fun FunSpec.Builder.addActionInvocation(
            actionReceiver: VariableElement,
            actionParameters: List<VariableElement>,
            hasReturnValue: Boolean
    ): FunSpec.Builder {
        val invocationParameters = actionParameters.toMutableList().apply {
            add(0, actionReceiver)
        }.map { it.simpleName.toString() }
        val actionInvocation = "action.invoke(${invocationParameters.joinToString(", ")})"
        return if (hasReturnValue) {
            addStatement("return $actionInvocation")
        } else {
            addStatement(actionInvocation)
        }
    }

    private fun FunSpec.Builder.kotlinizeParameterTypes(listenerDefinition: CallbackListenerDefinition): FunSpec.Builder {
        (0 until parameters.size).forEach { i ->
            val kotlinType = listenerDefinition.method.parameters[i].getKotlinTypeName()
            parameters[i] = parameters[i].toBuilder(type = kotlinType).build()
        }
        return this
    }

    private val VariableElement.isReceiver: Boolean
        get() = getAnnotation(Receiver::class.java) != null

    private fun writeFile(outputDirectory: Path, className: ClassName, fileSpec: FileSpec) {
        val packageDirectory = className.packageName.split('.').fold(outputDirectory, Path::resolve)
        Files.createDirectories(packageDirectory)
        val outputFile = packageDirectory.resolve("${className.simpleName}.kt")
        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            fileSpec.writeTo(it)
        }
    }
}
