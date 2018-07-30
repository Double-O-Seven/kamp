package ch.leadrian.samp.kamp.api.constants

enum class PlayerState(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.PLAYER_STATE_NONE),
    ONFOOT(SAMPConstants.PLAYER_STATE_ONFOOT),
    DRIVER(SAMPConstants.PLAYER_STATE_DRIVER),
    PASSENGER(SAMPConstants.PLAYER_STATE_PASSENGER),
    EXIT_VEHICLE(SAMPConstants.PLAYER_STATE_EXIT_VEHICLE),
    ENTER_VEHICLE_DRIVER(SAMPConstants.PLAYER_STATE_ENTER_VEHICLE_DRIVER),
    ENTER_VEHICLE_PASSENGER(SAMPConstants.PLAYER_STATE_ENTER_VEHICLE_PASSENGER),
    WASTED(SAMPConstants.PLAYER_STATE_WASTED),
    SPAWNED(SAMPConstants.PLAYER_STATE_SPAWNED),
    SPECTATING(SAMPConstants.PLAYER_STATE_SPECTATING);

    companion object : ConstantValueRegistry<Int, PlayerState>(*PlayerState.values())

}
