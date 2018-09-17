package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel

class WeaponModelCommandParameterResolver : CommandParameterResolver<WeaponModel> {

    override val parameterType: Class<WeaponModel> = WeaponModel::class.java

    override fun resolve(value: String): WeaponModel? {
        val modelId = value.toIntOrNull()
        return when {
            modelId != null && WeaponModel.exists(modelId) -> WeaponModel[modelId]
            modelId != null -> null
            else -> WeaponModel[value] ?: getWeaponModelByEnumValue(value)
        }
    }

    private fun getWeaponModelByEnumValue(value: String): WeaponModel? =
            try {
                enumValueOf<WeaponModel>(value.toUpperCase())
            } catch (e: IllegalArgumentException) {
                null
            }
}