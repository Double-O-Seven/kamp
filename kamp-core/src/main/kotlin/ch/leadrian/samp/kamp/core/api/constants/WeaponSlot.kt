package ch.leadrian.samp.kamp.core.api.constants

enum class WeaponSlot(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    INVALID(-1),
    HAND(0),
    MELEE(1),
    PISTOL(2),
    SHOTGUN(3),
    MACHINE_PISTOL(4),
    CARABINER(5),
    RIFLE(6),
    HEAVY(7),
    THROWABLE(8),
    MISC1(9),
    MISC2(10),
    WEARABLE(11),
    DETONATOR(12);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, WeaponSlot>(*WeaponSlot.values())
}