package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.base.HasModelId
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.runtime.data.WeaponDataImpl

interface WeaponData : HasModelId {

    val model: WeaponModel

    val ammo: Int

    fun toWeaponData(): WeaponData

    fun toMutableWeaponData(): MutableWeaponData

    companion object {

        val FISTS = weaponDataOf(model = WeaponModel.FIST, ammo = 0)
    }

}

fun weaponDataOf(model: WeaponModel, ammo: Int): WeaponData = WeaponDataImpl(
        model = model,
        ammo = ammo
)