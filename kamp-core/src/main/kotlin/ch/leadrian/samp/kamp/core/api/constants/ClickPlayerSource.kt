package ch.leadrian.samp.kamp.core.api.constants

enum class ClickPlayerSource(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    SCOREBOARD(SAMPConstants.CLICK_SOURCE_SCOREBOARD);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, ClickPlayerSource>(*ClickPlayerSource.values())

}
