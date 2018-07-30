package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.WeaponModel

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