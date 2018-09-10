package ch.leadrian.samp.kamp.core.api.constants

enum class Bone(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NOT_USABLE(0),
    SPINE(1),
    HEAD(2),
    UPPER_ARM_LEFT(3),
    UPPER_ARM_RIGHT(4),
    HAND_LEFT(5),
    HAND_RIGHT(6),
    THIGH_LEFT(7),
    THIGH_RIGHT(8),
    FOOT_LEFT(9),
    FOOT_RIGHT(10),
    CALF_RIGHT(11),
    CALF_LEFT(12),
    FOREARM_LEFT(13),
    FOREARM_RIGHT(14),
    CLAVICLE_LEFT(15),
    CLAVICLE_RIGHT(16),
    NECK(17),
    JAW(18);


    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, Bone>(*Bone.values())

}