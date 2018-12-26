package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject

internal class PlayerFactory
@Inject
constructor(
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler,
        private val playerExtensionFactories: Set<@JvmSuppressWildcards EntityExtensionFactory<Player, *>>
) {

    fun create(playerId: PlayerId): Player {
        val player = Player(
                id = playerId,
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                vehicleRegistry = vehicleRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                nativeFunctionExecutor = nativeFunctionExecutor,
                onPlayerNameChangeHandler = onPlayerNameChangeHandler
        )
        playerRegistry.register(player)
        playerExtensionFactories.forEach { player.extensions.install(it) }
        return player
    }
}