package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.entity.Destroyable

interface Streamable : Destroyable {

    val priority: Int

}