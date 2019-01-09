package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleModelInfoType(override val value: Int) : ConstantValue<Int> {
    SIZE(SAMPConstants.VEHICLE_MODEL_INFO_SIZE),
    FRONT_SEAT(SAMPConstants.VEHICLE_MODEL_INFO_FRONTSEAT),
    REAR_SEAT(SAMPConstants.VEHICLE_MODEL_INFO_REARSEAT),
    PETROL_CAP(SAMPConstants.VEHICLE_MODEL_INFO_PETROLCAP),
    FRONT_WHEELS(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSFRONT),
    REAR_WHEELS(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSREAR),
    MID_WHEELS(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSMID),
    FRONT_BUMPER_Z(SAMPConstants.VEHICLE_MODEL_INFO_FRONT_BUMPER_Z),
    REAR_BUMPER_Z(SAMPConstants.VEHICLE_MODEL_INFO_REAR_BUMPER_Z);

    companion object : ConstantValueRegistry<Int, VehicleModelInfoType>(*VehicleModelInfoType.values())

}
