package ch.leadrian.samp.kamp.core.api.constants

enum class SpectateType(override val value: Int) : ConstantValue<Int> {
    NORMAL(SAMPConstants.SPECTATE_MODE_NORMAL),
    FIXED(SAMPConstants.SPECTATE_MODE_FIXED),
    SIDE(SAMPConstants.SPECTATE_MODE_SIDE);

    companion object : ConstantValueRegistry<Int, SpectateType>(*SpectateType.values())

}
