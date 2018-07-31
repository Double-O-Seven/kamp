package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.ActorId

interface Actor : Destroyable {

    val id: ActorId
}