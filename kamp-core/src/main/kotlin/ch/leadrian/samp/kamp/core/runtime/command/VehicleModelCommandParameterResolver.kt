package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel

internal class VehicleModelCommandParameterResolver : CommandParameterResolver<VehicleModel> {

    override val parameterType: Class<VehicleModel> = VehicleModel::class.java

    override fun resolve(value: String): VehicleModel? {
        val modelId = value.toIntOrNull()
        return when {
            modelId != null && VehicleModel.exists(modelId) -> VehicleModel[modelId]
            modelId != null -> null
            else -> VehicleModel[value] ?: getVehicleModelByEnumValue(value)
        }
    }

    private fun getVehicleModelByEnumValue(value: String): VehicleModel? =
            try {
                enumValueOf<VehicleModel>(value.toUpperCase())
            } catch (e: IllegalArgumentException) {
                null
            }
}