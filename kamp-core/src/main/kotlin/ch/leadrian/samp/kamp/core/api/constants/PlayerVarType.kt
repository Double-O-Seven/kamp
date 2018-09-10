package ch.leadrian.samp.kamp.core.api.constants

enum class PlayerVarType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NONE(SAMPConstants.PLAYER_VARTYPE_NONE),
    INT(SAMPConstants.PLAYER_VARTYPE_INT),
    STRING(SAMPConstants.PLAYER_VARTYPE_STRING),
    FLOAT(SAMPConstants.PLAYER_VARTYPE_FLOAT);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, PlayerVarType>(*PlayerVarType.values())

}
