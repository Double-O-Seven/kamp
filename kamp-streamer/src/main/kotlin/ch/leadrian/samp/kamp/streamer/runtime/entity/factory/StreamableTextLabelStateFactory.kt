package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelState
import javax.inject.Inject

internal class StreamableTextLabelStateFactory
@Inject
constructor(
        private val playerTextLabelService: PlayerTextLabelService,
        private val asyncExecutor: AsyncExecutor
) {

    fun createFixedCoordinates(
            streamableTextLabel: StreamableTextLabelImpl,
            coordinates: Vector3D
    ): StreamableTextLabelState.FixedCoordinates =
            StreamableTextLabelState.FixedCoordinates(streamableTextLabel, coordinates, playerTextLabelService)

    fun createAttachedToVehicle(
            streamableTextLabel: StreamableTextLabelImpl,
            offset: Vector3D,
            vehicle: Vehicle
    ): StreamableTextLabelState.AttachedToVehicle =
            StreamableTextLabelState.AttachedToVehicle(
                    streamableTextLabel,
                    vehicle,
                    offset,
                    playerTextLabelService,
                    asyncExecutor
            )

    fun createAttachedToPlayer(
            streamableTextLabel: StreamableTextLabelImpl,
            offset: Vector3D,
            Player: Player
    ): StreamableTextLabelState.AttachedToPlayer =
            StreamableTextLabelState.AttachedToPlayer(
                    streamableTextLabel,
                    Player,
                    offset,
                    playerTextLabelService,
                    asyncExecutor
            )

}
