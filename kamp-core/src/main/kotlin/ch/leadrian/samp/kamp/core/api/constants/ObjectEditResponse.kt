package ch.leadrian.samp.kamp.core.api.constants

enum class ObjectEditResponse(override val value: Int) : ConstantValue<Int> {
    CANCEL(SAMPConstants.EDIT_RESPONSE_CANCEL),
    FINAL(SAMPConstants.EDIT_RESPONSE_FINAL),
    UPDATE(SAMPConstants.EDIT_RESPONSE_UPDATE);

    companion object : ConstantValueRegistry<Int, ObjectEditResponse>(ObjectEditResponse.values())

}
