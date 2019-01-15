package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.cidl.model.Types
import ch.leadrian.samp.kamp.codegen.isOutParameter
import com.squareup.javapoet.TypeName
import javax.inject.Inject

class SAMPNativeFunctionsParameterGeneratorFactory
@Inject
constructor() {

    fun create(parameter: Parameter): SAMPNativeFunctionsParameterGenerator {
        return when {
            parameter.type == Types.INT && parameter.isOutParameter -> SAMPNativeFunctionsObjectParameterGenerator(
                    parameter,
                    REFERENCE_INT_TYPE
            )
            parameter.type == Types.FLOAT && parameter.isOutParameter -> SAMPNativeFunctionsObjectParameterGenerator(
                    parameter,
                    REFERENCE_FLOAT_TYPE
            )
            parameter.type == Types.STRING && parameter.isOutParameter -> SAMPNativeFunctionsObjectParameterGenerator(
                    parameter,
                    REFERENCE_STRING_TYPE
            )
            parameter.type == Types.BOOL -> SAMPNativeFunctionsPrimitiveParameterGenerator(parameter, TypeName.BOOLEAN)
            parameter.type == Types.INT -> SAMPNativeFunctionsPrimitiveParameterGenerator(parameter, TypeName.INT)
            parameter.type == Types.FLOAT -> SAMPNativeFunctionsPrimitiveParameterGenerator(parameter, TypeName.FLOAT)
            parameter.type == Types.STRING -> SAMPNativeFunctionsStringParameterGenerator(parameter)
            else -> throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
        }
    }

}