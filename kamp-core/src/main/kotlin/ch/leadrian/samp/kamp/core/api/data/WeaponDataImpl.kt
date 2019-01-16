package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel

internal data class WeaponDataImpl(
        override val model: WeaponModel,
        override val ammo: Int
) : WeaponData {

    override fun toWeaponData(): WeaponData = this

    override fun toMutableWeaponData(): MutableWeaponData = MutableWeaponDataImpl(
            model = model,
            ammo = ammo
    )
}