package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class PlayerClass
internal constructor(
        spawnInfo: SpawnInfo,
        nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<PlayerClassId> {

    override val id: PlayerClassId

    val spawnInfo: SpawnInfo = spawnInfo.toSpawnInfo()

    init {
        val playerClassId = with(spawnInfo) {
            val teamId = this.teamId
            when (teamId) {
                null -> nativeFunctionExecutor.addPlayerClass(
                        modelid = skinModel.value,
                        spawn_x = position.x,
                        spawn_y = position.y,
                        spawn_z = position.z,
                        z_angle = position.angle,
                        weapon1 = weapon1.model.value,
                        weapon1_ammo = weapon1.ammo,
                        weapon2 = weapon2.model.value,
                        weapon2_ammo = weapon2.ammo,
                        weapon3 = weapon3.model.value,
                        weapon3_ammo = weapon3.ammo
                )
                else -> nativeFunctionExecutor.addPlayerClassEx(
                        modelid = skinModel.value,
                        spawn_x = position.x,
                        spawn_y = position.y,
                        spawn_z = position.z,
                        z_angle = position.angle,
                        weapon1 = weapon1.model.value,
                        weapon1_ammo = weapon1.ammo,
                        weapon2 = weapon2.model.value,
                        weapon2_ammo = weapon2.ammo,
                        weapon3 = weapon3.model.value,
                        weapon3_ammo = weapon3.ammo,
                        teamid = teamId.value
                )
            }
        }
        id = PlayerClassId.valueOf(playerClassId)
    }
}
