package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.mutableVector2DOf
import ch.leadrian.samp.kamp.core.api.data.mutableVehicleColorsOf
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerClickPlayerTextDrawReceiverDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

internal class PlayerTextDrawTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructPlayerTextDraw() {
            val playerId = PlayerId.valueOf(13)
            val player = mockk<Player> {
                every { id } returns playerId
                every { isConnected } returns true
            }
            val playerTextDrawId = PlayerTextDrawId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayerTextDraw(playerid = playerId.value, x = 1f, y = 2f, text = "test")
                } returns playerTextDrawId.value
            }

            val playerTextDraw = PlayerTextDraw(
                    text = "test",
                    position = vector2DOf(x = 1f, y = 2f),
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = mockk(),
                    textProvider = mockk()
            )

            assertThat(playerTextDraw.id)
                    .isEqualTo(playerTextDrawId)
        }

        @Test
        fun givenCreatePlayerTextDrawReturnsInvalidPlayerTextDrawIdItShouldThrowCreationFailedException() {
            val playerId = PlayerId.valueOf(13)
            val player = mockk<Player> {
                every { id } returns playerId
                every { isConnected } returns true
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPlayerTextDraw(playerid = playerId.value, x = 1f, y = 2f, text = "test")
                } returns SAMPConstants.INVALID_TEXT_DRAW
            }

            val caughtThrowable = catchThrowable {
                PlayerTextDraw(
                        text = "test",
                        position = vector2DOf(x = 1f, y = 2f),
                        player = player,
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        textFormatter = mockk(),
                        textProvider = mockk()
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val playerId = PlayerId.valueOf(13)
        private val player = mockk<Player>()
        private val playerTextDrawId = PlayerTextDrawId.valueOf(69)
        private lateinit var playerTextDraw: PlayerTextDraw
        private val onPlayerClickPlayerTextDrawReceiver = mockk<OnPlayerClickPlayerTextDrawReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        private val textFormatter = mockk<TextFormatter>()
        private val textProvider = mockk<TextProvider>()

        @BeforeEach
        fun setUp() {
            every { player.id } returns playerId
            every { player.isConnected } returns true
            every {
                nativeFunctionExecutor.createPlayerTextDraw(any(), any(), any(), any())
            } returns playerTextDrawId.value
            playerTextDraw = PlayerTextDraw(
                    text = "test",
                    position = mutableVector2DOf(x = 1f, y = 2f),
                    player = player,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    textProvider = textProvider,
                    onPlayerClickPlayerTextDrawReceiver = onPlayerClickPlayerTextDrawReceiver
            )
        }

        @Test
        fun shouldSetPosition() {
            val position = playerTextDraw.position

            assertThat(position)
                    .isEqualTo(vector2DOf(x = 1f, y = 2f))
        }

        @Nested
        inner class LetterSizeTests {

            @Test
            fun shouldInitializeLetterSize() {
                val letterSize = playerTextDraw.letterSize

                assertThat(letterSize)
                        .isEqualTo(vector2DOf(x = 1f, y = 1f))
            }

            @Test
            fun shouldSetLetterSize() {
                every { nativeFunctionExecutor.playerTextDrawLetterSize(any(), any(), any(), any()) } returns true

                playerTextDraw.letterSize = mutableVector2DOf(x = 13f, y = 3f)

                verify {
                    nativeFunctionExecutor.playerTextDrawLetterSize(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            x = 13f,
                            y = 3f
                    )
                }
                assertThat(playerTextDraw.letterSize)
                        .isEqualTo(vector2DOf(x = 13f, y = 3f))
            }
        }

        @Nested
        inner class TextSizeTests {

            @Test
            fun shouldInitializeTextSize() {
                val textSize = playerTextDraw.textSize

                assertThat(textSize)
                        .isEqualTo(vector2DOf(x = 0f, y = 0f))
            }

            @Test
            fun shouldSetTextSize() {
                every { nativeFunctionExecutor.playerTextDrawTextSize(any(), any(), any(), any()) } returns true

                playerTextDraw.textSize = mutableVector2DOf(x = 13f, y = 3f)

                verify {
                    nativeFunctionExecutor.playerTextDrawTextSize(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            x = 13f,
                            y = 3f
                    )
                }
                assertThat(playerTextDraw.textSize)
                        .isEqualTo(vector2DOf(x = 13f, y = 3f))
            }
        }

        @Nested
        inner class AlignmentTests {

            @Test
            fun shouldInitializeAlignment() {
                val alignment = playerTextDraw.alignment

                assertThat(alignment)
                        .isEqualTo(TextDrawAlignment.LEFT)
            }

            @Test
            fun shouldSetAlignment() {
                every { nativeFunctionExecutor.playerTextDrawAlignment(any(), any(), any()) } returns true

                playerTextDraw.alignment = TextDrawAlignment.CENTERED

                verify {
                    nativeFunctionExecutor.playerTextDrawAlignment(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            alignment = TextDrawAlignment.CENTERED.value
                    )
                }
                assertThat(playerTextDraw.alignment)
                        .isEqualTo(TextDrawAlignment.CENTERED)
            }
        }

        @Nested
        inner class ColorTests {

            @Test
            fun shouldInitializeColor() {
                val color = playerTextDraw.color

                assertThat(color)
                        .isEqualTo(Colors.WHITE)
            }

            @Test
            fun shouldSetColor() {
                every { nativeFunctionExecutor.playerTextDrawColor(any(), any(), any()) } returns true

                playerTextDraw.color = Colors.RED

                verify {
                    nativeFunctionExecutor.playerTextDrawColor(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(playerTextDraw.color)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class UseBoxTests {

            @Test
            fun shouldInitializeUseBox() {
                val useBox = playerTextDraw.useBox

                assertThat(useBox)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetUseBox(useBox: Boolean) {
                every { nativeFunctionExecutor.playerTextDrawUseBox(any(), any(), any()) } returns true

                playerTextDraw.useBox = useBox

                verify {
                    nativeFunctionExecutor.playerTextDrawUseBox(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            use = useBox
                    )
                }
                assertThat(playerTextDraw.useBox)
                        .isEqualTo(useBox)
            }
        }

        @Nested
        inner class BoxColorTests {

            @Test
            fun shouldInitializeBoxColor() {
                val boxColor = playerTextDraw.boxColor

                assertThat(boxColor)
                        .isEqualTo(Colors.TRANSPARENT)
            }

            @Test
            fun shouldSetBoxColor() {
                every { nativeFunctionExecutor.playerTextDrawBoxColor(any(), any(), any()) } returns true

                playerTextDraw.boxColor = Colors.RED

                verify {
                    nativeFunctionExecutor.playerTextDrawBoxColor(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(playerTextDraw.boxColor)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class ShadowSizeTests {

            @Test
            fun shouldInitializeShadowSize() {
                val shadowSize = playerTextDraw.shadowSize

                assertThat(shadowSize)
                        .isEqualTo(1)
            }

            @Test
            fun shouldSetShadowSize() {
                every { nativeFunctionExecutor.playerTextDrawSetShadow(any(), any(), any()) } returns true

                playerTextDraw.shadowSize = 4

                verify {
                    nativeFunctionExecutor.playerTextDrawSetShadow(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            size = 4
                    )
                }
                assertThat(playerTextDraw.shadowSize)
                        .isEqualTo(4)
            }
        }

        @Nested
        inner class OutlineSizeTests {

            @Test
            fun shouldInitializeOutlineSize() {
                val outlineSize = playerTextDraw.outlineSize

                assertThat(outlineSize)
                        .isEqualTo(0)
            }

            @Test
            fun shouldSetOutlineSize() {
                every { nativeFunctionExecutor.playerTextDrawSetOutline(any(), any(), any()) } returns true

                playerTextDraw.outlineSize = 4

                verify {
                    nativeFunctionExecutor.playerTextDrawSetOutline(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            size = 4
                    )
                }
                assertThat(playerTextDraw.outlineSize)
                        .isEqualTo(4)
            }
        }

        @Nested
        inner class BackgroundColorTests {

            @Test
            fun shouldInitializeBackgroundColor() {
                val backgroundColor = playerTextDraw.backgroundColor

                assertThat(backgroundColor)
                        .isEqualTo(Colors.BLACK)
            }

            @Test
            fun shouldSetBackgroundColor() {
                every { nativeFunctionExecutor.playerTextDrawBackgroundColor(any(), any(), any()) } returns true

                playerTextDraw.backgroundColor = Colors.RED

                verify {
                    nativeFunctionExecutor.playerTextDrawBackgroundColor(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(playerTextDraw.backgroundColor)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class FontTests {

            @Test
            fun shouldInitializeFont() {
                val font = playerTextDraw.font

                assertThat(font)
                        .isEqualTo(TextDrawFont.FONT2)
            }

            @Test
            fun shouldSetFont() {
                every { nativeFunctionExecutor.playerTextDrawFont(any(), any(), any()) } returns true

                playerTextDraw.font = TextDrawFont.BANK_GOTHIC

                verify {
                    nativeFunctionExecutor.playerTextDrawFont(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            font = TextDrawFont.BANK_GOTHIC.value
                    )
                }
                assertThat(playerTextDraw.font)
                        .isEqualTo(TextDrawFont.BANK_GOTHIC)
            }
        }

        @Nested
        inner class IsProportionalTests {

            @Test
            fun shouldInitializeIsProportional() {
                val isProportional = playerTextDraw.isProportional

                assertThat(isProportional)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetIsProportional(isProportional: Boolean) {
                every { nativeFunctionExecutor.playerTextDrawSetProportional(any(), any(), any()) } returns true

                playerTextDraw.isProportional = isProportional

                verify {
                    nativeFunctionExecutor.playerTextDrawSetProportional(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            set = isProportional
                    )
                }
                assertThat(playerTextDraw.isProportional)
                        .isEqualTo(isProportional)
            }
        }

        @Nested
        inner class IsSelectableTests {

            @Test
            fun shouldInitializeIsSelectable() {
                val isSelectable = playerTextDraw.isSelectable

                assertThat(isSelectable)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetIsSelectable(isSelectable: Boolean) {
                every { nativeFunctionExecutor.playerTextDrawSetSelectable(any(), any(), any()) } returns true

                playerTextDraw.isSelectable = isSelectable

                verify {
                    nativeFunctionExecutor.playerTextDrawSetSelectable(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            set = isSelectable
                    )
                }
                assertThat(playerTextDraw.isSelectable)
                        .isEqualTo(isSelectable)
            }
        }

        @Test
        fun shouldShowTextDrawToPlayer() {
            every { nativeFunctionExecutor.playerTextDrawShow(any(), any()) } returns true

            playerTextDraw.show()

            verify {
                nativeFunctionExecutor.playerTextDrawShow(playerid = playerId.value, text = playerTextDrawId.value)
            }
        }

        @Test
        fun shouldHideTextDrawFromPlayer() {
            every { nativeFunctionExecutor.playerTextDrawHide(any(), any()) } returns true

            playerTextDraw.hide()

            verify {
                nativeFunctionExecutor.playerTextDrawHide(playerid = playerId.value, text = playerTextDrawId.value)
            }
        }

        @Nested
        inner class TextTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.playerTextDrawSetString(any(), any(), any()) } returns true
            }

            @Test
            fun shouldInitializeText() {
                val text = playerTextDraw.text

                assertThat(text)
                        .isEqualTo("test")
            }

            @Test
            fun shouldSetText() {
                playerTextDraw.text = "Hi there"

                verify {
                    nativeFunctionExecutor.playerTextDrawSetString(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(playerTextDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetFormattedText() {
                every { player.locale } returns Locale.GERMANY
                every { textFormatter.format(Locale.GERMANY, "Hi %s", "there") } returns "Hi there"

                playerTextDraw.setText("Hi %s", "there")

                verify {
                    nativeFunctionExecutor.playerTextDrawSetString(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(playerTextDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetProvidedText() {
                val textKey = TextKey("player.text.draw.test")
                every { player.locale } returns Locale.GERMANY
                every { textProvider.getText(Locale.GERMANY, textKey) } returns "Hi there"

                playerTextDraw.setText(textKey)

                verify {
                    nativeFunctionExecutor.playerTextDrawSetString(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(playerTextDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetFormattedProvidedText() {
                val textKey = TextKey("player.text.draw.test")
                every { player.locale } returns Locale.GERMANY
                every { textProvider.getText(Locale.GERMANY, textKey) } returns "Hi %s"
                every { textFormatter.format(Locale.GERMANY, "Hi %s", "there") } returns "Hi there"

                playerTextDraw.setText(textKey, "there")

                verify {
                    nativeFunctionExecutor.playerTextDrawSetString(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(playerTextDraw.text)
                        .isEqualTo("Hi there")
            }
        }

        @Nested
        inner class PreviewModelTests {

            @Test
            fun shouldInitializePreviewModel() {
                val previewModel = playerTextDraw.previewModelId

                assertThat(previewModel)
                        .isNull()
            }

            @Test
            fun shouldSetPreviewModel() {
                every { nativeFunctionExecutor.playerTextDrawSetPreviewModel(any(), any(), any()) } returns true
                // IntelliJ doesn't like direct assignment
                val previewModel: Int? = 4

                playerTextDraw.previewModelId = previewModel

                verify {
                    nativeFunctionExecutor.playerTextDrawSetPreviewModel(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            modelindex = 4
                    )
                }
                assertThat(playerTextDraw.previewModelId)
                        .isEqualTo(4)
            }

            @Test
            fun givenNullAsValueItShouldSetPreviewModel() {
                every { nativeFunctionExecutor.playerTextDrawSetPreviewModel(any(), any(), any()) } returns true
                // IntelliJ doesn't like direct assignment
                val previewModel: Int? = null

                playerTextDraw.previewModelId = previewModel

                verify {
                    nativeFunctionExecutor.playerTextDrawSetPreviewModel(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            modelindex = -1
                    )
                }
                assertThat(playerTextDraw.previewModelId)
                        .isNull()
            }
        }

        @Nested
        inner class SetPreviewModelRotationTests {

            @Test
            fun shouldCallNativeFunctionExecutor() {
                every {
                    nativeFunctionExecutor.playerTextDrawSetPreviewRot(any(), any(), any(), any(), any(), any())
                } returns true

                playerTextDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 1.5f)

                verify {
                    nativeFunctionExecutor.playerTextDrawSetPreviewRot(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            fRotX = 45f,
                            fRotY = 60f,
                            fRotZ = 15f,
                            fZoom = 1.5f
                    )
                }
            }

            @Test
            fun shouldSetZoom() {
                every {
                    nativeFunctionExecutor.playerTextDrawSetPreviewRot(any(), any(), any(), any(), any(), any())
                } returns true

                playerTextDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 13.37f)

                assertThat(playerTextDraw.previewModelZoom)
                        .isEqualTo(13.37f)
            }

            @Test
            fun shouldSetPreviewModelRotationProperty() {
                every {
                    nativeFunctionExecutor.playerTextDrawSetPreviewRot(any(), any(), any(), any(), any(), any())
                } returns true

                playerTextDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 13.37f)

                assertThat(playerTextDraw.previewModelRotation)
                        .isEqualTo(vector3DOf(x = 45f, y = 60f, z = 15f))
            }
        }


        @Nested
        inner class PreviewModelVehicleColors {

            @Test
            fun shouldSetPreviewModelVehicleColors() {
                every { nativeFunctionExecutor.playerTextDrawSetPreviewVehCol(any(), any(), any(), any()) } returns true

                playerTextDraw.previewModelVehicleColors = mutableVehicleColorsOf(
                        color1 = VehicleColor[3],
                        color2 = VehicleColor[6]
                )

                verify {
                    nativeFunctionExecutor.playerTextDrawSetPreviewVehCol(
                            playerid = playerId.value,
                            text = playerTextDrawId.value,
                            color1 = 3,
                            color2 = 6
                    )
                }
                assertThat(playerTextDraw.previewModelVehicleColors)
                        .isEqualTo(vehicleColorsOf(
                                color1 = VehicleColor[3],
                                color2 = VehicleColor[6]
                        ))
            }

            @Test
            fun givenValueIsSetToNullItShouldDoNothing() {
                every { nativeFunctionExecutor.playerTextDrawSetPreviewVehCol(any(), any(), any(), any()) } returns true
                playerTextDraw.previewModelVehicleColors = mutableVehicleColorsOf(
                        color1 = VehicleColor[3],
                        color2 = VehicleColor[6]
                )

                val vehicleColors: VehicleColors? = null
                playerTextDraw.previewModelVehicleColors = vehicleColors

                verify(exactly = 1) { nativeFunctionExecutor.playerTextDrawSetPreviewVehCol(any(), any(), any(), any()) }
                assertThat(playerTextDraw.previewModelVehicleColors)
                        .isEqualTo(vehicleColorsOf(
                                color1 = VehicleColor[3],
                                color2 = VehicleColor[6]
                        ))
            }
        }

        @Test
        fun shouldCallOnPlayerClickPlayerTextDrawReceiverDelegate() {
            every {
                onPlayerClickPlayerTextDrawReceiver.onPlayerClickPlayerTextDraw(any())
            } returns OnPlayerClickPlayerTextDrawListener.Result.Processed

            playerTextDraw.onClick()

            verify { onPlayerClickPlayerTextDrawReceiver.onPlayerClickPlayerTextDraw(playerTextDraw) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.playerTextDrawDestroy(any(), any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = playerTextDraw.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun givenPlayerIsNotConnectedIsDestroyShouldBeTrue() {
                every { player.isConnected } returns false

                val isDestroyed = playerTextDraw.isDestroyed

                assertThat(isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldDestroyPlayerTextDraw() {
                val onDestroy = mockk<PlayerTextDraw.() -> Unit>(relaxed = true)
                playerTextDraw.onDestroy(onDestroy)

                playerTextDraw.destroy()

                verifyOrder {
                    onDestroy.invoke(playerTextDraw)
                    nativeFunctionExecutor.playerTextDrawDestroy(playerid = playerId.value, text = playerTextDrawId.value)
                }
                assertThat(playerTextDraw.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<PlayerTextDraw.() -> Unit>(relaxed = true)
                playerTextDraw.onDestroy(onDestroy)

                playerTextDraw.destroy()
                playerTextDraw.destroy()

                verify(exactly = 1) {
                    nativeFunctionExecutor.playerTextDrawDestroy(playerid = playerId.value, text = playerTextDrawId.value)
                    onDestroy.invoke(playerTextDraw)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                playerTextDraw.destroy()

                val caughtThrowable = catchThrowable { playerTextDraw.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}