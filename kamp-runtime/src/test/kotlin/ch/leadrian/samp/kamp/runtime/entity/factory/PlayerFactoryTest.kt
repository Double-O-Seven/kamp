package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import io.mockk.*
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

    @BeforeEach
    fun setUp() {
        every { playerRegistry.register(any()) } just Runs
        playerFactory = PlayerFactory(
                actorRegistry = actorRegistry,
                playerRegistry = playerRegistry,
                mapObjectRegistry = mapObjectRegistry,
                menuRegistry = menuRegistry,
                playerMapIconFactory = playerMapIconFactory,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
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

        player.onDisconnect(DisconnectReason.QUIT)

        verify { playerRegistry.unregister(player) }
    }

}