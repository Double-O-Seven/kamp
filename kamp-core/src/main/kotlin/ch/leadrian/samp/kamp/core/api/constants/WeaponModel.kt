package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.text.HasTextKey
import ch.leadrian.samp.kamp.core.api.text.TextKey
import org.apache.commons.collections4.trie.PatriciaTrie

/**
 * Base damage and range taken from https://github.com/oscar-broman/samp-weapon-config
 */
@Suppress("unused")
enum class WeaponModel(
        override val value: Int,
        override val textKey: TextKey,
        val modelName: String,
        val slot: WeaponSlot,
        val modelId: Int,
        val skill: WeaponSkill? = null,
        val isTwoHanded: Boolean = false,
        val baseDamage: Float,
        val range: Float = 0f
) : ConstantValue<Int>, HasTextKey {

    FIST(
            value = SAMPConstants.WEAPON_FIST,
            textKey = KampCoreTextKeys.weapon.model.name.fist,
            modelName = "Fists",
            slot = WeaponSlot.HAND,
            modelId = 0,
            baseDamage = 1f
    ),
    BRASS_KNUCKLE(
            value = SAMPConstants.WEAPON_BRASSKNUCKLE,
            textKey = KampCoreTextKeys.weapon.model.name.brassknuckle,
            modelName = "Brass knuckles",
            slot = WeaponSlot.HAND,
            modelId = 331,
            baseDamage = 1f
    ),
    GOLF_CLUB(
            value = SAMPConstants.WEAPON_GOLFCLUB,
            textKey = KampCoreTextKeys.weapon.model.name.golfclub,
            modelName = "Golf club",
            slot = WeaponSlot.MELEE,
            modelId = 333,
            baseDamage = 1f
    ),
    NIGHT_STICK(
            value = SAMPConstants.WEAPON_NITESTICK,
            textKey = KampCoreTextKeys.weapon.model.name.nightstick,
            modelName = "Nightstick",
            slot = WeaponSlot.MELEE,
            modelId = 334,
            baseDamage = 1f
    ),
    KNIFE(
            value = SAMPConstants.WEAPON_KNIFE,
            textKey = KampCoreTextKeys.weapon.model.name.knife,
            modelName = "Knife",
            slot = WeaponSlot.MELEE,
            modelId = 335,
            baseDamage = 1f
    ),
    BAT(
            value = SAMPConstants.WEAPON_BAT,
            textKey = KampCoreTextKeys.weapon.model.name.bat,
            modelName = "Baseball bat",
            slot = WeaponSlot.MELEE,
            modelId = 336,
            baseDamage = 1f
    ),
    SHOVEL(
            value = SAMPConstants.WEAPON_SHOVEL,
            textKey = KampCoreTextKeys.weapon.model.name.shovel,
            modelName = "Shovel",
            slot = WeaponSlot.MELEE,
            modelId = 337,
            baseDamage = 1f
    ),
    POOL_STICK(
            value = SAMPConstants.WEAPON_POOLSTICK,
            textKey = KampCoreTextKeys.weapon.model.name.poolstick,
            modelName = "Billiards cue",
            slot = WeaponSlot.MELEE,
            modelId = 338,
            baseDamage = 1f
    ),
    KATANA(
            value = SAMPConstants.WEAPON_KATANA,
            textKey = KampCoreTextKeys.weapon.model.name.katana,
            modelName = "Katana",
            slot = WeaponSlot.MELEE,
            modelId = 339,
            baseDamage = 1f
    ),
    CHAINSAW(
            value = SAMPConstants.WEAPON_CHAINSAW,
            textKey = KampCoreTextKeys.weapon.model.name.chainsaw,
            modelName = "Chainsaw",
            slot = WeaponSlot.MELEE,
            modelId = 341,
            baseDamage = 1f
    ),
    DILDO(
            value = SAMPConstants.WEAPON_DILDO,
            textKey = KampCoreTextKeys.weapon.model.name.dildo,
            modelName = "Dildo",
            slot = WeaponSlot.MISC2,
            modelId = 321,
            baseDamage = 1f
    ),
    DILDO2(
            value = SAMPConstants.WEAPON_DILDO2,
            textKey = KampCoreTextKeys.weapon.model.name.dildo2,
            modelName = "Dildo",
            slot = WeaponSlot.MISC2,
            modelId = 322,
            baseDamage = 1f
    ),
    VIBRATOR(
            value = SAMPConstants.WEAPON_VIBRATOR,
            textKey = KampCoreTextKeys.weapon.model.name.vibrator,
            modelName = "Vibrator",
            slot = WeaponSlot.MISC2,
            modelId = 323,
            baseDamage = 1f
    ),
    VIBRATOR2(
            value = SAMPConstants.WEAPON_VIBRATOR2,
            textKey = KampCoreTextKeys.weapon.model.name.vibrator2,
            modelName = "Vibrator",
            slot = WeaponSlot.MISC2,
            modelId = 324,
            baseDamage = 1f
    ),
    FLOWER(
            value = SAMPConstants.WEAPON_FLOWER,
            textKey = KampCoreTextKeys.weapon.model.name.flower,
            modelName = "Flowers",
            slot = WeaponSlot.MISC2,
            modelId = 325,
            baseDamage = 1f
    ),
    CANE(
            value = SAMPConstants.WEAPON_CANE,
            textKey = KampCoreTextKeys.weapon.model.name.cane,
            modelName = "Cane",
            slot = WeaponSlot.MISC2,
            modelId = 326,
            baseDamage = 1f
    ),
    GRENADE(
            value = SAMPConstants.WEAPON_GRENADE,
            textKey = KampCoreTextKeys.weapon.model.name.grenade,
            modelName = "Grenade",
            slot = WeaponSlot.THROWABLE,
            modelId = 342,
            baseDamage = 82.5f
    ),
    TEARGAS(
            value = SAMPConstants.WEAPON_TEARGAS,
            textKey = KampCoreTextKeys.weapon.model.name.teargas,
            modelName = "Tear gas",
            slot = WeaponSlot.THROWABLE,
            modelId = 343,
            baseDamage = 0f
    ),
    MOLOTOV(
            value = SAMPConstants.WEAPON_MOLTOV,
            textKey = KampCoreTextKeys.weapon.model.name.molotov,
            modelName = "Molotov cocktail",
            slot = WeaponSlot.THROWABLE,
            modelId = 344,
            baseDamage = 1f
    ),
    COLT45(
            value = SAMPConstants.WEAPON_COLT45,
            textKey = KampCoreTextKeys.weapon.model.name.colt45,
            modelName = "Colt 45",
            slot = WeaponSlot.PISTOL,
            modelId = 346,
            skill = WeaponSkill.PISTOL,
            isTwoHanded = true,
            baseDamage = 8.25f,
            range = 35f
    ),
    SILENCED(
            value = SAMPConstants.WEAPON_SILENCED,
            textKey = KampCoreTextKeys.weapon.model.name.silenced,
            modelName = "Silenced Colt 45",
            slot = WeaponSlot.PISTOL,
            modelId = 347,
            skill = WeaponSkill.PISTOL_SILENCED,
            baseDamage = 13.2f,
            range = 35f
    ),
    DESERT_EAGLE(
            value = SAMPConstants.WEAPON_DEAGLE,
            textKey = KampCoreTextKeys.weapon.model.name.deserteagle,
            modelName = "Desert Eagle",
            slot = WeaponSlot.PISTOL,
            modelId = 348,
            skill = WeaponSkill.DESERT_EAGLE,
            baseDamage = 46.2f,
            range = 35f
    ),
    SHOTGUN(
            value = SAMPConstants.WEAPON_SHOTGUN,
            textKey = KampCoreTextKeys.weapon.model.name.shotgun,
            modelName = "Shotgun",
            slot = WeaponSlot.SHOTGUN,
            modelId = 349,
            skill = WeaponSkill.SHOTGUN,
            baseDamage = 3.3f,
            range = 40f
    ),
    SAWED_OFF_SHOTGUN(
            value = SAMPConstants.WEAPON_SAWEDOFF,
            textKey = KampCoreTextKeys.weapon.model.name.sawedoffshotgun,
            modelName = "Sawed-off shotgun",
            slot = WeaponSlot.SHOTGUN,
            modelId = 350,
            skill = WeaponSkill.SAWED_OFF_SHOTGUN,
            isTwoHanded = true,
            baseDamage = 3.3f,
            range = 35f
    ),
    SPAS12_SHOTGUN(
            value = SAMPConstants.WEAPON_SHOTGSPA,
            textKey = KampCoreTextKeys.weapon.model.name.spas12shotgun,
            modelName = "SPAS-12",
            slot = WeaponSlot.SHOTGUN,
            modelId = 351,
            skill = WeaponSkill.SPAS12_SHOTGUN,
            baseDamage = 4.95f,
            range = 40f
    ),
    MICRO_UZI(
            value = SAMPConstants.WEAPON_UZI,
            textKey = KampCoreTextKeys.weapon.model.name.microuzi,
            modelName = "Micro Uri",
            slot = WeaponSlot.MACHINE_PISTOL,
            modelId = 352,
            skill = WeaponSkill.MICRO_UZI,
            isTwoHanded = true,
            baseDamage = 6.6f,
            range = 35f
    ),
    MP5(
            value = SAMPConstants.WEAPON_MP5,
            textKey = KampCoreTextKeys.weapon.model.name.mp5,
            modelName = "MP5",
            slot = WeaponSlot.MACHINE_PISTOL,
            modelId = 353,
            skill = WeaponSkill.MP5,
            baseDamage = 8.25f,
            range = 45f
    ),
    AK47(
            value = SAMPConstants.WEAPON_AK47,
            textKey = KampCoreTextKeys.weapon.model.name.ak47,
            modelName = "AK-47",
            slot = WeaponSlot.CARABINER,
            modelId = 355,
            skill = WeaponSkill.AK47,
            baseDamage = 9.9f,
            range = 70f
    ),
    M4(
            value = SAMPConstants.WEAPON_M4,
            textKey = KampCoreTextKeys.weapon.model.name.m4,
            modelName = "M4",
            slot = WeaponSlot.CARABINER,
            modelId = 356,
            skill = WeaponSkill.M4,
            baseDamage = 9.9f,
            range = 90f
    ),
    TEC9(
            value = SAMPConstants.WEAPON_TEC9,
            textKey = KampCoreTextKeys.weapon.model.name.tec9,
            modelName = "TEC-9",
            slot = WeaponSlot.MACHINE_PISTOL,
            modelId = 372,
            skill = WeaponSkill.MICRO_UZI,
            isTwoHanded = true,
            baseDamage = 6.6f,
            range = 35f
    ),
    RIFLE(
            value = SAMPConstants.WEAPON_RIFLE,
            textKey = KampCoreTextKeys.weapon.model.name.rifle,
            modelName = "Hunting rifle",
            slot = WeaponSlot.RIFLE,
            modelId = 357,
            skill = WeaponSkill.SNIPER_RIFLE,
            baseDamage = 24.75f,
            range = 100f
    ),
    SNIPER_RIFLE(
            value = SAMPConstants.WEAPON_SNIPER,
            textKey = KampCoreTextKeys.weapon.model.name.sniperrifle,
            modelName = "Sniper rifle",
            slot = WeaponSlot.RIFLE,
            modelId = 358,
            skill = WeaponSkill.SNIPER_RIFLE,
            baseDamage = 41.25f,
            range = 320f
    ),
    ROCKET_LAUNCHER(
            value = SAMPConstants.WEAPON_ROCKETLAUNCHER,
            textKey = KampCoreTextKeys.weapon.model.name.rocketlauncher,
            modelName = "Rocket launcher",
            slot = WeaponSlot.HEAVY,
            modelId = 359,
            baseDamage = 82.5f
    ),
    HEAT_SEEKER(
            value = SAMPConstants.WEAPON_HEATSEEKER,
            textKey = KampCoreTextKeys.weapon.model.name.heatseeker,
            modelName = "Heat-seeking rocket launcher",
            slot = WeaponSlot.HEAVY,
            modelId = 360,
            baseDamage = 82.5f
    ),
    FLAMETHROWER(
            value = SAMPConstants.WEAPON_FLAMETHROWER,
            textKey = KampCoreTextKeys.weapon.model.name.flamethrower,
            modelName = "Flamethrower",
            slot = WeaponSlot.HEAVY,
            modelId = 361,
            baseDamage = 1f
    ),
    MINIGUN(
            value = SAMPConstants.WEAPON_MINIGUN,
            textKey = KampCoreTextKeys.weapon.model.name.minigun,
            modelName = "Minigun",
            slot = WeaponSlot.HEAVY,
            modelId = 362,
            baseDamage = 46.2f,
            range = 75f
    ),
    SATCHEL(
            value = SAMPConstants.WEAPON_SATCHEL,
            textKey = KampCoreTextKeys.weapon.model.name.satchel,
            modelName = "Satchel",
            slot = WeaponSlot.THROWABLE,
            modelId = 363,
            baseDamage = 82.5f
    ),
    BOMB(
            value = SAMPConstants.WEAPON_BOMB,
            textKey = KampCoreTextKeys.weapon.model.name.bomb,
            modelName = "Bomb",
            slot = WeaponSlot.DETONATOR,
            modelId = 364,
            baseDamage = 0f
    ),
    SPRAY_CAN(
            value = SAMPConstants.WEAPON_SPRAYCAN,
            textKey = KampCoreTextKeys.weapon.model.name.spraycan,
            modelName = "Spray can",
            slot = WeaponSlot.MISC1,
            modelId = 365,
            baseDamage = 0.33f
    ),
    FIRE_EXTINGUISHER(
            value = SAMPConstants.WEAPON_FIREEXTINGUISHER,
            textKey = KampCoreTextKeys.weapon.model.name.fireextinguisher,
            modelName = "Fire extinguisher",
            slot = WeaponSlot.MISC1,
            modelId = 366,
            baseDamage = 0.33f
    ),
    CAMERA(
            value = SAMPConstants.WEAPON_CAMERA,
            textKey = KampCoreTextKeys.weapon.model.name.camera,
            modelName = "Camera",
            slot = WeaponSlot.MISC1,
            modelId = 367,
            baseDamage = 0f
    ),
    NIGHT_VISION(
            value = SAMPConstants.WEAPON_NIGHTVISION,
            textKey = KampCoreTextKeys.weapon.model.name.nightvision,
            modelName = "Night vision",
            slot = WeaponSlot.WEARABLE,
            modelId = 368,
            baseDamage = 0f
    ),
    INFRARED(
            value = SAMPConstants.WEAPON_INFRARED,
            textKey = KampCoreTextKeys.weapon.model.name.infrared,
            modelName = "Infrared vision",
            slot = WeaponSlot.WEARABLE,
            modelId = 368,
            baseDamage = 0f
    ),
    PARACHUTE(
            value = SAMPConstants.WEAPON_PARACHUTE,
            textKey = KampCoreTextKeys.weapon.model.name.parachute,
            modelName = "Parachute",
            slot = WeaponSlot.WEARABLE,
            modelId = 371,
            baseDamage = 0f
    ),
    FAKE_PISTOL(
            value = 47,
            textKey = KampCoreTextKeys.weapon.model.name.fakepistol,
            modelName = "Fake pistol",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 0f
    ),
    VEHICLE(
            value = SAMPConstants.WEAPON_VEHICLE,
            textKey = KampCoreTextKeys.weapon.model.name.vehicle,
            modelName = "Vehicle",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 9.9f
    ),
    HELICOPTER_BLADES(
            value = 50,
            textKey = KampCoreTextKeys.weapon.model.name.helicopterblades,
            modelName = "Helicopter blades",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 330f
    ),
    EXPLOSION(
            value = 51,
            textKey = KampCoreTextKeys.weapon.model.name.explosion,
            modelName = "Explosion",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 82.5f
    ),
    DROWN(
            value = SAMPConstants.WEAPON_DROWN,
            textKey = KampCoreTextKeys.weapon.model.name.drown,
            modelName = "Drowning",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 1f
    ),
    COLLISION(
            value = SAMPConstants.WEAPON_COLLISION,
            textKey = KampCoreTextKeys.weapon.model.name.collision,
            modelName = "Collision",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 165f
    ),
    SUICIDE(
            value = 255,
            textKey = KampCoreTextKeys.weapon.model.name.suicide,
            modelName = "Suicide",
            slot = WeaponSlot.INVALID,
            modelId = 0,
            baseDamage = 1f
    );

    companion object : ConstantValueRegistry<Int, WeaponModel>(*WeaponModel.values()) {

        private val weaponModelsByName = PatriciaTrie<WeaponModel>()

        init {
            // Only index models with unique names
            WeaponModel
                    .values()
                    .groupBy { it.modelName.toLowerCase() }
                    .values
                    .filter { it.size == 1 }
                    .map { it.first() }
                    .forEach {
                        weaponModelsByName[it.modelName.toLowerCase()] = it
                    }
        }

        operator fun get(modelName: String): WeaponModel? {
            val models = weaponModelsByName.prefixMap(modelName.toLowerCase()).values
            return when {
                models.size == 1 -> models.first()
                else -> null
            }
        }
    }

}
