package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId

interface PlayerClass : Entity<PlayerClassId> {

    override val id: PlayerClassId

    val spawnInfo: SpawnInfo
}