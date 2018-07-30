package ch.leadrian.samp.kamp.api.constants

enum class ClickSource(override val value: Int) : ConstantValue<Int> {
    SCOREBOARD(SAMPConstants.CLICK_SOURCE_SCOREBOARD);

    companion object : ConstantValueRegistry<Int, ClickSource>(*ClickSource.values())

}
