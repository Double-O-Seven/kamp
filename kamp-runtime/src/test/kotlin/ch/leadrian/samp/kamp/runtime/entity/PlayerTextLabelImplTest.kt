package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.data.Colors
import ch.leadrian.samp.kamp.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerTextLabelImplTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructPlayerTextLabel() {
            val playerId = PlayerId.valueOf(65)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            val attachToPlayerId = PlayerId.valueOf(65)
            val attachToPlayer = mockk<Player> {
                every { id } returns attachToPlayerId
            }
            val attachToVehicleId = VehicleId.valueOf(65)
            val attachToVehicle = mockk<Vehicle> {
                every { id } returns attachToVehicleId
            }
            val playerTextLabelId = PlayerTextLabelId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayer3DTextLabel(
                            playerid = playerId.value,
                            text = "Hi there",
                            color = Colors.RED.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            testLOS = true,
                            DrawDistance = 50f,
                            attachedplayer = attachToPlayerId.value,
                            attachedvehicle = attachToVehicleId.value
                    )
                } returns playerTextLabelId.value
            }

            val playerTextLabel = PlayerTextLabelImpl(
                    player = player,
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    testLOS = true,
                    drawDistance = 50f,
                    attachToPlayer = attachToPlayer,
                    attachToVehicle = attachToVehicle,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(playerTextLabel.id)
                    .isEqualTo(playerTextLabelId)
        }


        @Test
        fun givenNoAttachPlayerOrVehicleItShouldConstructPlayerTextLabel() {
            val playerId = PlayerId.valueOf(65)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            val playerTextLabelId = PlayerTextLabelId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayer3DTextLabel(
                            playerid = playerId.value,
                            text = "Hi there",
                            color = Colors.RED.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            testLOS = true,
                            DrawDistance = 50f,
                            attachedplayer = SAMPConstants.INVALID_PLAYER_ID,
                            attachedvehicle = SAMPConstants.INVALID_3DTEXT_ID
                    )
                } returns playerTextLabelId.value
            }

            val playerTextLabel = PlayerTextLabelImpl(
                    player = player,
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    testLOS = true,
                    drawDistance = 50f,
                    attachToPlayer = null,
                    attachToVehicle = null,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(playerTextLabel.id)
                    .isEqualTo(playerTextLabelId)
        }

        @Test
        fun givenCreatePlayerTextLabelReturnsInvalidPlayerTextLabelIdItShouldThrowCreationFailedException() {
            val playerId = PlayerId.valueOf(65)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayer3DTextLabel(
                            playerid = playerId.value,
                            text = "Hi there",
                            color = Colors.RED.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            testLOS = true,
                            DrawDistance = 50f,
                            attachedplayer = SAMPConstants.INVALID_PLAYER_ID,
                            attachedvehicle = SAMPConstants.INVALID_3DTEXT_ID
                    )
                } returns SAMPConstants.INVALID_3DTEXT_ID
            }

            val caughtThrowable = catchThrowable {
                PlayerTextLabelImpl(
                        player = player,
                        text = "Hi there",
                        color = Colors.RED,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        testLOS = true,
                        drawDistance = 50f,
                        attachToPlayer = null,
                        attachToVehicle = null,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val playerId = PlayerId.valueOf(69)
        private val player = mockk<Player>()
        private val playerTextLabelId = PlayerTextLabelId.valueOf(50)
        private lateinit var playerTextLabel: PlayerTextLabelImpl

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every { player.id } returns playerId
            every {
                nativeFunctionExecutor.createPlayer3DTextLabel(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns playerTextLabelId.value
            playerTextLabel = PlayerTextLabelImpl(
                    player = player,
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                    testLOS = true,
                    drawDistance = 50f,
                    attachToPlayer = null,
                    attachToVehicle = null,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )
        }

        @Test
        fun shouldInitializeText() {
            val text = playerTextLabel.text

            assertThat(text)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetText() {
            every { nativeFunctionExecutor.updatePlayer3DTextLabelText(any(), any(), any(), any()) } returns true

            playerTextLabel.text = "Test"

            verify {
                nativeFunctionExecutor.updatePlayer3DTextLabelText(
                        playerid = playerId.value,
                        id = playerTextLabelId.value,
                        text = "Test",
                        color = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldInitializeColor() {
            val color = playerTextLabel.color

            assertThat(color)
                    .isEqualTo(Colors.RED)
        }

        @Test
        fun shouldSetColor() {
            every { nativeFunctionExecutor.updatePlayer3DTextLabelText(any(), any(), any(), any()) } returns true

            playerTextLabel.color = Colors.BLUE

            verify {
                nativeFunctionExecutor.updatePlayer3DTextLabelText(
                        playerid = playerId.value,
                        id = playerTextLabelId.value,
                        text = "Hi there",
                        color = Colors.BLUE.value
                )
            }
        }

        @Test
        fun shouldUpdateTextAndColor() {
            every { nativeFunctionExecutor.updatePlayer3DTextLabelText(any(), any(), any(), any()) } returns true

            playerTextLabel.update("Test", Colors.BLUE)

            verify {
                nativeFunctionExecutor.updatePlayer3DTextLabelText(
                        playerid = playerId.value,
                        id = playerTextLabelId.value,
                        text = "Test",
                        color = Colors.BLUE.value
                )
            }
        }

        @Test
        fun shouldInitializeCoordinates() {
            val coordinates = playerTextLabel.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.deletePlayer3DTextLabel(any(), any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = playerTextLabel.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyPlayerTextLabel() {
                val onDestroy = mockk<PlayerTextLabelImpl.() -> Unit>(relaxed = true)
                playerTextLabel.onDestroy(onDestroy)

                playerTextLabel.destroy()

                verifyOrder {
                    onDestroy.invoke(playerTextLabel)
                    nativeFunctionExecutor.deletePlayer3DTextLabel(
                            playerid = playerId.value,
                            id = playerTextLabelId.value
                    )
                }
                assertThat(playerTextLabel.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<PlayerTextLabelImpl.() -> Unit>(relaxed = true)
                playerTextLabel.onDestroy(onDestroy)

                playerTextLabel.destroy()
                playerTextLabel.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(playerTextLabel)
                    nativeFunctionExecutor.deletePlayer3DTextLabel(
                            playerid = playerId.value,
                            id = playerTextLabelId.value
                    )
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                playerTextLabel.destroy()

                val caughtThrowable = catchThrowable { playerTextLabel.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}
