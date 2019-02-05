package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.geodata.hmap.HeightMap
import ch.leadrian.samp.kamp.streamer.api.callback.onEnter
import ch.leadrian.samp.kamp.streamer.api.callback.onStreamIn
import ch.leadrian.samp.kamp.streamer.api.callback.onStreamOut
import ch.leadrian.samp.kamp.streamer.api.service.StreamableActorService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableCheckpointService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableMapObjectService
import ch.leadrian.samp.kamp.streamer.api.service.StreamablePickupService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableRaceCheckpointService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableTextLabelService
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StreamerCommands
@Inject
constructor(
        private val messageSender: MessageSender,
        private val vehicleService: VehicleService,
        private val streamableMapObjectService: StreamableMapObjectService,
        private val streamableTextLabelService: StreamableTextLabelService,
        private val streamableActorService: StreamableActorService,
        private val streamablePickupService: StreamablePickupService,
        private val streamableCheckpointService: StreamableCheckpointService,
        private val streamableRaceCheckpointService: StreamableRaceCheckpointService,
        private val heightMap: HeightMap
) : Commands() {

    @Unlisted
    @Command(name = "cmds", aliases = ["commands"])
    fun showCommands(player: Player) {
        showCommandList(player)
    }

    @Command
    fun labelVehicles(player: Player) {
        vehicleService.getAllVehicles().forEach { vehicle ->
            streamableTextLabelService.createStreamableTextLabel(
                    textKey = vehicle.model.textKey,
                    color = vehicle.colors.color1.color,
                    coordinates = Vector3D.ORIGIN,
                    streamDistance = 300f
            ).apply {
                attachTo(vehicle, vector3DOf(0f, 0f, 0f))
                onStreamIn {
                    messageSender.sendMessageToPlayer(
                            player,
                            vehicle.colors.color1.color,
                            "{0} label streamed in",
                            vehicle.model.textKey
                    )
                }
                onStreamOut {
                    messageSender.sendMessageToPlayer(
                            player,
                            vehicle.colors.color1.color,
                            "{0} label streamed out",
                            vehicle.model.textKey
                    )
                }
            }
        }
    }

    @Command
    @Description("Creates cows with a fixed offset at height z")
    fun createCows(player: Player, z: Float, offset: Int) {
        var numberOfObjects = 0
        for (x: Int in (-3000..3000) step offset) {
            for (y: Int in (-3000..3000) step offset) {
                streamableMapObjectService.createStreamableMapObject(
                        modelId = 16442,
                        priority = 0,
                        streamDistance = 100f,
                        coordinates = vector3DOf(x.toFloat(), y.toFloat(), z),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
                numberOfObjects++
            }
            messageSender.sendMessageToPlayer(player, Colors.GREEN, "Created $numberOfObjects objects")
        }
        messageSender.sendMessageToPlayer(player, Colors.GREEN, "Created $numberOfObjects objects")
    }

    @Command
    fun createActors(player: Player, offset: Int) {
        val random = Random(System.currentTimeMillis())
        var actorCount = 0
        (-3000..3000 step offset).map { it.toFloat() }.forEach { x ->
            (-3000..3000 step offset).map { it.toFloat() }.forEach { y ->
                val z = heightMap.findZ(x, y) + 2f
                val skinModel = SkinModel.values()[random.nextInt(SkinModel.values().size)]
                val actor = streamableActorService.createStreamableActor(
                        skinModel,
                        positionOf(x, y, z, 0f)
                )
                actor.onStreamIn { p ->
                    messageSender.sendMessageToPlayer(p, Colors.PINK, "$skinModel streamed in")
                }
                actor.onStreamOut { p ->
                    messageSender.sendMessageToPlayer(p, Colors.PINK, "$skinModel streamed out")
                }
                actorCount++
            }
        }
        messageSender.sendMessageToPlayer(player, Colors.PINK, "$actorCount actors created")
    }

    @Command
    fun createPickups(player: Player, offset: Int) {
        val random = Random(System.currentTimeMillis())
        var pickupCount = 0
        (-3000..3000 step offset).map { it.toFloat() }.forEach { x ->
            (-3000..3000 step offset).map { it.toFloat() }.forEach { y ->
                val z = heightMap.findZ(x, y) + 3.5f
                val weaponModel = WeaponModel.values()[random.nextInt(WeaponModel.values().size)]
                val pickup = streamablePickupService.createStreamablePickup(
                        weaponModel.modelId,
                        vector3DOf(x, y, z),
                        3
                )
                pickup.onStreamIn {
                    messageSender.sendMessageToAll(Colors.PINK, "$weaponModel streamed in")
                }
                pickup.onStreamOut {
                    messageSender.sendMessageToAll(Colors.PINK, "$weaponModel streamed out")
                }
                pickupCount++
            }
        }
        messageSender.sendMessageToPlayer(player, Colors.PINK, "$pickupCount pickups created")
    }

    @Command
    fun createCheckpoints(player: Player) {
        vehicleService.getAllVehicles().forEach { vehicle ->
            val checkpoint = streamableCheckpointService.createStreamableCheckpoint(vehicle.coordinates, 5f)
            checkpoint.onEnter {
                messageSender.sendMessageToPlayer(player, vehicle.colors.color1.color, "{0} spawn point", vehicle.model)
            }
        }
    }

    @Command
    fun createRaceCheckpoints(player: Player) {
        vehicleService.getAllVehicles().forEach { vehicle ->
            val raceCheckpoint = streamableRaceCheckpointService.createStreamableRaceCheckpoint(
                    vehicle.coordinates,
                    5f,
                    RaceCheckpointType.NORMAL
            )
            raceCheckpoint.onEnter {
                messageSender.sendMessageToPlayer(player, vehicle.colors.color1.color, "{0} spawn point", vehicle.model)
            }
        }
    }
}