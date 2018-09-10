package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel

internal data class MutableWeaponDataImpl(
        override var model: WeaponModel,
        override var ammo: Int
) : MutableWeaponData {

    override fun toWeaponData(): WeaponData = WeaponDataImpl(
            model = model,
            ammo = ammo
    )

    override fun toMutableWeaponData(): MutableWeaponData = this
}