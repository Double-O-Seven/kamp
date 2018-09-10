package ch.leadrian.samp.kamp.core.api.data

internal data class WeaponDataImpl(
        override val model: ch.leadrian.samp.kamp.core.api.constants.WeaponModel,
        override val ammo: Int
) : WeaponData {

    override fun toWeaponData(): WeaponData = this

    override fun toMutableWeaponData(): MutableWeaponData = MutableWeaponDataImpl(
            model = model,
            ammo = ammo
    )
}