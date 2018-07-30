package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.entity.id.TeamId

internal data class MutableSpawnInfoImpl(
        override var teamId: TeamId,
        override var skinModel: SkinModel,
        override var position: Position,
        override var weapon1: WeaponData,
        override var weapon2: WeaponData,
        override var weapon3: WeaponData
) : MutableSpawnInfo {

    override fun toSpawnInfo(): SpawnInfo = SpawnInfoImpl(
            teamId = teamId,
            skinModel = skinModel,
            position = position,
            weapon1 = weapon1,
            weapon2 = weapon2,
            weapon3 = weapon3
    )

    override fun toMutableSpawnInfo(): MutableSpawnInfo = this
}