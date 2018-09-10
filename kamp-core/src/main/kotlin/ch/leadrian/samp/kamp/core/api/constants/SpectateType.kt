package ch.leadrian.samp.kamp.core.api.constants

enum class SpectateType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NORMAL(SAMPConstants.SPECTATE_MODE_NORMAL),
    FIXED(SAMPConstants.SPECTATE_MODE_FIXED),
    SIDE(SAMPConstants.SPECTATE_MODE_SIDE);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, SpectateType>(*SpectateType.values())

}
