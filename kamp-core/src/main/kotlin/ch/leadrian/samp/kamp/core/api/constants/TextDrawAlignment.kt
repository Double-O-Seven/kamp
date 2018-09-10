package ch.leadrian.samp.kamp.core.api.constants

enum class TextDrawAlignment(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    LEFT(1),
    CENTERED(2),
    RIGHT(3);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, TextDrawAlignment>(*TextDrawAlignment.values())
}