package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TextLabelTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructTextLabel() {
            val textLabelId = TextLabelId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    create3DTextLabel(
                            text = "Hi there",
                            color = Colors.RED.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            testLOS = true,
                            DrawDistance = 50f,
                            virtualworld = 1337
                    )
                } returns textLabelId.value
            }

            val textLabel = TextLabel(
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    testLOS = true,
                    drawDistance = 50f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    virtualWorldId = 1337
            )

            assertThat(textLabel.id)
                    .isEqualTo(textLabelId)
        }

        @Test
        fun givenCreateTextLabelReturnsInvalidTextLabelIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    create3DTextLabel(
                            text = "Hi there",
                            color = Colors.RED.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            testLOS = true,
                            DrawDistance = 50f,
                            virtualworld = 1337
                    )
                } returns SAMPConstants.INVALID_3DTEXT_ID
            }

            val caughtThrowable = catchThrowable {
                TextLabel(
                        text = "Hi there",
                        color = Colors.RED,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        testLOS = true,
                        drawDistance = 50f,
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        virtualWorldId = 1337
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val textLabelId = TextLabelId.valueOf(50)
        private lateinit var textLabel: TextLabel

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.create3DTextLabel(any(), any(), any(), any(), any(), any(), any(), any())
            } returns textLabelId.value
            textLabel = TextLabel(
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                    testLOS = true,
                    drawDistance = 50f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    virtualWorldId = 1337
            )
        }

        @Test
        fun shouldInitializeText() {
            val text = textLabel.text

            assertThat(text)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetText() {
            every { nativeFunctionExecutor.update3DTextLabelText(any(), any(), any()) } returns true

            textLabel.text = "Test"

            verify {
                nativeFunctionExecutor.update3DTextLabelText(
                        id = textLabelId.value,
                        text = "Test",
                        color = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldInitializeColor() {
            val color = textLabel.color

            assertThat(color)
                    .isEqualTo(Colors.RED)
        }

        @Test
        fun shouldSetColor() {
            every { nativeFunctionExecutor.update3DTextLabelText(any(), any(), any()) } returns true

            textLabel.color = Colors.BLUE

            verify {
                nativeFunctionExecutor.update3DTextLabelText(
                        id = textLabelId.value,
                        text = "Hi there",
                        color = Colors.BLUE.value
                )
            }
        }

        @Test
        fun shouldUpdateTextAndColor() {
            every { nativeFunctionExecutor.update3DTextLabelText(any(), any(), any()) } returns true

            textLabel.update("Test", Colors.BLUE)

            verify {
                nativeFunctionExecutor.update3DTextLabelText(
                        id = textLabelId.value,
                        text = "Test",
                        color = Colors.BLUE.value
                )
            }
        }

        @Test
        fun shouldInitializeCoordinates() {
            val coordinates = textLabel.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldAttachToPlayer() {
            val playerId = PlayerId.valueOf(69)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every { nativeFunctionExecutor.attach3DTextLabelToPlayer(any(), any(), any(), any(), any()) } returns true

            textLabel.attachTo(player, vector3DOf(x = 1f, y = 2f, z = 3f))

            verify {
                nativeFunctionExecutor.attach3DTextLabelToPlayer(
                        id = textLabelId.value,
                        playerid = playerId.value,
                        OffsetX = 1f,
                        OffsetY = 2f,
                        OffsetZ = 3f
                )
            }
        }

        @Test
        fun shouldAttachToVehicle() {
            val vehicleId = VehicleId.valueOf(69)
            val vehicle = mockk<Vehicle> {
                every { id } returns vehicleId
            }
            every { nativeFunctionExecutor.attach3DTextLabelToVehicle(any(), any(), any(), any(), any()) } returns true

            textLabel.attachTo(vehicle, vector3DOf(x = 1f, y = 2f, z = 3f))

            verify {
                nativeFunctionExecutor.attach3DTextLabelToVehicle(
                        id = textLabelId.value,
                        vehicleid = vehicleId.value,
                        OffsetX = 1f,
                        OffsetY = 2f,
                        OffsetZ = 3f
                )
            }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.delete3DTextLabel(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = textLabel.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyTextLabel() {
                val onDestroy = mockk<TextLabel.() -> Unit>(relaxed = true)
                textLabel.onDestroy(onDestroy)

                textLabel.destroy()

                verifyOrder {
                    nativeFunctionExecutor.delete3DTextLabel(textLabelId.value)
                    onDestroy.invoke(textLabel)
                }
                assertThat(textLabel.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<TextLabel.() -> Unit>(relaxed = true)
                textLabel.onDestroy(onDestroy)

                textLabel.destroy()
                textLabel.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(textLabel)
                    nativeFunctionExecutor.delete3DTextLabel(textLabelId.value)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                textLabel.destroy()

                val caughtThrowable = catchThrowable { textLabel.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}
