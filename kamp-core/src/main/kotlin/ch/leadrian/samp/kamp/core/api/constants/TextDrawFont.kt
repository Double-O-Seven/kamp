package ch.leadrian.samp.kamp.core.api.constants

@Suppress("SpellCheckingInspection")
enum class TextDrawFont(override val value: Int) : ConstantValue<Int> {

    DIPLOMA(0),
    FONT2(1),
    BANK_GOTHIC(2),
    PRICEDOWN(3),
    SPRITE_DRAW(SAMPConstants.TEXT_DRAW_FONT_SPRITE_DRAW),
    MODEL_PREVIEW(SAMPConstants.TEXT_DRAW_FONT_MODEL_PREVIEW);

    companion object : ConstantValueRegistry<Int, TextDrawFont>(*TextDrawFont.values())

}
