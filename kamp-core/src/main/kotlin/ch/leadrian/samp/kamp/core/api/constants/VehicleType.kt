package ch.leadrian.samp.kamp.core.api.constants

/**
 * Taken from https://github.com/Shoebill/shoebill-api
 */
enum class VehicleType(override val value: Int) : ConstantValue<Int> {

    UNKNOWN(-1),
    BICYCLE(0),
    MOTORBIKE(1),
    CAR(2),
    TRAILER(3),
    REMOTE_CONTROL(4),
    TRAIN(5),
    BOAT(6),
    AIRCRAFT(7),
    HELICOPTER(8),
    TANK(9);

    companion object : ConstantValueRegistry<Int, VehicleType>(*VehicleType.values())
}