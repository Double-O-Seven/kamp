package ch.leadrian.samp.kamp.core.api.constants

enum class DialogResponse(override val value: Int) : ConstantValue<Int> {
    LEFT_BUTTON(1),
    RIGHT_BUTTON(0);

    companion object : ConstantValueRegistry<Int, DialogResponse>(*DialogResponse.values())
}