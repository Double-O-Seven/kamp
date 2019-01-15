package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Parameter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec

abstract class SAMPCallbacksParameterGenerator(protected val parameter: Parameter) {

    abstract fun generateAbstractMethodParameterSpec(): ParameterSpec

    abstract fun generateAbstractMethodInvocationParameterCode(): CodeBlock

    abstract fun generateDefaultMethodParameterSpec(): ParameterSpec

    abstract val isDefaultMethodRequired: Boolean

}