package ch.leadrian.samp.kamp.core.api.constants

enum class WeaponState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    UNKNOWN(SAMPConstants.WEAPONSTATE_UNKNOWN),
    NO_BULLETS(SAMPConstants.WEAPONSTATE_NO_BULLETS),
    LAST_BULLET(SAMPConstants.WEAPONSTATE_LAST_BULLET),
    MORE_BULLETS(SAMPConstants.WEAPONSTATE_MORE_BULLETS),
    RELOADING(SAMPConstants.WEAPONSTATE_RELOADING);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, WeaponState>(*WeaponState.values())

}
