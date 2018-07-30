package ch.leadrian.samp.kamp.api.constants

enum class PlayerRecordingType(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.PLAYER_RECORDING_TYPE_NONE),
    DRIVER(SAMPConstants.PLAYER_RECORDING_TYPE_DRIVER),
    ONFOOT(SAMPConstants.PLAYER_RECORDING_TYPE_ONFOOT);

    companion object : ConstantValueRegistry<Int, PlayerRecordingType>(*PlayerRecordingType.values())

}
