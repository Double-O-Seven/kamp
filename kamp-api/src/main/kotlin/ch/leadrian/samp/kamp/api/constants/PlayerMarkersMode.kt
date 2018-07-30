package ch.leadrian.samp.kamp.api.constants

enum class PlayerMarkersMode(override val value: Int) : ConstantValue<Int> {
    OFF(SAMPConstants.PLAYER_MARKERS_MODE_OFF),
    GLOBAL(SAMPConstants.PLAYER_MARKERS_MODE_GLOBAL),
    STREAMED(SAMPConstants.PLAYER_MARKERS_MODE_STREAMED);

    companion object : ConstantValueRegistry<Int, PlayerMarkersMode>(*PlayerMarkersMode.values())

}
