package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Parameter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName

internal class SAMPCallbacksPrimitiveParameterGenerator(
        parameter: Parameter,
        type: TypeName
) : SAMPCallbacksParameterGenerator(parameter) {

    private val parameterSpec = ParameterSpec.builder(type, parameter.name).build()

    override fun generateAbstractMethodParameterSpec(): ParameterSpec = parameterSpec

    override fun generateAbstractMethodInvocationParameterCode(): CodeBlock = CodeBlock.of("\$N", parameterSpec)

    override fun generateDefaultMethodParameterSpec(): ParameterSpec = parameterSpec

    override val isDefaultMethodRequired: Boolean = false

}