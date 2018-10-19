package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextDrawRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerTextDrawFactoryTest {

    private val playerId = PlayerId.valueOf(69)
    private lateinit var player: Player
    private lateinit var playerTextDrawFactory: PlayerTextDrawFactory

    private val playerTextDrawRegistry = mockk<PlayerTextDrawRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.createPlayerTextDraw(any(), any(), any(), any()) } returns 0
        every { playerTextDrawRegistry.register(any()) } just Runs
        every { playerTextDrawRegistry.unregister(any()) } just Runs
        playerTextDrawFactory = PlayerTextDrawFactory(nativeFunctionExecutor, textProvider, textFormatter)
        player = mockk {
            every { this@mockk.playerTextDrawRegistry } returns this@PlayerTextDrawFactoryTest.playerTextDrawRegistry
            every { id } returns playerId
        }
    }

    @Test
    fun shouldCreatePlayerTextDraw() {
        playerTextDrawFactory.create(
                player = player,
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test"
        )

        verify {
            nativeFunctionExecutor.createPlayerTextDraw(
                    playerid = playerId.value,
                    x = 1f,
                    y = 2f,
                    text = "Test"
            )
        }
    }

    @Test
    fun shouldRegisterPlayerTextDraw() {
        val playerTextDraw = playerTextDrawFactory.create(
                player = player,
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test"
        )

        verify { playerTextDrawRegistry.register(playerTextDraw) }
    }

    @Test
    fun shouldUnregisterPlayerTextDrawOnDestroy() {
        every { player.isConnected } returns true
        every { nativeFunctionExecutor.playerTextDrawDestroy(any(), any()) } returns true
        val playerTextDraw = playerTextDrawFactory.create(
                player = player,
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test"
        )
        val onDestroy = mockk<PlayerTextDraw.() -> Unit>(relaxed = true)
        playerTextDraw.onDestroy(onDestroy)

        playerTextDraw.destroy()

        verify { onDestroy.invoke(playerTextDraw) }
    }

}