package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleModelInfoType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    SIZE(SAMPConstants.VEHICLE_MODEL_INFO_SIZE),
    FRONTSEAT(SAMPConstants.VEHICLE_MODEL_INFO_FRONTSEAT),
    REARSEAT(SAMPConstants.VEHICLE_MODEL_INFO_REARSEAT),
    PETROLCAP(SAMPConstants.VEHICLE_MODEL_INFO_PETROLCAP),
    WHEELSFRONT(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSFRONT),
    WHEELSREAR(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSREAR),
    WHEELSMID(SAMPConstants.VEHICLE_MODEL_INFO_WHEELSMID),
    FRONT_BUMPER_Z(SAMPConstants.VEHICLE_MODEL_INFO_FRONT_BUMPER_Z),
    REAR_BUMPER_Z(SAMPConstants.VEHICLE_MODEL_INFO_REAR_BUMPER_Z);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleModelInfoType>(*VehicleModelInfoType.values())

}
