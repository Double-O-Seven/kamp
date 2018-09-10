package ch.leadrian.samp.kamp.core.api.constants

enum class BodyPart(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    TORSO(SAMPConstants.BODY_PART_TORSO),
    GROIN(SAMPConstants.BODY_PART_GROIN),
    LEFT_ARM(SAMPConstants.BODY_PART_LEFT_ARM),
    RIGHT_ARM(SAMPConstants.BODY_PART_RIGHT_ARM),
    LEFT_LEG(SAMPConstants.BODY_PART_LEFT_LEG),
    RIGHT_LEG(SAMPConstants.BODY_PART_RIGHT_LEG),
    HEAD(SAMPConstants.BODY_PART_HEAD);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, BodyPart>(*BodyPart.values())

}
