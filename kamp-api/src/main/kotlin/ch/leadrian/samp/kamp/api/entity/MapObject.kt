package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.MapObjectId

interface MapObject : Destroyable {

    val id: MapObjectId
}