package ch.leadrian.samp.kamp.api.constants

enum class ServerVarType(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.SERVER_VARTYPE_NONE),
    INT(SAMPConstants.SERVER_VARTYPE_INT),
    STRING(SAMPConstants.SERVER_VARTYPE_STRING),
    FLOAT(SAMPConstants.SERVER_VARTYPE_FLOAT);

    companion object : ConstantValueRegistry<Int, ServerVarType>(*ServerVarType.values())

}
