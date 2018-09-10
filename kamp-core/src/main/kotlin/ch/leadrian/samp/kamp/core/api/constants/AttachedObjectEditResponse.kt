package ch.leadrian.samp.kamp.core.api.constants

enum class AttachedObjectEditResponse(override val value: Int) : ConstantValue<Int> {
    CANCEL(0),
    SAVE(1);

    companion object : ConstantValueRegistry<Int, AttachedObjectEditResponse>(*AttachedObjectEditResponse.values())

}
