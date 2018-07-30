package ch.leadrian.samp.kamp.api.constants

enum class PlayerVarType(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.PLAYER_VARTYPE_NONE),
    INT(SAMPConstants.PLAYER_VARTYPE_INT),
    STRING(SAMPConstants.PLAYER_VARTYPE_STRING),
    FLOAT(SAMPConstants.PLAYER_VARTYPE_FLOAT);

    companion object : ConstantValueRegistry<Int, PlayerVarType>(*PlayerVarType.values())

}
