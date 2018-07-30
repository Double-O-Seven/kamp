package ch.leadrian.samp.kamp.api.constants

enum class BulletHitType(override val value: Int) : ConstantValue<Int> {
    NONE(SAMPConstants.BULLET_HIT_TYPE_NONE),
    PLAYER(SAMPConstants.BULLET_HIT_TYPE_PLAYER),
    VEHICLE(SAMPConstants.BULLET_HIT_TYPE_VEHICLE),
    OBJECT(SAMPConstants.BULLET_HIT_TYPE_OBJECT),
    PLAYER_OBJECT(SAMPConstants.BULLET_HIT_TYPE_PLAYER_OBJECT);

    companion object : ConstantValueRegistry<Int, BulletHitType>(*BulletHitType.values())

}
