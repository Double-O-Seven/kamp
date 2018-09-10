package ch.leadrian.samp.kamp.core.api.constants

enum class CarModType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    SPOILER(SAMPConstants.CARMODTYPE_SPOILER),
    HOOD(SAMPConstants.CARMODTYPE_HOOD),
    ROOF(SAMPConstants.CARMODTYPE_ROOF),
    SIDE_SKIRT(SAMPConstants.CARMODTYPE_SIDESKIRT),
    LAMPS(SAMPConstants.CARMODTYPE_LAMPS),
    NITRO(SAMPConstants.CARMODTYPE_NITRO),
    EXHAUST(SAMPConstants.CARMODTYPE_EXHAUST),
    WHEELS(SAMPConstants.CARMODTYPE_WHEELS),
    STEREO(SAMPConstants.CARMODTYPE_STEREO),
    HYDRAULICS(SAMPConstants.CARMODTYPE_HYDRAULICS),
    FRONT_BUMPER(SAMPConstants.CARMODTYPE_FRONT_BUMPER),
    REAR_BUMPER(SAMPConstants.CARMODTYPE_REAR_BUMPER),
    VENT_RIGHT(SAMPConstants.CARMODTYPE_VENT_RIGHT),
    VENT_LEFT(SAMPConstants.CARMODTYPE_VENT_LEFT);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, CarModType>(*CarModType.values())

}
