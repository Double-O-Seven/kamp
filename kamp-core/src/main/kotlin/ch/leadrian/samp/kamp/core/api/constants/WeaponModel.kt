package ch.leadrian.samp.kamp.core.api.constants

/**
 * Base damage and range taken from https://github.com/oscar-broman/samp-weapon-config
 */
enum class WeaponModel(
        override val value: Int,
        val slot: ch.leadrian.samp.kamp.core.api.constants.WeaponSlot,
        val modelId: Int,
        val skill: ch.leadrian.samp.kamp.core.api.constants.WeaponSkill? = null,
        val isTwoHanded: Boolean = false,
        val baseDamage: Float,
        val range: Float = 0f
) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    FIST(
            value = SAMPConstants.WEAPON_FIST,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HAND,
            modelId = 0,
            baseDamage = 1f
    ),
    BRASS_KNUCKLE(
            value = SAMPConstants.WEAPON_BRASSKNUCKLE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HAND,
            modelId = 331,
            baseDamage = 1f
    ),
    GOLF_CLUB(
            value = SAMPConstants.WEAPON_GOLFCLUB,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 333,
            baseDamage = 1f
    ),
    NIGHT_STICK(
            value = SAMPConstants.WEAPON_NITESTICK,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 334,
            baseDamage = 1f
    ),
    KNIFE(
            value = SAMPConstants.WEAPON_KNIFE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 335,
            baseDamage = 1f
    ),
    BAT(
            value = SAMPConstants.WEAPON_BAT,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 336,
            baseDamage = 1f
    ),
    SHOVEL(
            value = SAMPConstants.WEAPON_SHOVEL,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 337,
            baseDamage = 1f
    ),
    POOL_STICK(
            value = SAMPConstants.WEAPON_POOLSTICK,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 338,
            baseDamage = 1f
    ),
    KATANA(
            value = SAMPConstants.WEAPON_KATANA,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 339,
            baseDamage = 1f
    ),
    CHAINSAW(
            value = SAMPConstants.WEAPON_CHAINSAW,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MELEE,
            modelId = 341,
            baseDamage = 1f
    ),
    DILDO(
            value = SAMPConstants.WEAPON_DILDO,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 321,
            baseDamage = 1f
    ),
    DILDO2(
            value = SAMPConstants.WEAPON_DILDO2,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 322,
            baseDamage = 1f
    ),
    VIBRATOR(
            value = SAMPConstants.WEAPON_VIBRATOR,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 323,
            baseDamage = 1f
    ),
    VIBRATOR2(
            value = SAMPConstants.WEAPON_VIBRATOR2,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 324,
            baseDamage = 1f
    ),
    FLOWER(
            value = SAMPConstants.WEAPON_FLOWER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 325,
            baseDamage = 1f
    ),
    CANE(
            value = SAMPConstants.WEAPON_CANE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC2,
            modelId = 326,
            baseDamage = 1f
    ),
    GRENADE(
            value = SAMPConstants.WEAPON_GRENADE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.THROWABLE,
            modelId = 342,
            baseDamage = 82.5f
    ),
    TEARGAS(
            value = SAMPConstants.WEAPON_TEARGAS,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.THROWABLE,
            modelId = 343,
            baseDamage = 0f
    ),
    MOLOTOV(
            value = SAMPConstants.WEAPON_MOLTOV,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.THROWABLE,
            modelId = 344,
            baseDamage = 1f
    ),
    COLT45(
            value = SAMPConstants.WEAPON_COLT45,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.PISTOL,
            modelId = 346,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.PISTOL,
            isTwoHanded = true,
            baseDamage = 8.25f,
            range = 35f
    ),
    SILENCED(
            value = SAMPConstants.WEAPON_SILENCED,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.PISTOL,
            modelId = 347,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.PISTOL_SILENCED,
            baseDamage = 13.2f,
            range = 35f
    ),
    DESERT_EAGLE(
            value = SAMPConstants.WEAPON_DEAGLE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.PISTOL,
            modelId = 348,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.DESERT_EAGLE,
            baseDamage = 46.2f,
            range = 35f
    ),
    SHOTGUN(
            value = SAMPConstants.WEAPON_SHOTGUN,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.SHOTGUN,
            modelId = 349,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.SHOTGUN,
            baseDamage = 3.3f,
            range = 40f
    ),
    SAWED_OFF_SHOTGUN(
            value = SAMPConstants.WEAPON_SAWEDOFF,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.SHOTGUN,
            modelId = 350,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.SAWED_OFF_SHOTGUN,
            isTwoHanded = true,
            baseDamage = 3.3f,
            range = 35f
    ),
    SPAS12_SHOTGUN(
            value = SAMPConstants.WEAPON_SHOTGSPA,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.SHOTGUN,
            modelId = 351,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.SPAS12_SHOTGUN,
            baseDamage = 4.95f,
            range = 40f
    ),
    MICRO_UZI(
            value = SAMPConstants.WEAPON_UZI,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MACHINE_PISTOL,
            modelId = 352,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.MICRO_UZI,
            isTwoHanded = true,
            baseDamage = 6.6f,
            range = 35f
    ),
    MP5(
            value = SAMPConstants.WEAPON_MP5,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MACHINE_PISTOL,
            modelId = 353,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.MP5,
            baseDamage = 8.25f,
            range = 45f
    ),
    AK47(
            value = SAMPConstants.WEAPON_AK47,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.CARABINER,
            modelId = 355,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.AK47,
            baseDamage = 9.9f,
            range = 70f
    ),
    M4(
            value = SAMPConstants.WEAPON_M4,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.CARABINER,
            modelId = 356,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.M4,
            baseDamage = 9.9f,
            range = 90f
    ),
    TEC9(
            value = SAMPConstants.WEAPON_TEC9,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MACHINE_PISTOL,
            modelId = 372,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.MICRO_UZI,
            isTwoHanded = true,
            baseDamage = 6.6f,
            range = 35f
    ),
    RIFLE(
            value = SAMPConstants.WEAPON_RIFLE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.RIFLE,
            modelId = 357,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.SNIPER_RIFLE,
            baseDamage = 24.75f,
            range = 100f
    ),
    SNIPER_RIFLE(
            value = SAMPConstants.WEAPON_SNIPER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.RIFLE,
            modelId = 358,
            skill = ch.leadrian.samp.kamp.core.api.constants.WeaponSkill.SNIPER_RIFLE,
            baseDamage = 41.25f,
            range = 320f
    ),
    ROCKET_LAUNCHER(
            value = SAMPConstants.WEAPON_ROCKETLAUNCHER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HEAVY,
            modelId = 359,
            baseDamage = 82.5f
    ),
    HEAT_SEEKER(
            value = SAMPConstants.WEAPON_HEATSEEKER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HEAVY,
            modelId = 360,
            baseDamage = 82.5f
    ),
    FLAMETHROWER(
            value = SAMPConstants.WEAPON_FLAMETHROWER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HEAVY,
            modelId = 361,
            baseDamage = 1f
    ),
    MINIGUN(
            value = SAMPConstants.WEAPON_MINIGUN,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.HEAVY,
            modelId = 362,
            baseDamage = 46.2f,
            range = 75f
    ),
    SATCHEL(
            value = SAMPConstants.WEAPON_SATCHEL,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.THROWABLE,
            modelId = 363,
            baseDamage = 82.5f
    ),
    BOMB(
            value = SAMPConstants.WEAPON_BOMB,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.DETONATOR,
            modelId = 364,
            baseDamage = 0f
    ),
    SPRAY_CAN(
            value = SAMPConstants.WEAPON_SPRAYCAN,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC1,
            modelId = 365,
            baseDamage = 0.33f
    ),
    FIRE_EXTINGUISHER(
            value = SAMPConstants.WEAPON_FIREEXTINGUISHER,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC1,
            modelId = 366,
            baseDamage = 0.33f
    ),
    CAMERA(
            value = SAMPConstants.WEAPON_CAMERA,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.MISC1,
            modelId = 367,
            baseDamage = 0f
    ),
    NIGHT_VISION(
            value = SAMPConstants.WEAPON_NIGHTVISION,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.WEARABLE,
            modelId = 368,
            baseDamage = 0f
    ),
    INFRARED(
            value = SAMPConstants.WEAPON_INFRARED,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.WEARABLE,
            modelId = 368,
            baseDamage = 0f
    ),
    PARACHUTE(
            value = SAMPConstants.WEAPON_PARACHUTE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.WEARABLE,
            modelId = 371,
            baseDamage = 0f
    ),
    FAKE_PISTOL(
            value = 47,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 0f
    ),
    VEHICLE(
            value = SAMPConstants.WEAPON_VEHICLE,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 9.9f
    ),
    HELICOPTER_BLADES(
            value = 50,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 330f
    ),
    EXPLOSION(
            value = 51,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 82.5f
    ),
    DROWN(
            value = SAMPConstants.WEAPON_DROWN,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 1f
    ),
    COLLISION(
            value = SAMPConstants.WEAPON_COLLISION,
            slot = ch.leadrian.samp.kamp.core.api.constants.WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 165f
    );

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, WeaponModel>(*WeaponModel.values())

}
