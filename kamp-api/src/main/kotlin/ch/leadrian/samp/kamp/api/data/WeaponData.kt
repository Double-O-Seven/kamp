package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.WeaponModel

interface WeaponData {

    val model: WeaponModel

    val ammo: Int

    fun toWeaponData(): WeaponData

    fun toMutableWeaponData(): MutableWeaponData

}

fun weaponDataOf(model: WeaponModel, ammo: Int): WeaponData = WeaponDataImpl(model = model, ammo = ammo)