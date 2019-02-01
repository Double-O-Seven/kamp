package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.MutableWeaponData
import ch.leadrian.samp.kamp.core.api.data.WeaponData

internal data class MutableWeaponDataImpl(
        override var model: WeaponModel,
        override var ammo: Int
) : MutableWeaponData {

    override fun toWeaponData(): WeaponData = WeaponDataImpl(
            model = model,
            ammo = ammo
    )

    override fun toMutableWeaponData(): MutableWeaponData = this

    override val modelId: Int
        get() = model.modelId
}