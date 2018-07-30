package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.WeaponModel

interface MutableWeaponData : WeaponData {

    override var model: WeaponModel

    override var ammo: Int

}

fun mutableWeaponDataOf(model: WeaponModel, ammo: Int): MutableWeaponData = MutableWeaponDataImpl(model = model, ammo = ammo)