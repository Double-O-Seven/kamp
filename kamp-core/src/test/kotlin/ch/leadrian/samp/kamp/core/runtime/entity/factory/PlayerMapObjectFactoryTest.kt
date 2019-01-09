package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerMapObjectRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerMapObjectFactoryTest {

    private val playerId = PlayerId.valueOf(69)
    private lateinit var player: Player
    private lateinit var playerMapObjectFactory: PlayerMapObjectFactory

    private val playerMapObjectRegistry = mockk<PlayerMapObjectRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every {
            nativeFunctionExecutor.createPlayerObject(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } returns 0
        every { playerMapObjectRegistry.register(any()) } just Runs
        playerMapObjectFactory = PlayerMapObjectFactory(nativeFunctionExecutor)
        player = mockk {
            every { this@mockk.playerMapObjectRegistry } returns this@PlayerMapObjectFactoryTest.playerMapObjectRegistry
            every { id } returns playerId
        }
    }

    @Test
    fun shouldCreatePlayerMapObject() {
        playerMapObjectFactory.create(
                player = player,
                modelId = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        verify {
            nativeFunctionExecutor.createPlayerObject(
                    playerid = playerId.value,
                    modelid = 1337,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    rX = 4f,
                    rY = 5f,
                    rZ = 6f,
                    DrawDistance = 7f
            )
        }
    }

    @Test
    fun shouldRegisterPlayerMapObject() {
        val playerMapObject = playerMapObjectFactory.create(
                player = player,
                modelId = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        verify { playerMapObjectRegistry.register(playerMapObject) }
    }

    @Test
    fun shouldUnregisterPlayerMapObjectOnDestroy() {
        every { playerMapObjectRegistry.unregister(any()) } just Runs
        every { player.isConnected } returns true
        every { nativeFunctionExecutor.destroyPlayerObject(any(), any()) } returns true
        val playerMapObject = playerMapObjectFactory.create(
                player = player,
                modelId = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        playerMapObject.destroy()

        verify { playerMapObjectRegistry.unregister(playerMapObject) }
    }

}