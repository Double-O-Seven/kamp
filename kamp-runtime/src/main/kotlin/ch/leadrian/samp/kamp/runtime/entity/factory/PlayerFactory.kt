package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerFactory
@Inject
constructor(
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(playerId: PlayerId): PlayerImpl {
        val player = PlayerImpl(
                id = playerId,
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                vehicleRegistry = vehicleRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        playerRegistry.register(player)
        return player
    }
}