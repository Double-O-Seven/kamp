package ch.leadrian.samp.kamp.core.api.constants

enum class PlayerRecordingType(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.PLAYER_RECORDING_TYPE_NONE),
    DRIVER(SAMPConstants.PLAYER_RECORDING_TYPE_DRIVER),
    ON_FOOT(SAMPConstants.PLAYER_RECORDING_TYPE_ONFOOT);

    companion object : ConstantValueRegistry<Int, PlayerRecordingType>(PlayerRecordingType.values())

}
