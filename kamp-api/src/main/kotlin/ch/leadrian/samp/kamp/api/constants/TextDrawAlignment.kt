package ch.leadrian.samp.kamp.api.constants

enum class TextDrawAlignment(override val value: Int) : ConstantValue<Int> {
    LEFT(1),
    CENTERED(2),
    RIGHT(3);

    companion object : ConstantValueRegistry<Int, TextDrawAlignment>(*TextDrawAlignment.values())
}