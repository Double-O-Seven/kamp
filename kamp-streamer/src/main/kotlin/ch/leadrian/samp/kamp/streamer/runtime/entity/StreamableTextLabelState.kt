package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.HasVehicle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService

internal sealed class StreamableTextLabelState(protected val streamableTextLabel: StreamableTextLabelImpl) {

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
                        text = streamableTextLabel.getText(player.locale),
                        color = streamableTextLabel.color,
                        coordinates = coordinates,
                        drawDistance = streamableTextLabel.streamDistance,
                        testLOS = streamableTextLabel.testLOS
                )

    }

    internal class AttachedToVehicle(
            streamableTextLabelImpl: StreamableTextLabelImpl,
            override val vehicle: Vehicle,
            offset: Vector3D,
            private val playerTextLabelService: PlayerTextLabelService,
            private val asyncExecutor: AsyncExecutor
    ) : StreamableTextLabelState(streamableTextLabelImpl), HasVehicle {

        val offset = offset.toVector3D()

        override val coordinates: Vector3D
            get() = asyncExecutor.computeOnMainThread { vehicle.coordinates }.get()

        override fun createPlayerTextLabel(player: Player): PlayerTextLabel =
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = streamableTextLabel.getText(player.locale),
                        color = streamableTextLabel.color,
                        coordinates = offset,
                        drawDistance = streamableTextLabel.streamDistance,
                        testLOS = streamableTextLabel.testLOS,
                        attachedToVehicle = vehicle
                )

    }

    internal class AttachedToPlayer(
            streamableTextLabelImpl: StreamableTextLabelImpl,
            override val player: Player,
            offset: Vector3D,
            private val playerTextLabelService: PlayerTextLabelService,
            private val asyncExecutor: AsyncExecutor
    ) : StreamableTextLabelState(streamableTextLabelImpl), HasPlayer {

        val offset = offset.toVector3D()

        override val coordinates: Vector3D
            get() = asyncExecutor.computeOnMainThread { player.coordinates }.get()

        override fun createPlayerTextLabel(player: Player): PlayerTextLabel =
                playerTextLabelService.createPlayerTextLabel(
                        player = player,
                        text = streamableTextLabel.getText(player.locale),
                        color = streamableTextLabel.color,
                        coordinates = offset,
                        drawDistance = streamableTextLabel.streamDistance,
                        testLOS = streamableTextLabel.testLOS,
                        attachedToPlayer = this.player
                )

    }

}