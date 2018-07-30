package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.entity.id.TeamId

interface SpawnInfo {

    val teamId: TeamId

    val skinModel: SkinModel

    val position: Position

    val weapon1: WeaponData

    val weapon2: WeaponData

    val weapon3: WeaponData

    fun toSpawnInfo(): SpawnInfo

    fun toMutableSpawnInfo(): MutableSpawnInfo
}

fun spawnInfoOf(
        teamId: TeamId,
        skinModel: SkinModel,
        position: Position,
        weapon1: WeaponData,
        weapon2: WeaponData,
        weapon3: WeaponData
): SpawnInfo = SpawnInfoImpl(
        teamId = teamId,
        skinModel = skinModel,
        position = position,
        weapon1 = weapon1,
        weapon2 = weapon2,
        weapon3 = weapon3
)
