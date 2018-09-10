package ch.leadrian.samp.kamp.core.api.constants

enum class ServerVarType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NONE(SAMPConstants.SERVER_VARTYPE_NONE),
    INT(SAMPConstants.SERVER_VARTYPE_INT),
    STRING(SAMPConstants.SERVER_VARTYPE_STRING),
    FLOAT(SAMPConstants.SERVER_VARTYPE_FLOAT);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, ServerVarType>(*ServerVarType.values())

}
