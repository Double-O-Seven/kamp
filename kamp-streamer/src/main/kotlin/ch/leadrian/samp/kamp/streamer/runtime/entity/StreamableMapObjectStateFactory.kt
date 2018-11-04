package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider
import javax.inject.Inject

internal class StreamableMapObjectStateFactory
@Inject
constructor(private val timeProvider: TimeProvider, private val timerExecutor: TimerExecutor) {

    fun createFixedCoordinates(coordinates: Vector3D, rotation: Vector3D): StreamableMapObjectState.FixedCoordinates =
            StreamableMapObjectState.FixedCoordinates(
                    coordinates = coordinates,
                    rotation = rotation
            )

    fun createMoving(
            origin: Vector3D,
            destination: Vector3D,
            startRotation: Vector3D,
            targetRotation: Vector3D?,
            speed: Float,
            onMoved: () -> Unit
    ): StreamableMapObjectState.Moving =
            StreamableMapObjectState.Moving(
                    onMoved = onMoved,
                    origin = origin,
                    destination = destination,
                    startRotation = startRotation,
                    targetRotation = targetRotation,
                    speed = speed,
                    timeProvider = timeProvider,
                    timerExecutor = timerExecutor
            )

    fun createAttachedToVehicle(
            vehicle: Vehicle,
            offset: Vector3D,
            attachRotation: Vector3D
    ): StreamableMapObjectState.Attached.ToVehicle =
            StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = offset,
                    attachRotation = attachRotation
            )

    fun createAttachedToPlayer(
            player: Player,
            offset: Vector3D,
            attachRotation: Vector3D
    ): StreamableMapObjectState.Attached.ToPlayer =
            StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = offset,
                    attachRotation = attachRotation
            )

}