package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService

internal sealed class StreamableTextLabelState(protected val streamableTextLabelImpl: StreamableTextLabelImpl) {

    abstract val coordinates: Vector3D

    abstract fun createPlayerTextLabel(player: Player): PlayerTextLabel

    internal class FixedCoordinates(
            streamableTextLabelImpl: StreamableTextLabelImpl,
            coordinates: Vector3D,
            private val playerTextLabelService: PlayerTextLabelService
    ) : StreamableTextLabelState(streamableTextLabelImpl) {

        override val coordinates = coordinates.toVector3D()

        override fun createPlayerTextLabel(player: Player): PlayerTextLabel =
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = streamableTextLabelImpl.getText(player),
                        color = streamableTextLabelImpl.color,
                        coordinates = coordinates,
                        drawDistance = streamableTextLabelImpl.streamDistance,
                        testLOS = streamableTextLabelImpl.testLOS
                )

    }

    internal class AttachedToVehicle(
            streamableTextLabelImpl: StreamableTextLabelImpl,
            private val attachedToVehicle: Vehicle,
            offset: Vector3D,
            private val playerTextLabelService: PlayerTextLabelService,
            private val asyncExecutor: AsyncExecutor
    ) : StreamableTextLabelState(streamableTextLabelImpl) {

        private val offset = offset.toVector3D()

        override val coordinates: Vector3D
            get() = asyncExecutor.computeOnMainThread { attachedToVehicle.coordinates }.get()

        override fun createPlayerTextLabel(player: Player): PlayerTextLabel =
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = streamableTextLabelImpl.getText(player),
                        color = streamableTextLabelImpl.color,
                        coordinates = offset,
                        drawDistance = streamableTextLabelImpl.streamDistance,
                        testLOS = streamableTextLabelImpl.testLOS,
                        attachedToVehicle = attachedToVehicle
                )

    }

    internal class AttachedToPlayer(
            streamableTextLabelImpl: StreamableTextLabelImpl,
            private val attachedToPlayer: Player,
            offset: Vector3D,
            private val playerTextLabelService: PlayerTextLabelService,
            private val asyncExecutor: AsyncExecutor
    ) : StreamableTextLabelState(streamableTextLabelImpl) {

        private val offset = offset.toVector3D()

        override val coordinates: Vector3D
            get() = asyncExecutor.computeOnMainThread { attachedToPlayer.coordinates }.get()

        override fun createPlayerTextLabel(player: Player): PlayerTextLabel =
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = streamableTextLabelImpl.getText(player),
                        color = streamableTextLabelImpl.color,
                        coordinates = offset,
                        drawDistance = streamableTextLabelImpl.streamDistance,
                        testLOS = streamableTextLabelImpl.testLOS,
                        attachedToPlayer = attachedToPlayer
                )

    }

}