package ch.leadrian.samp.kamp.api.constants

enum class EditResponse(override val value: Int) : ConstantValue<Int> {
    CANCEL(SAMPConstants.EDIT_RESPONSE_CANCEL),
    FINAL(SAMPConstants.EDIT_RESPONSE_FINAL),
    UPDATE(SAMPConstants.EDIT_RESPONSE_UPDATE);

    companion object : ConstantValueRegistry<Int, EditResponse>(*EditResponse.values())

}
