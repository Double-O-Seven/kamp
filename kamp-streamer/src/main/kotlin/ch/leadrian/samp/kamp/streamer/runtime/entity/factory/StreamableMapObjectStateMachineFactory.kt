package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectStateMachine
import javax.inject.Inject

internal class StreamableMapObjectStateMachineFactory
@Inject
constructor(private val streamableMapObjectStateFactory: StreamableMapObjectStateFactory) {

    fun create(streamableMapObject: StreamableMapObjectImpl, coordinates: Vector3D, rotation: Vector3D): StreamableMapObjectStateMachine {
        val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
        return StreamableMapObjectStateMachine(fixedCoordinates, streamableMapObject, streamableMapObjectStateFactory)
    }
}