package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.entity.id.TeamId

internal data class SpawnInfoImpl(
        override val teamId: TeamId?,
        override val skinModel: SkinModel,
        override val position: Position,
        override val weapon1: WeaponData,
        override val weapon2: WeaponData,
        override val weapon3: WeaponData
) : SpawnInfo {

    override fun toSpawnInfo(): SpawnInfo = this

    override fun toMutableSpawnInfo(): MutableSpawnInfo = MutableSpawnInfoImpl(
            teamId = teamId,
            skinModel = skinModel,
            position = position,
            weapon1 = weapon1,
            weapon2 = weapon2,
            weapon3 = weapon3
    )
}