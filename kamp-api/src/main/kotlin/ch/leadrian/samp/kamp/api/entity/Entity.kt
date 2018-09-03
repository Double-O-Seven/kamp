package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.EntityId

interface Entity<T : EntityId> {

    val id: T
}