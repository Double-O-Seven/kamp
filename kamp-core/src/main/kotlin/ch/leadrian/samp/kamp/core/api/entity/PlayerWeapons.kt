package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponSkill
import ch.leadrian.samp.kamp.core.api.constants.WeaponSlot
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.core.api.data.WeaponData
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt

class PlayerWeapons
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    var armed: WeaponModel
        get() = nativeFunctionExecutor.getPlayerWeapon(player.id.value).let { WeaponModel[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerArmedWeapon(playerid = player.id.value, weaponid = value.value)
        }

    val state: WeaponState
        get() = nativeFunctionExecutor.getPlayerWeaponState(player.id.value).let { WeaponState[it] }

    val ammo: Int
        get() = nativeFunctionExecutor.getPlayerAmmo(player.id.value)

    fun setAmmo(model: WeaponModel, ammo: Int) {
        nativeFunctionExecutor.setPlayerAmmo(playerid = player.id.value, weaponid = model.value, ammo = ammo)
    }

    fun give(model: WeaponModel, ammo: Int) {
        nativeFunctionExecutor.givePlayerWeapon(playerid = player.id.value, weaponid = model.value, ammo = ammo)
    }

    fun reset() {
        nativeFunctionExecutor.resetPlayerWeapons(player.id.value)
    }

    fun setSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctionExecutor.setPlayerSkillLevel(playerid = player.id.value, skill = skill.value, level = level)
    }

    operator fun get(slot: WeaponSlot): WeaponData {
        val weapon = ReferenceInt()
        val ammo = ReferenceInt()
        nativeFunctionExecutor.getPlayerWeaponData(
                playerid = player.id.value,
                slot = slot.value,
                weapon = weapon,
                ammo = ammo
        )
        return weaponDataOf(model = WeaponModel[weapon.value], ammo = ammo.value)
    }

}