package ch.leadrian.samp.kamp.core.api.constants

enum class DialogStyle(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    MSGBOX(SAMPConstants.DIALOG_STYLE_MSGBOX),
    INPUT(SAMPConstants.DIALOG_STYLE_INPUT),
    LIST(SAMPConstants.DIALOG_STYLE_LIST),
    PASSWORD(SAMPConstants.DIALOG_STYLE_PASSWORD),
    TABLIST(SAMPConstants.DIALOG_STYLE_TABLIST),
    TABLIST_HEADERS(SAMPConstants.DIALOG_STYLE_TABLIST_HEADERS);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, DialogStyle>(*DialogStyle.values())

}
