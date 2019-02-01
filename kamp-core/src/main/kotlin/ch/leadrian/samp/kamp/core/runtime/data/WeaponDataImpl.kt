package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.base.HasModelId
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.MutableWeaponData
import ch.leadrian.samp.kamp.core.api.data.WeaponData

internal data class WeaponDataImpl(
        override val model: WeaponModel,
        override val ammo: Int
) : WeaponData, HasModelId by model {

    override fun toWeaponData(): WeaponData = this

    override fun toMutableWeaponData(): MutableWeaponData = MutableWeaponDataImpl(
            model = model,
            ammo = ammo
    )
}