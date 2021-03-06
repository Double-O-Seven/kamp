package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.runtime.data.SpawnInfoImpl

interface SpawnInfo {

    val teamId: TeamId?

    val skinModel: SkinModel

    val position: Position

    val weapon1: WeaponData

    val weapon2: WeaponData

    val weapon3: WeaponData

    fun toSpawnInfo(): SpawnInfo

    fun toMutableSpawnInfo(): MutableSpawnInfo
}

fun spawnInfoOf(
        skinModel: SkinModel,
        position: Position,
        weapon1: WeaponData,
        weapon2: WeaponData,
        weapon3: WeaponData,
        teamId: TeamId? = null
): SpawnInfo = SpawnInfoImpl(
        teamId = teamId,
        skinModel = skinModel,
        position = position.toPosition(),
        weapon1 = weapon1.toWeaponData(),
        weapon2 = weapon2.toWeaponData(),
        weapon3 = weapon3.toWeaponData()
)
