package ch.leadrian.samp.kamp.core.api.constants

enum class CameraMode(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NONE(-1),
    TRAIN_OR_TRAM(3),
    FOLLOW_PED(4),
    SNIPER_AIMING(7),
    ROCKET_LAUNCHER_AIMING(8),
    FIXED_CAMERA(15),
    VEHICLE_FRONT_OR_BIKE_SIDE(16),
    NORMAL_CAR(18),
    NORMAL_BOAT(22),
    WEAPON_AIMING(46),
    HEAT_SEEKING_ROCKET_LAUNCHER_AIMING(51),
    AIMING_ANY_OTHER_WEAPON(53),
    PASSENGER_DRIVE_BY(55),
    CHASE_CAMERA_HELICOPTER(56),
    CHASE_CAMERA_GROUND_CAMERA_ZOOM_QUICKLY(57),
    CHASE_CAMERA_HORIZONTAL_FLY_BY_VEHICLE(58),
    CHASE_CAMERA_GROUND_CAMERA_LOOK_AT_AIR_VEHICLE(59),
    CHASE_CAMERA_VERTICAL_FLY_BY(62),
    CHASE_CAMERA_HORIZONTAL_FLY_BY_AIRCRAFT(63),
    CHASE_CAMERA_FOCUS_ON_PILOT(64);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, CameraMode>(*CameraMode.values())
}