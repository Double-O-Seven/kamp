package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Parameter
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import org.jetbrains.annotations.NotNull

internal class SAMPNativeFunctionsStringParameterGenerator(parameter: Parameter) :
        SAMPNativeFunctionsParameterGenerator(parameter) {

    private val wrapperMethodParameterSpec: ParameterSpec =
            ParameterSpec
                    .builder(ClassName.get(String::class.java), parameter.name)
                    .addAnnotation(NotNull::class.java)
                    .build()

    override fun generateNativeMethodParameterSpec(): ParameterSpec =
            ParameterSpec
                    .builder(ArrayTypeName.of(Byte::class.javaPrimitiveType), parameter.name)
                    .addAnnotation(NotNull::class.java)
                    .build()

    override fun generateNativeMethodInvocationParameterCode(): CodeBlock =
            CodeBlock.of(
                    "\$N.getBytes(\$T.getCharset())",
                    wrapperMethodParameterSpec,
                    STRING_ENCODING_TYPE
            )

    override fun generateWrapperMethodParameterSpec(): ParameterSpec = wrapperMethodParameterSpec

    override val isWrapperMethodRequired: Boolean = true

}