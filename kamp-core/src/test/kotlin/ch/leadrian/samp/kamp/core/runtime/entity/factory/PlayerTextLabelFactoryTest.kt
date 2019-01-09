package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextLabelRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerTextLabelFactoryTest {

    private val attachToPlayerId = PlayerId.valueOf(50)
    private val attachToPlayer = mockk<Player>()
    private val attachToVehicleId = VehicleId.valueOf(187)
    private val attachToVehicle = mockk<Vehicle>()
    private val playerId = PlayerId.valueOf(69)
    private lateinit var player: Player
    private lateinit var playerTextLabelFactory: PlayerTextLabelFactory

    private val playerTextLabelRegistry = mockk<PlayerTextLabelRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { attachToPlayer.id } returns attachToPlayerId
        every { attachToVehicle.id } returns attachToVehicleId
        every {
            nativeFunctionExecutor
                    .createPlayer3DTextLabel(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns 0
        every { playerTextLabelRegistry.register(any()) } just Runs
        playerTextLabelFactory = PlayerTextLabelFactory(nativeFunctionExecutor)
        player = mockk {
            every { this@mockk.playerTextLabelRegistry } returns this@PlayerTextLabelFactoryTest.playerTextLabelRegistry
            every { id } returns playerId
        }
    }

    @Test
    fun shouldCreatePlayerTextLabel() {
        playerTextLabelFactory.create(
                player = player,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                attachToPlayer = attachToPlayer,
                attachToVehicle = attachToVehicle
        )

        verify {
            nativeFunctionExecutor.createPlayer3DTextLabel(
                    playerid = playerId.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    text = "Test",
                    testLOS = true,
                    color = Colors.RED.value,
                    DrawDistance = 4f,
                    attachedplayer = attachToPlayerId.value,
                    attachedvehicle = attachToVehicleId.value
            )
        }
    }

    @Test
    fun shouldRegisterPlayerTextLabel() {
        val playerTextLabel = playerTextLabelFactory.create(
                player = player,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                attachToPlayer = attachToPlayer,
                attachToVehicle = attachToVehicle
        )

        verify { playerTextLabelRegistry.register(playerTextLabel) }
    }

    @Test
    fun shouldUnregisterPlayerTextLabelOnDestroy() {
        every { playerTextLabelRegistry.unregister(any()) } just Runs
        every { player.isConnected } returns true
        every { nativeFunctionExecutor.deletePlayer3DTextLabel(any(), any()) } returns true
        val playerTextLabel = playerTextLabelFactory.create(
                player = player,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                attachToPlayer = attachToPlayer,
                attachToVehicle = attachToVehicle
        )

        playerTextLabel.destroy()

        verify { playerTextLabelRegistry.unregister(playerTextLabel) }
    }

}