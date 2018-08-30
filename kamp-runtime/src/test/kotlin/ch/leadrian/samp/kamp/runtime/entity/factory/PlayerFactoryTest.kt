package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.InterceptablePlayer
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import ch.leadrian.samp.kamp.runtime.entity.interceptor.InterceptorPriority
import ch.leadrian.samp.kamp.runtime.entity.interceptor.PlayerInterceptor
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerFactoryTest {

    @Test
    fun givenNoInterceptorItShouldReturnPlayerImpl() {
        val playerId = PlayerId.valueOf(123)
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        val playerRegistry = mockk<PlayerRegistry>()
        val actorRegistry = mockk<ActorRegistry>()
        val mapObjectRegistry = mockk<MapObjectRegistry>()
        val menuRegistry = mockk<MenuRegistry>()
        val playerMapIconFactory = mockk<PlayerMapIconFactory>()
        val vehicleRegistry = mockk<VehicleRegistry>()
        val playerFactory = PlayerFactory(
                interceptors = emptySet(),
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val player = playerFactory.create(playerId)

        assertThat(player)
                .isInstanceOfSatisfying(PlayerImpl::class.java) {
                    assertThat(it.id)
                            .isEqualTo(playerId)
                }
    }

    @Test
    fun givenInterceptorsItShouldReturnInterceptablePlayer() {
        val playerId = PlayerId.valueOf(123)
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        val playerRegistry = mockk<PlayerRegistry>()
        val actorRegistry = mockk<ActorRegistry>()
        val mapObjectRegistry = mockk<MapObjectRegistry>()
        val menuRegistry = mockk<MenuRegistry>()
        val playerMapIconFactory = mockk<PlayerMapIconFactory>()
        val vehicleRegistry = mockk<VehicleRegistry>()
        val interceptors = setOf(
                DefaultPriorityInterceptor("default"),
                HighPriorityInterceptor("high"),
                LowPriorityInterceptor("low")
        )
        val playerFactory = PlayerFactory(
                interceptors = interceptors,
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val player = playerFactory.create(playerId)

        assertThat(player)
                .isInstanceOfSatisfying(PlayerWrapper::class.java) {
                    assertThat(it.interceptorName)
                            .isEqualTo("low")
                    assertThat(it.player)
                            .isInstanceOfSatisfying(PlayerWrapper::class.java) {
                                assertThat(it.interceptorName)
                                        .isEqualTo("default")
                                assertThat(it.player)
                                        .isInstanceOfSatisfying(PlayerWrapper::class.java) {
                                            assertThat(it.interceptorName)
                                                    .isEqualTo("high")
                                            assertThat(it.player)
                                                    .isInstanceOfSatisfying(PlayerImpl::class.java) {
                                                        assertThat(it.id)
                                                                .isEqualTo(playerId)
                                                    }
                                        }
                            }
                }
    }

    @InterceptorPriority(-5)
    private class LowPriorityInterceptor(val name: String) : PlayerInterceptor {

        override fun intercept(interceptablePlayer: InterceptablePlayer): InterceptablePlayer {
            return PlayerWrapper(name, interceptablePlayer)
        }

    }

    @InterceptorPriority(10)
    private class HighPriorityInterceptor(val name: String) : PlayerInterceptor {

        override fun intercept(interceptablePlayer: InterceptablePlayer): InterceptablePlayer {
            return PlayerWrapper(name, interceptablePlayer)
        }

    }

    private class DefaultPriorityInterceptor(val name: String) : PlayerInterceptor {

        override fun intercept(interceptablePlayer: InterceptablePlayer): InterceptablePlayer {
            return PlayerWrapper(name, interceptablePlayer)
        }

    }

    private class PlayerWrapper(val interceptorName: String, val player: InterceptablePlayer) : InterceptablePlayer by player


}