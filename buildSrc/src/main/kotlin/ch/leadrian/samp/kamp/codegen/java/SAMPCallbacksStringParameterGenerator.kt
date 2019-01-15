package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Parameter
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import org.jetbrains.annotations.NotNull

internal class SAMPCallbacksStringParameterGenerator(
        parameter: Parameter
) : SAMPCallbacksParameterGenerator(parameter) {

    private val defaultMethodParameterSpec: ParameterSpec =
            ParameterSpec
                    .builder(ArrayTypeName.of(Byte::class.javaPrimitiveType), parameter.name)
                    .addAnnotation(NotNull::class.java)
                    .build()

    override fun generateAbstractMethodParameterSpec(): ParameterSpec =
            ParameterSpec
                    .builder(ClassName.get(String::class.java), parameter.name)
                    .addAnnotation(NotNull::class.java)
                    .build()

    override fun generateAbstractMethodInvocationParameterCode(): CodeBlock =
            CodeBlock.of(
                    "new \$T(\$N, \$T.getCharset())",
                    String::class.java,
                    defaultMethodParameterSpec,
                    STRING_ENCODING_TYPE
            )

    override fun generateDefaultMethodParameterSpec(): ParameterSpec = defaultMethodParameterSpec

    override val isDefaultMethodRequired: Boolean = true

}