package ch.leadrian.samp.kamp.core.api.constants

enum class ClickPlayerSource(override val value: Int) : ConstantValue<Int> {
    SCOREBOARD(SAMPConstants.CLICK_SOURCE_SCOREBOARD);

    companion object : ConstantValueRegistry<Int, ClickPlayerSource>(*ClickPlayerSource.values())

}
