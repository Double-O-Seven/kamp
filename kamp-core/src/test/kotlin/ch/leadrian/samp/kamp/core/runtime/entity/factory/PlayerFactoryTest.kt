package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason.QUIT
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerFactoryTest {

    private val playerId = PlayerId.valueOf(123)
    private lateinit var playerFactory: PlayerFactory

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val actorRegistry = mockk<ActorRegistry>()
    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val menuRegistry = mockk<MenuRegistry>()
    private val playerMapIconFactory = mockk<PlayerMapIconFactory>()
    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val playerExtensionFactory = mockk<EntityExtensionFactory<Player, FooExtension>>()
    private val fooExtension = FooExtension()

    @BeforeEach
    fun setUp() {
        every { playerRegistry.register(any()) } just Runs
        every { playerExtensionFactory.create(any()) } returns fooExtension
        every { playerExtensionFactory.extensionClass } returns FooExtension::class
        playerFactory = PlayerFactory(
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerExtensionFactories = setOf(playerExtensionFactory)
        )
    }

    @Test
    fun shouldReturnPlayer() {
        val player = playerFactory.create(playerId)

        assertThat(player.id)
                .isEqualTo(playerId)
    }

    @Test
    fun shouldRegisterPlayer() {
        val player = playerFactory.create(playerId)

        verify { playerRegistry.register(player) }
    }

    @Test
    fun shouldUnregisterPlayerOnDisconnect() {
        every { playerRegistry.unregister(any()) } just Runs
        val player = playerFactory.create(playerId)

        player.onDisconnect(QUIT)

        verify { playerRegistry.unregister(player) }
    }

    @Test
    fun shouldInstallExtension() {
        val player = playerFactory.create(playerId)

        verify { playerExtensionFactory.create(player) }
        assertThat(player.extensions[FooExtension::class])
                .isEqualTo(fooExtension)
    }

    private class FooExtension

}