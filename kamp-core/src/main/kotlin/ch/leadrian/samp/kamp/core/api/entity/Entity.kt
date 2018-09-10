package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.entity.id.EntityId

interface Entity<T : EntityId> {

    val id: T
}