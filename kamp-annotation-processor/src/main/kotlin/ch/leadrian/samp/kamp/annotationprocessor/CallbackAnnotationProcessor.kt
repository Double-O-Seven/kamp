package ch.leadrian.samp.kamp.annotationprocessor

import ch.leadrian.samp.kamp.annotationprocessor.codegen.CallbackHandlerGenerator
import ch.leadrian.samp.kamp.annotationprocessor.codegen.CallbackReceiverDelegateGenerator
import ch.leadrian.samp.kamp.annotationprocessor.codegen.CallbackReceiverGenerator
import ch.leadrian.samp.kamp.annotationprocessor.model.CallbackListenerDefinition
import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.annotations.InlineCallback
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import java.nio.file.Paths
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.NoType
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic
import kotlin.reflect.full.safeCast

@Suppress("unused")
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(CallbackAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class CallbackAnnotationProcessor : AbstractProcessor() {

    private val callbackHandlerGenerator = CallbackHandlerGenerator()
    private val callbackReceiverGenerator = CallbackReceiverGenerator()
    private val callbackReceiverDelegateGenerator = CallbackReceiverDelegateGenerator()

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private val kaptOutputDirectory by lazy { Paths.get(processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]) }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return mutableSetOf(CallbackListener::class.java.name)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        annotations.forEach { annotation ->
            try {
                roundEnv.getElementsAnnotatedWith(annotation).forEach(this::process)
            } catch (e: CallbackAnnotationProcessorException) {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, e.message)
            }
        }
        return true
    }

    private fun process(element: Element) {
        if (element.kind != ElementKind.INTERFACE) {
            throw CallbackAnnotationProcessorException("Only interfaces may be annotated with ${CallbackListener::class.java.name}")
        }
        val typeElement = element as TypeElement
        val callbackMethodElement: ExecutableElement = getCallbackMethodElement(element)

        val ignoredReturnValueType = when {
            callbackMethodElement.returnType !is NoType -> getIgnoredReturnValue(callbackMethodElement).asTypeName()
            else -> null
        }
        val className = typeElement.asClassName()
        val callbackListenerAnnotation = element.getAnnotation(CallbackListener::class.java)
        val apiPackageName = callbackListenerAnnotation.apiPackageName.takeIf { it.isNotBlank() } ?: className.packageName
        val runtimePackageName = callbackListenerAnnotation.runtimePackageName.takeIf { it.isNotBlank() } ?: className.packageName
        val listenerDefinition = CallbackListenerDefinition(
                type = className,
                method = callbackMethodElement,
                apiPackageName = apiPackageName,
                runtimePackageName = runtimePackageName,
                ignoredReturnValueType = ignoredReturnValueType
        )
        callbackHandlerGenerator.generate(listenerDefinition, kaptOutputDirectory)
        if (callbackMethodElement.getAnnotation(InlineCallback::class.java) != null) {
            callbackReceiverGenerator.generate(listenerDefinition, kaptOutputDirectory)
            callbackReceiverDelegateGenerator.generate(listenerDefinition, kaptOutputDirectory)
        }
    }

    private fun getIgnoredReturnValue(callbackMethodElement: ExecutableElement): TypeMirror =
            callbackMethodElement.getAnnotation(IgnoredReturnValue::class.java)?.valueTypeMirror
                    ?: throw CallbackAnnotationProcessorException("Could not find ${IgnoredReturnValue::class} annotation on $callbackMethodElement")

    private val IgnoredReturnValue.valueTypeMirror: TypeMirror?
        get() {
            return try {
                value
                null
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        }

    private fun getCallbackMethodElement(element: Element): ExecutableElement {
        val interfaceMethodElements = element
                .enclosedElements
                .asSequence()
                .mapNotNull { ExecutableElement::class.safeCast(it) }
                .filter { !it.isDefault }
                .toList()
        if (interfaceMethodElements.size != 1) {
            throw CallbackAnnotationProcessorException("Exactly one interface method must be non-default")
        }
        return interfaceMethodElements.first()
    }

}