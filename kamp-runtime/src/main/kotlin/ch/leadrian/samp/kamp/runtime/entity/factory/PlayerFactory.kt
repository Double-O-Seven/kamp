package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.InterceptablePlayer
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import ch.leadrian.samp.kamp.runtime.entity.interceptor.PlayerInterceptor
import ch.leadrian.samp.kamp.runtime.entity.interceptor.interceptorPriority
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerFactory
@Inject
constructor(
        interceptors: Set<PlayerInterceptor>,
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    private val interceptors: List<PlayerInterceptor> = interceptors
            .sortedByDescending { it.interceptorPriority }
            .toList()

    fun create(playerId: PlayerId): InterceptablePlayer {
        var player: InterceptablePlayer = PlayerImpl(
                id = playerId,
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                vehicleRegistry = vehicleRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        interceptors.forEach {
            player = it.intercept(player)
        }

        return player
    }
}