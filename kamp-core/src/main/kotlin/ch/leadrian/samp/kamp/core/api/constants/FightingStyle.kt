package ch.leadrian.samp.kamp.core.api.constants

@Suppress("SpellCheckingInspection")
enum class FightingStyle(override val value: Int) : ConstantValue<Int> {

    NORMAL(SAMPConstants.FIGHT_STYLE_NORMAL),
    BOXING(SAMPConstants.FIGHT_STYLE_BOXING),
    KUNGFU(SAMPConstants.FIGHT_STYLE_KUNGFU),
    KNEEHEAD(SAMPConstants.FIGHT_STYLE_KNEEHEAD),
    GRABKICK(SAMPConstants.FIGHT_STYLE_GRABKICK),
    ELBOW(SAMPConstants.FIGHT_STYLE_ELBOW);

    companion object : ConstantValueRegistry<Int, FightingStyle>(*FightingStyle.values())
}