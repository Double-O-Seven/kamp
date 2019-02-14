package ch.leadrian.samp.kamp.core.api.constants

@Suppress("SpellCheckingInspection")
enum class DialogStyle(override val value: Int) : ConstantValue<Int> {

    MSGBOX(SAMPConstants.DIALOG_STYLE_MSGBOX),
    INPUT(SAMPConstants.DIALOG_STYLE_INPUT),
    LIST(SAMPConstants.DIALOG_STYLE_LIST),
    PASSWORD(SAMPConstants.DIALOG_STYLE_PASSWORD),
    TABLIST(SAMPConstants.DIALOG_STYLE_TABLIST),
    TABLIST_HEADERS(SAMPConstants.DIALOG_STYLE_TABLIST_HEADERS);

    companion object : ConstantValueRegistry<Int, DialogStyle>(DialogStyle.values())

}
