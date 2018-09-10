package ch.leadrian.samp.kamp.core.api.constants

enum class ObjectEditResponse(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    CANCEL(SAMPConstants.EDIT_RESPONSE_CANCEL),
    FINAL(SAMPConstants.EDIT_RESPONSE_FINAL),
    UPDATE(SAMPConstants.EDIT_RESPONSE_UPDATE);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, ObjectEditResponse>(*ObjectEditResponse.values())

}
