package ch.leadrian.samp.kamp.core.api.constants

enum class WeaponSkill(override val value: Int) : ConstantValue<Int> {
    PISTOL(SAMPConstants.WEAPONSKILL_PISTOL),
    PISTOL_SILENCED(SAMPConstants.WEAPONSKILL_PISTOL_SILENCED),
    DESERT_EAGLE(SAMPConstants.WEAPONSKILL_DESERT_EAGLE),
    SHOTGUN(SAMPConstants.WEAPONSKILL_SHOTGUN),
    SAWED_OFF_SHOTGUN(SAMPConstants.WEAPONSKILL_SAWNOFF_SHOTGUN),
    SPAS12_SHOTGUN(SAMPConstants.WEAPONSKILL_SPAS12_SHOTGUN),
    MICRO_UZI(SAMPConstants.WEAPONSKILL_MICRO_UZI),
    MP5(SAMPConstants.WEAPONSKILL_MP5),
    AK47(SAMPConstants.WEAPONSKILL_AK47),
    M4(SAMPConstants.WEAPONSKILL_M4),
    SNIPER_RIFLE(SAMPConstants.WEAPONSKILL_SNIPERRIFLE);

    companion object : ConstantValueRegistry<Int, WeaponSkill>(WeaponSkill.values())

}
