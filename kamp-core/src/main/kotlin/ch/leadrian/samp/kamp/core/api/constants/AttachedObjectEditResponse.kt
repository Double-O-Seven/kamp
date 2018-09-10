package ch.leadrian.samp.kamp.core.api.constants

enum class AttachedObjectEditResponse(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    CANCEL(0),
    SAVE(1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, AttachedObjectEditResponse>(*AttachedObjectEditResponse.values())

}
