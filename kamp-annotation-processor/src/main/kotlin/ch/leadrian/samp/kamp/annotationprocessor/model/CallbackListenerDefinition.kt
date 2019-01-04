package ch.leadrian.samp.kamp.annotationprocessor.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import javax.lang.model.element.ExecutableElement

data class CallbackListenerDefinition(
        val type: ClassName,
        val method: ExecutableElement,
        val runtimePackageName: String,
        val apiPackageName: String,
        val ignoredReturnValueType: TypeName?
)
