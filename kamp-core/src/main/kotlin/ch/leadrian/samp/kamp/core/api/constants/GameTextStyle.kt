package ch.leadrian.samp.kamp.core.api.constants

@Suppress("SpellCheckingInspection")
enum class GameTextStyle(override val value: Int) : ConstantValue<Int> {

    PRICEDOWN_CENTER_9_SECONDS(0),
    PRICEDOWN_BOTTOM_RIGHT_8_SECONDS(1),
    DIPLOMA_CENTER_UNTIL_RESPAWN(2),
    BANK_GOTHIC_CENTER_1(3),
    BANK_GOTHIC_CENTER_2(4),
    BANK_GOTHIC_CENTER_3_SECONDS(5),
    PRICEDOWN_TOP_CENTER(6);

    companion object : ConstantValueRegistry<Int, GameTextStyle>(*GameTextStyle.values())
}