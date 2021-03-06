package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Parameter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName

internal class SAMPNativeFunctionsPrimitiveParameterGenerator(
        parameter: Parameter,
        type: TypeName
) : SAMPNativeFunctionsParameterGenerator(parameter) {

    private val parameterSpec = ParameterSpec.builder(type, parameter.name).build()

    override fun generateNativeMethodParameterSpec(): ParameterSpec = parameterSpec

    override fun generateNativeMethodInvocationParameterCode(): CodeBlock = CodeBlock.of("\$N", parameterSpec)

    override fun generateWrapperMethodParameterSpec(): ParameterSpec = parameterSpec

    override val isWrapperMethodRequired: Boolean = false

}