package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.constants.SkinModel

internal class SkinModelCommandParameterResolver : CommandParameterResolver<SkinModel> {

    override val parameterType: Class<SkinModel> = SkinModel::class.java

    override fun resolve(value: String): SkinModel? {
        val modelId = value.toIntOrNull()
        return when {
            modelId != null && SkinModel.exists(modelId) -> SkinModel[modelId]
            modelId != null -> null
            else -> SkinModel[value] ?: getSkinModelByEnumValue(value)
        }
    }

    private fun getSkinModelByEnumValue(value: String): SkinModel? =
            try {
                enumValueOf<SkinModel>(value.toUpperCase())
            } catch (e: IllegalArgumentException) {
                null
            }
}