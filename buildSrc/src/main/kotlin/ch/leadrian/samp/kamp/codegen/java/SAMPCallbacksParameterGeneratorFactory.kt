package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Parameter
import ch.leadrian.samp.kamp.cidl.model.Types
import ch.leadrian.samp.kamp.codegen.isOutParameter
import com.squareup.javapoet.TypeName
import javax.inject.Inject

class SAMPCallbacksParameterGeneratorFactory
@Inject
constructor() {

    fun create(parameter: Parameter): SAMPCallbacksParameterGenerator {
        return when {
            parameter.type == Types.INT && parameter.isOutParameter -> SAMPCallbacksObjectParameterGenerator(
                    parameter,
                    REFERENCE_INT_TYPE
            )
            parameter.type == Types.FLOAT && parameter.isOutParameter -> SAMPCallbacksObjectParameterGenerator(
                    parameter,
                    REFERENCE_FLOAT_TYPE
            )
            parameter.type == Types.STRING && parameter.isOutParameter -> SAMPCallbacksObjectParameterGenerator(
                    parameter,
                    REFERENCE_STRING_TYPE
            )
            parameter.type == Types.BOOL -> SAMPCallbacksPrimitiveParameterGenerator(parameter, TypeName.BOOLEAN)
            parameter.type == Types.INT -> SAMPCallbacksPrimitiveParameterGenerator(parameter, TypeName.INT)
            parameter.type == Types.FLOAT -> SAMPCallbacksPrimitiveParameterGenerator(parameter, TypeName.FLOAT)
            parameter.type == Types.STRING -> SAMPCallbacksStringParameterGenerator(parameter)
            else -> throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
        }
    }

}