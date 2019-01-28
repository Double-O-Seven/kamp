package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerPickUpStreamablePickupReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamablePickupStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamablePickupStreamOutReceiver

interface StreamablePickup :
        Destroyable,
        Extendable<StreamablePickup>,
        OnPlayerPickUpStreamablePickupReceiver,
        OnStreamablePickupStreamInReceiver,
        OnStreamablePickupStreamOutReceiver {

    var coordinates: Vector3D

    var modelId: Int

    var type: Int

    var virtualWorldId: Int?

    var interiorIds: MutableSet<Int>
}