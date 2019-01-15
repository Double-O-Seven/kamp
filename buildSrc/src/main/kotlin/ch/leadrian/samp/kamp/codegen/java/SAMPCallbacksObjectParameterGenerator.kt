package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Parameter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import org.jetbrains.annotations.NotNull

internal class SAMPCallbacksObjectParameterGenerator(
        parameter: Parameter,
        type: TypeName
) : SAMPCallbacksParameterGenerator(parameter) {

    private val parameterSpec =
            ParameterSpec
                    .builder(type, parameter.name)
                    .addAnnotation(NotNull::class.java)
                    .build()

    override fun generateAbstractMethodParameterSpec(): ParameterSpec = parameterSpec

    override fun generateAbstractMethodInvocationParameterCode(): CodeBlock = CodeBlock.of("\$N", parameterSpec)

    override fun generateDefaultMethodParameterSpec(): ParameterSpec = parameterSpec

    override val isDefaultMethodRequired: Boolean = false

}