package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
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
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerClickTextDrawReceiverDelegate
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
import java.util.Locale

internal class TextDrawTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructTextDraw() {
            val textDrawId = TextDrawId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    textDrawCreate(x = 1f, y = 2f, text = "test")
                } returns textDrawId.value
            }

            val textDraw = TextDraw(
                    text = "test",
                    position = vector2DOf(x = 1f, y = 2f),
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = mockk(),
                    textProvider = mockk(),
                    locale = Locale.CANADA
            )

            assertThat(textDraw.id)
                    .isEqualTo(textDrawId)
        }

        @Test
        fun givenCreateTextDrawReturnsInvalidTextDrawIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    textDrawCreate(x = 1f, y = 2f, text = "test")
                } returns SAMPConstants.INVALID_TEXT_DRAW
            }

            val caughtThrowable = catchThrowable {
                TextDraw(
                        text = "test",
                        position = vector2DOf(x = 1f, y = 2f),
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        textFormatter = mockk(),
                        textProvider = mockk(),
                        locale = Locale.CANADA
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val locale = Locale.GERMANY
        private val textDrawId = TextDrawId.valueOf(69)
        private lateinit var textDraw: TextDraw
        private val onPlayerClickTextDrawReceiver = mockk<OnPlayerClickTextDrawReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        private val textFormatter = mockk<TextFormatter>()
        private val textProvider = mockk<TextProvider>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.textDrawCreate(any(), any(), any())
            } returns textDrawId.value
            textDraw = TextDraw(
                    text = "test",
                    position = mutableVector2DOf(x = 1f, y = 2f),
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    textProvider = textProvider,
                    locale = locale,
                    onPlayerClickTextDrawReceiver = onPlayerClickTextDrawReceiver
            )
        }

        @Test
        fun shouldSetPosition() {
            val position = textDraw.position

            assertThat(position)
                    .isEqualTo(vector2DOf(x = 1f, y = 2f))
        }

        @Nested
        inner class LetterSizeTests {

            @Test
            fun shouldInitializeLetterSize() {
                val letterSize = textDraw.letterSize

                assertThat(letterSize)
                        .isEqualTo(vector2DOf(x = 1f, y = 1f))
            }

            @Test
            fun shouldSetLetterSize() {
                every { nativeFunctionExecutor.textDrawLetterSize(any(), any(), any()) } returns true

                textDraw.letterSize = mutableVector2DOf(x = 13f, y = 3f)

                verify {
                    nativeFunctionExecutor.textDrawLetterSize(
                            text = textDrawId.value,
                            x = 13f,
                            y = 3f
                    )
                }
                assertThat(textDraw.letterSize)
                        .isEqualTo(vector2DOf(x = 13f, y = 3f))
            }
        }

        @Nested
        inner class TextSizeTests {

            @Test
            fun shouldInitializeTextSize() {
                val textSize = textDraw.textSize

                assertThat(textSize)
                        .isEqualTo(vector2DOf(x = 0f, y = 0f))
            }

            @Test
            fun shouldSetTextSize() {
                every { nativeFunctionExecutor.textDrawTextSize(any(), any(), any()) } returns true

                textDraw.textSize = mutableVector2DOf(x = 13f, y = 3f)

                verify {
                    nativeFunctionExecutor.textDrawTextSize(
                            text = textDrawId.value,
                            x = 13f,
                            y = 3f
                    )
                }
                assertThat(textDraw.textSize)
                        .isEqualTo(vector2DOf(x = 13f, y = 3f))
            }
        }

        @Nested
        inner class AlignmentTests {

            @Test
            fun shouldInitializeAlignment() {
                val alignment = textDraw.alignment

                assertThat(alignment)
                        .isEqualTo(TextDrawAlignment.LEFT)
            }

            @Test
            fun shouldSetAlignment() {
                every { nativeFunctionExecutor.textDrawAlignment(any(), any()) } returns true

                textDraw.alignment = TextDrawAlignment.CENTERED

                verify {
                    nativeFunctionExecutor.textDrawAlignment(
                            text = textDrawId.value,
                            alignment = TextDrawAlignment.CENTERED.value
                    )
                }
                assertThat(textDraw.alignment)
                        .isEqualTo(TextDrawAlignment.CENTERED)
            }
        }

        @Nested
        inner class ColorTests {

            @Test
            fun shouldInitializeColor() {
                val color = textDraw.color

                assertThat(color)
                        .isEqualTo(Colors.WHITE)
            }

            @Test
            fun shouldSetColor() {
                every { nativeFunctionExecutor.textDrawColor(any(), any()) } returns true

                textDraw.color = Colors.RED

                verify {
                    nativeFunctionExecutor.textDrawColor(
                            text = textDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(textDraw.color)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class UseBoxTests {

            @Test
            fun shouldInitializeUseBox() {
                val useBox = textDraw.useBox

                assertThat(useBox)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetUseBox(useBox: Boolean) {
                every { nativeFunctionExecutor.textDrawUseBox(any(), any()) } returns true

                textDraw.useBox = useBox

                verify {
                    nativeFunctionExecutor.textDrawUseBox(
                            text = textDrawId.value,
                            use = useBox
                    )
                }
                assertThat(textDraw.useBox)
                        .isEqualTo(useBox)
            }
        }

        @Nested
        inner class BoxColorTests {

            @Test
            fun shouldInitializeBoxColor() {
                val boxColor = textDraw.boxColor

                assertThat(boxColor)
                        .isEqualTo(Colors.TRANSPARENT)
            }

            @Test
            fun shouldSetBoxColor() {
                every { nativeFunctionExecutor.textDrawBoxColor(any(), any()) } returns true

                textDraw.boxColor = Colors.RED

                verify {
                    nativeFunctionExecutor.textDrawBoxColor(
                            text = textDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(textDraw.boxColor)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class ShadowSizeTests {

            @Test
            fun shouldInitializeShadowSize() {
                val shadowSize = textDraw.shadowSize

                assertThat(shadowSize)
                        .isEqualTo(1)
            }

            @Test
            fun shouldSetShadowSize() {
                every { nativeFunctionExecutor.textDrawSetShadow(any(), any()) } returns true

                textDraw.shadowSize = 4

                verify {
                    nativeFunctionExecutor.textDrawSetShadow(
                            text = textDrawId.value,
                            size = 4
                    )
                }
                assertThat(textDraw.shadowSize)
                        .isEqualTo(4)
            }
        }

        @Nested
        inner class OutlineSizeTests {

            @Test
            fun shouldInitializeOutlineSize() {
                val outlineSize = textDraw.outlineSize

                assertThat(outlineSize)
                        .isEqualTo(0)
            }

            @Test
            fun shouldSetOutlineSize() {
                every { nativeFunctionExecutor.textDrawSetOutline(any(), any()) } returns true

                textDraw.outlineSize = 4

                verify {
                    nativeFunctionExecutor.textDrawSetOutline(
                            text = textDrawId.value,
                            size = 4
                    )
                }
                assertThat(textDraw.outlineSize)
                        .isEqualTo(4)
            }
        }

        @Nested
        inner class BackgroundColorTests {

            @Test
            fun shouldInitializeBackgroundColor() {
                val backgroundColor = textDraw.backgroundColor

                assertThat(backgroundColor)
                        .isEqualTo(Colors.BLACK)
            }

            @Test
            fun shouldSetBackgroundColor() {
                every { nativeFunctionExecutor.textDrawBackgroundColor(any(), any()) } returns true

                textDraw.backgroundColor = Colors.RED

                verify {
                    nativeFunctionExecutor.textDrawBackgroundColor(
                            text = textDrawId.value,
                            color = Colors.RED.value
                    )
                }
                assertThat(textDraw.backgroundColor)
                        .isEqualTo(Colors.RED)
            }
        }

        @Nested
        inner class FontTests {

            @Test
            fun shouldInitializeFont() {
                val font = textDraw.font

                assertThat(font)
                        .isEqualTo(TextDrawFont.FONT2)
            }

            @Test
            fun shouldSetFont() {
                every { nativeFunctionExecutor.textDrawFont(any(), any()) } returns true

                textDraw.font = TextDrawFont.BANK_GOTHIC

                verify {
                    nativeFunctionExecutor.textDrawFont(
                            text = textDrawId.value,
                            font = TextDrawFont.BANK_GOTHIC.value
                    )
                }
                assertThat(textDraw.font)
                        .isEqualTo(TextDrawFont.BANK_GOTHIC)
            }
        }

        @Nested
        inner class IsProportionalTests {

            @Test
            fun shouldInitializeIsProportional() {
                val isProportional = textDraw.isProportional

                assertThat(isProportional)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetIsProportional(isProportional: Boolean) {
                every { nativeFunctionExecutor.textDrawSetProportional(any(), any()) } returns true

                textDraw.isProportional = isProportional

                verify {
                    nativeFunctionExecutor.textDrawSetProportional(
                            text = textDrawId.value,
                            set = isProportional
                    )
                }
                assertThat(textDraw.isProportional)
                        .isEqualTo(isProportional)
            }
        }

        @Nested
        inner class IsSelectableTests {

            @Test
            fun shouldInitializeIsSelectable() {
                val isSelectable = textDraw.isSelectable

                assertThat(isSelectable)
                        .isEqualTo(false)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetIsSelectable(isSelectable: Boolean) {
                every { nativeFunctionExecutor.textDrawSetSelectable(any(), any()) } returns true

                textDraw.isSelectable = isSelectable

                verify {
                    nativeFunctionExecutor.textDrawSetSelectable(
                            text = textDrawId.value,
                            set = isSelectable
                    )
                }
                assertThat(textDraw.isSelectable)
                        .isEqualTo(isSelectable)
            }
        }

        @Test
        fun shouldShowTextDrawToPlayer() {
            val playerId = PlayerId.valueOf(13)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every { nativeFunctionExecutor.textDrawShowForPlayer(any(), any()) } returns true

            textDraw.show(player)

            verify {
                nativeFunctionExecutor.textDrawShowForPlayer(playerid = playerId.value, text = textDrawId.value)
            }
        }

        @Test
        fun shouldShowTextDrawToAll() {
            every { nativeFunctionExecutor.textDrawShowForAll(any()) } returns true

            textDraw.showForAll()

            verify {
                nativeFunctionExecutor.textDrawShowForAll(textDrawId.value)
            }
        }

        @Test
        fun shouldHideTextDrawFromAll() {
            every { nativeFunctionExecutor.textDrawHideForAll(any()) } returns true

            textDraw.hideForAll()

            verify {
                nativeFunctionExecutor.textDrawHideForAll(textDrawId.value)
            }
        }

        @Test
        fun shouldHideTextDrawFromPlayer() {
            val playerId = PlayerId.valueOf(13)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every { nativeFunctionExecutor.textDrawHideForPlayer(any(), any()) } returns true

            textDraw.hide(player)

            verify {
                nativeFunctionExecutor.textDrawHideForPlayer(playerid = playerId.value, text = textDrawId.value)
            }
        }

        @Nested
        inner class TextTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.textDrawSetString(any(), any()) } returns true
            }

            @Test
            fun shouldInitializeText() {
                val text = textDraw.text

                assertThat(text)
                        .isEqualTo("test")
            }

            @Test
            fun shouldSetText() {
                textDraw.text = "Hi there"

                verify {
                    nativeFunctionExecutor.textDrawSetString(
                            text = textDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(textDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetFormattedText() {
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                textDraw.setText("Hi %s", "there")

                verify {
                    nativeFunctionExecutor.textDrawSetString(
                            text = textDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(textDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetProvidedText() {
                val textKey = TextKey("player.text.draw.test")
                every { textProvider.getText(locale, textKey) } returns "Hi there"

                textDraw.setText(textKey)

                verify {
                    nativeFunctionExecutor.textDrawSetString(
                            text = textDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(textDraw.text)
                        .isEqualTo("Hi there")
            }

            @Test
            fun shouldSetFormattedProvidedText() {
                val textKey = TextKey("player.text.draw.test")
                every { textProvider.getText(locale, textKey) } returns "Hi %s"
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                textDraw.setText(textKey, "there")

                verify {
                    nativeFunctionExecutor.textDrawSetString(
                            text = textDrawId.value,
                            string = "Hi there"
                    )
                }
                assertThat(textDraw.text)
                        .isEqualTo("Hi there")
            }
        }

        @Nested
        inner class PreviewModelTests {

            @Test
            fun shouldInitializePreviewModel() {
                val previewModel = textDraw.previewModelId

                assertThat(previewModel)
                        .isNull()
            }

            @Test
            fun shouldSetPreviewModel() {
                every { nativeFunctionExecutor.textDrawSetPreviewModel(any(), any()) } returns true
                // IntelliJ doesn't like direct assignment
                val previewModel: Int? = 4

                textDraw.previewModelId = previewModel

                verify { nativeFunctionExecutor.textDrawSetPreviewModel(text = textDrawId.value, modelindex = 4) }
                assertThat(textDraw.previewModelId)
                        .isEqualTo(4)
            }

            @Test
            fun givenNullAsValueItShouldSetPreviewModel() {
                every { nativeFunctionExecutor.textDrawSetPreviewModel(any(), any()) } returns true
                // IntelliJ doesn't like direct assignment
                val previewModel: Int? = null

                textDraw.previewModelId = previewModel

                verify { nativeFunctionExecutor.textDrawSetPreviewModel(text = textDrawId.value, modelindex = -1) }
                assertThat(textDraw.previewModelId)
                        .isNull()
            }
        }

        @Nested
        inner class SetPreviewModelRotationTests {

            @Test
            fun shouldCallNativeFunctionExecutor() {
                every {
                    nativeFunctionExecutor.textDrawSetPreviewRot(any(), any(), any(), any(), any())
                } returns true

                textDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 1.5f)

                verify {
                    nativeFunctionExecutor.textDrawSetPreviewRot(
                            text = textDrawId.value,
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
                    nativeFunctionExecutor.textDrawSetPreviewRot(any(), any(), any(), any(), any())
                } returns true

                textDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 13.37f)

                assertThat(textDraw.previewModelZoom)
                        .isEqualTo(13.37f)
            }

            @Test
            fun shouldSetPreviewModelRotationProperty() {
                every {
                    nativeFunctionExecutor.textDrawSetPreviewRot(any(), any(), any(), any(), any())
                } returns true

                textDraw.setPreviewModelRotation(vector3DOf(x = 45f, y = 60f, z = 15f), 13.37f)

                assertThat(textDraw.previewModelRotation)
                        .isEqualTo(vector3DOf(x = 45f, y = 60f, z = 15f))
            }
        }

        @Nested
        inner class PreviewModelVehicleColors {

            @Test
            fun shouldSetPreviewModelVehicleColors() {
                every { nativeFunctionExecutor.textDrawSetPreviewVehCol(any(), any(), any()) } returns true

                textDraw.previewModelVehicleColors = mutableVehicleColorsOf(
                        color1 = VehicleColor[3],
                        color2 = VehicleColor[6]
                )

                verify {
                    nativeFunctionExecutor.textDrawSetPreviewVehCol(
                            text = textDrawId.value,
                            color1 = 3,
                            color2 = 6
                    )
                }
                assertThat(textDraw.previewModelVehicleColors)
                        .isEqualTo(
                                vehicleColorsOf(
                                        color1 = VehicleColor[3],
                                        color2 = VehicleColor[6]
                                )
                        )
            }

            @Test
            fun givenValueIsSetToNullItShouldDoNothing() {
                every { nativeFunctionExecutor.textDrawSetPreviewVehCol(any(), any(), any()) } returns true
                textDraw.previewModelVehicleColors = mutableVehicleColorsOf(
                        color1 = VehicleColor[3],
                        color2 = VehicleColor[6]
                )

                val vehicleColors: VehicleColors? = null
                textDraw.previewModelVehicleColors = vehicleColors

                verify(exactly = 1) { nativeFunctionExecutor.textDrawSetPreviewVehCol(any(), any(), any()) }
                assertThat(textDraw.previewModelVehicleColors)
                        .isEqualTo(
                                vehicleColorsOf(
                                        color1 = VehicleColor[3],
                                        color2 = VehicleColor[6]
                                )
                        )
            }
        }

        @Test
        fun shouldCallOnPlayerClickTextDrawReceiverDelegate() {
            val player = mockk<Player>()
            every {
                onPlayerClickTextDrawReceiver.onPlayerClickTextDraw(
                        any(),
                        any()
                )
            } returns OnPlayerClickTextDrawListener.Result.Processed

            textDraw.onClick(player)

            verify { onPlayerClickTextDrawReceiver.onPlayerClickTextDraw(player, textDraw) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.textDrawDestroy(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = textDraw.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyTextDraw() {
                val onDestroy = mockk<TextDraw.() -> Unit>(relaxed = true)
                textDraw.onDestroy(onDestroy)

                textDraw.destroy()

                verifyOrder {
                    onDestroy.invoke(textDraw)
                    nativeFunctionExecutor.textDrawDestroy(textDrawId.value)
                }
                assertThat(textDraw.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<TextDraw.() -> Unit>(relaxed = true)
                textDraw.onDestroy(onDestroy)

                textDraw.destroy()
                textDraw.destroy()

                verify(exactly = 1) {
                    nativeFunctionExecutor.textDrawDestroy(textDrawId.value)
                    onDestroy.invoke(textDraw)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                textDraw.destroy()

                val caughtThrowable = catchThrowable { textDraw.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}