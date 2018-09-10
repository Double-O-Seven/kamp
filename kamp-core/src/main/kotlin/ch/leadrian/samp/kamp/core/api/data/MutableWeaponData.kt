package ch.leadrian.samp.kamp.core.api.data

interface MutableWeaponData : WeaponData {

    override var model: ch.leadrian.samp.kamp.core.api.constants.WeaponModel

    override var ammo: Int

}

fun mutableWeaponDataOf(model: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, ammo: Int): MutableWeaponData = MutableWeaponDataImpl(model = model, ammo = ammo)