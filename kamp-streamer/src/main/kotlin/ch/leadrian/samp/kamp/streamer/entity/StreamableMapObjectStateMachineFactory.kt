package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import javax.inject.Inject

internal class StreamableMapObjectStateMachineFactory
@Inject
constructor(private val streamableMapObjectStateFactory: StreamableMapObjectStateFactory) {

    fun create(streamableMapObject: StreamableMapObject, coordinates: Vector3D, rotation: Vector3D): StreamableMapObjectStateMachine {
        val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
        return StreamableMapObjectStateMachine(fixedCoordinates, streamableMapObject, streamableMapObjectStateFactory)
    }
}