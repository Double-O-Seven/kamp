package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject

internal class EntityResolverImpl
@Inject
constructor(
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val actorRegistry: ActorRegistry,
        private val playerClassRegistry: PlayerClassRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val pickupRegistry: PickupRegistry,
        private val textDrawRegistry: TextDrawRegistry
) : EntityResolver {

    override fun Int.toPlayer(): Player =
            playerRegistry[this] ?: throw IllegalArgumentException("Invalid player ID $this")

    override fun Int.toPlayerOrNull(): Player? = playerRegistry[this]

    override fun Int.toPlayerClass(): PlayerClass =
            playerClassRegistry[this] ?: throw IllegalArgumentException("Invalid player class ID $this")

    override fun Int.toVehicle(): Vehicle =
            vehicleRegistry[this] ?: throw IllegalArgumentException("Invalid vehicle ID $this")

    override fun Int.toMapObject(): MapObject =
            mapObjectRegistry[this] ?: throw IllegalArgumentException("Invalid map object ID $this")

    override fun Int.toPlayerMapObject(player: Player): PlayerMapObject =
            player.playerMapObjectRegistry[this]
                    ?: throw IllegalArgumentException("Invalid player map object ID $this for player ID ${player.id.value}")

    override fun Int.toPickup(): Pickup =
            pickupRegistry[this] ?: throw IllegalArgumentException("Invalid pickup ID $this")

    override fun Int.toActor(): Actor =
            actorRegistry[this] ?: throw IllegalArgumentException("Invalid actor ID $this")

    override fun Int.toTextDrawOrNull(): TextDraw? = textDrawRegistry[this]

    override fun Int.toPlayerTextDraw(player: Player): PlayerTextDraw =
            player.playerTextDrawRegistry[this]
                    ?: throw IllegalArgumentException("Invalid player text draw ID $this for player ID ${player.id.value}")

}