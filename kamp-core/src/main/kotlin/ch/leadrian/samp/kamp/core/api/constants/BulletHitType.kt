package ch.leadrian.samp.kamp.core.api.constants

enum class BulletHitType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NONE(SAMPConstants.BULLET_HIT_TYPE_NONE),
    PLAYER(SAMPConstants.BULLET_HIT_TYPE_PLAYER),
    VEHICLE(SAMPConstants.BULLET_HIT_TYPE_VEHICLE),
    OBJECT(SAMPConstants.BULLET_HIT_TYPE_OBJECT),
    PLAYER_OBJECT(SAMPConstants.BULLET_HIT_TYPE_PLAYER_OBJECT);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, BulletHitType>(*BulletHitType.values())

}
