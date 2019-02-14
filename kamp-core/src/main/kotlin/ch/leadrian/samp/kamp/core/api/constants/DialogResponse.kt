package ch.leadrian.samp.kamp.core.api.constants

enum class DialogResponse(override val value: Int, val isCancelButton: Boolean) : ConstantValue<Int> {
    LEFT_BUTTON(1, false),
    RIGHT_BUTTON(0, true);

    companion object : ConstantValueRegistry<Int, DialogResponse>(DialogResponse.values())
}