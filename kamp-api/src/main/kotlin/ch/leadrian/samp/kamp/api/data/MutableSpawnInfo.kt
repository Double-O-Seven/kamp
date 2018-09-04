package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.entity.id.TeamId

interface MutableSpawnInfo : SpawnInfo {

    override var teamId: TeamId?

    override var skinModel: SkinModel

    override var position: Position

    override var weapon1: WeaponData

    override var weapon2: WeaponData

    override var weapon3: WeaponData
}

fun mutableSpawnInfoOf(
        skinModel: SkinModel,
        position: Position,
        weapon1: WeaponData,
        weapon2: WeaponData,
        weapon3: WeaponData,
        teamId: TeamId? = null
): MutableSpawnInfo = MutableSpawnInfoImpl(
        teamId = teamId,
        skinModel = skinModel,
        position = position,
        weapon1 = weapon1,
        weapon2 = weapon2,
        weapon3 = weapon3
)