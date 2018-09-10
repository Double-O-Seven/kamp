package ch.leadrian.samp.kamp.core.api.constants

enum class FightingStyle(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NORMAL(SAMPConstants.FIGHT_STYLE_NORMAL),
    BOXING(SAMPConstants.FIGHT_STYLE_BOXING),
    KUNGFU(SAMPConstants.FIGHT_STYLE_KUNGFU),
    KNEEHEAD(SAMPConstants.FIGHT_STYLE_KNEEHEAD),
    GRABKICK(SAMPConstants.FIGHT_STYLE_GRABKICK),
    ELBOW(SAMPConstants.FIGHT_STYLE_ELBOW);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, FightingStyle>(*FightingStyle.values())
}