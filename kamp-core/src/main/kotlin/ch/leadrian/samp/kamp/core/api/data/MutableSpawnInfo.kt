package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.entity.id.TeamId

interface MutableSpawnInfo : SpawnInfo {

    override var teamId: TeamId?

    override var skinModel: ch.leadrian.samp.kamp.core.api.constants.SkinModel

    override var position: Position

    override var weapon1: WeaponData

    override var weapon2: WeaponData

    override var weapon3: WeaponData
}

fun mutableSpawnInfoOf(
        skinModel: ch.leadrian.samp.kamp.core.api.constants.SkinModel,
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