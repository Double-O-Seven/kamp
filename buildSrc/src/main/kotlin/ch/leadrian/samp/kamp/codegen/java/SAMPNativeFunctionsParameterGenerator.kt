package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Parameter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec

abstract class SAMPNativeFunctionsParameterGenerator(protected val parameter: Parameter) {

    abstract fun generateNativeMethodParameterSpec(): ParameterSpec

    abstract fun generateNativeMethodInvocationParameterCode(): CodeBlock

    abstract fun generateWrapperMethodParameterSpec(): ParameterSpec

    abstract val isWrapperMethodRequired: Boolean

}