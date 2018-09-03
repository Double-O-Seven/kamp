package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.SpawnInfo
import ch.leadrian.samp.kamp.api.entity.id.PlayerClassId

interface PlayerClass : Entity<PlayerClassId> {

    override val id: PlayerClassId

    val spawnInfo: SpawnInfo
}