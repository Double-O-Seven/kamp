package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class StreamableMapObjectTest {

    private lateinit var streamableMapObject: StreamableMapObject
    private val playerMapObjectService = mockk<PlayerMapObjectService>()
    private val onStreamableMapObjectMovedHandler = mockk<OnStreamableMapObjectMovedHandler>()
    private val onPlayerEditStreamableMapObjectHandler = mockk<OnPlayerEditStreamableMapObjectHandler>()
    private val onPlayerSelectStreamableMapObjectHandler = mockk<OnPlayerSelectStreamableMapObjectHandler>()
    private val textProvider = mockk<TextProvider>()
    private val streamableMapObjectStateFactory = mockk<StreamableMapObjectStateFactory>()
    private val playerId = PlayerId.valueOf(69)
    private lateinit var player: Player
    private val playerMapObjectId = PlayerMapObjectId.valueOf(69)
    private lateinit var playerMapObject: PlayerMapObject
    private val initialCoordinates = mutableVector3DOf(1f, 2f, 3f)
    private val initialRotation = mutableVector3DOf(4f, 5f, 6f)
    private val initialState = mockk<StreamableMapObjectState.FixedCoordinates>()

    @BeforeEach
    fun setUp() {
        every {
            streamableMapObjectStateFactory.createFixedCoordinates(initialCoordinates, initialRotation)
        } returns initialState
        every { initialState.coordinates } returns initialCoordinates
        every { initialState.rotation } returns initialRotation
        player = mockk {
            every { id } returns playerId
        }
        playerMapObject = mockk {
            every { id } returns playerMapObjectId
            every { onEdit(any()) } just Runs
            every { onSelect(any()) } just Runs
            every { this@mockk.player } returns this@StreamableMapObjectTest.player
        }
        streamableMapObject = StreamableMapObject(
                modelId = 1337,
                priority = 0,
                streamDistance = 75f,
                coordinates = initialCoordinates,
                rotation = initialRotation,
                interiorIds = mutableSetOf(1),
                virtualWorldIds = mutableSetOf(1337),
                onStreamableMapObjectMovedHandler = onStreamableMapObjectMovedHandler,
                onPlayerSelectStreamableMapObjectHandler = onPlayerSelectStreamableMapObjectHandler,
                onPlayerEditStreamableMapObjectHandler = onPlayerEditStreamableMapObjectHandler,
                playerMapObjectService = playerMapObjectService,
                textProvider = textProvider,
                streamableMapObjectStateFactory = streamableMapObjectStateFactory
        )
    }

    @Test
    fun shouldGetBoundingBox() {
        every { initialState.coordinates } returns vector3DOf(1f, 2f, 3f)
        val boundingBox = streamableMapObject.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(
                        -74.0,
                        -73.0,
                        -72.0,
                        76.0,
                        77.0,
                        78.0
                ))
    }

    @Nested
    inner class CoordinatesTests {

        @Test
        fun shouldReturnCoordinates() {
            every { initialState.coordinates } returns vector3DOf(11f, 22f, 33f)

            val coordinates = streamableMapObject.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun setShouldTransitionToFixedCoordinatesState() {
            every { initialState.isStreamOutRequiredOnLeave(any()) } returns false
            every { initialState.onLeave(any()) } just Runs
            val fixedCoordinates = mockk<StreamableMapObjectState.FixedCoordinates> {
                every { coordinates } returns vector3DOf(0f, 0f, 0f)
                every { onEnter(any()) } just Runs
            }
            clearMocks(streamableMapObjectStateFactory)
            every {
                streamableMapObjectStateFactory.createFixedCoordinates(
                        coordinates = vector3DOf(123f, 456f, 789f),
                        rotation = initialRotation
                )
            } returns fixedCoordinates
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every { initialState.onStreamIn(any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.coordinates = vector3DOf(123f, 456f, 789f)

            val slot = slot<Collection<PlayerMapObject>>()
            verify { fixedCoordinates.onEnter(capture(slot)) }
            assertThat(slot.captured)
                    .containsExactlyInAnyOrder(playerMapObject)
        }

        @Test
        fun setShouldCallOnBoundingBoxChanged() {
            initialState.apply {
                every { coordinates } returns initialCoordinates
                every { rotation } returns initialRotation
                every { isStreamOutRequiredOnLeave(any()) } returns false
                every { onLeave(any()) } just Runs
            }
            val fixedCoordinates = mockk<StreamableMapObjectState.FixedCoordinates> {
                every { coordinates } returns vector3DOf(100f, 200f, 300f)
                every { onEnter(any()) } just Runs
            }
            clearMocks(streamableMapObjectStateFactory)
            every {
                streamableMapObjectStateFactory.createFixedCoordinates(any(), any())
            } returns fixedCoordinates
            val onBoundingBoxChanged = mockk<StreamableMapObject.(Rect3d) -> Unit>(relaxed = true)
            streamableMapObject.onBoundingBoxChanged(onBoundingBoxChanged)

            streamableMapObject.coordinates = vector3DOf(100f, 200f, 300f)

            val slot = slot<Rect3d>()
            verify { onBoundingBoxChanged.invoke(streamableMapObject, capture(slot)) }
            assertThat(slot.captured)
                    .isEqualTo(Rect3d(25.0, 125.0, 225.0, 175.0, 275.0, 375.0))
        }
    }

    @Nested
    inner class RotationTests {

        @Test
        fun shouldReturnRotation() {
            every { initialState.rotation } returns vector3DOf(11f, 22f, 33f)

            val rotation = streamableMapObject.rotation

            assertThat(rotation)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun setShouldTransitionToFixedRotationState() {
            every { initialState.isStreamOutRequiredOnLeave(any()) } returns false
            every { initialState.onLeave(any()) } just Runs
            val fixedCoordinates = mockk<StreamableMapObjectState.FixedCoordinates> {
                every { onEnter(any()) } just Runs
            }
            clearMocks(streamableMapObjectStateFactory)
            every {
                streamableMapObjectStateFactory.createFixedCoordinates(
                        coordinates = initialCoordinates,
                        rotation = vector3DOf(123f, 456f, 789f)
                )
            } returns fixedCoordinates
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every { initialState.onStreamIn(any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.rotation = vector3DOf(123f, 456f, 789f)

            val slot = slot<Collection<PlayerMapObject>>()
            verify { fixedCoordinates.onEnter(capture(slot)) }
            assertThat(slot.captured)
                    .containsExactlyInAnyOrder(playerMapObject)
        }
    }

    @Nested
    inner class CameraCollisionTests {

        @Test
        fun cameraCollisionsShouldNotBeDisabledInitially() {
            assertThat(streamableMapObject.isCameraCollisionDisabled)
                    .isFalse()
        }

        @Test
        fun shouldDisableCameraCollisions() {
            streamableMapObject.disableCameraCollision()

            assertThat(streamableMapObject.isCameraCollisionDisabled)
                    .isTrue()
        }

        @Test
        fun shouldDisableCameraCollisionsForPlayerMapObject() {
            every { playerMapObject.disableCameraCollision() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every { initialState.onStreamIn(any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.disableCameraCollision()

            verify {
                playerMapObject.disableCameraCollision()
            }
        }
    }

    @Test
    fun shouldSetMaterial() {
        every {
            playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
        } returns playerMapObject
        every { playerMapObject.setMaterial(any(), any(), any(), any(), any()) } just Runs
        every { initialState.onStreamIn(any()) } just Runs
        streamableMapObject.onStreamIn(player)

        streamableMapObject.setMaterial(3, 69, "txd", "texture", Colors.RED)

        verify { playerMapObject.setMaterial(3, 69, "txd", "texture", Colors.RED) }
    }

    @Nested
    inner class SetMaterialTextTests {

        @Test
        fun shouldSetMaterialTextWithSimpleString() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every {
                playerMapObject.setMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } just Runs
            every { initialState.onStreamIn(any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.setMaterialText(
                    text = "Hi there",
                    index = 1,
                    size = ObjectMaterialSize.SIZE_128X128,
                    fontFace = "Comic Sans",
                    fontSize = 36,
                    isBold = true,
                    fontColor = Colors.RED,
                    backColor = Colors.BLUE,
                    textAlignment = ObjectMaterialTextAlignment.RIGHT
            )

            verify {
                playerMapObject.setMaterialText(
                        text = "Hi there",
                        index = 1,
                        size = ObjectMaterialSize.SIZE_128X128,
                        fontFace = "Comic Sans",
                        fontSize = 36,
                        isBold = true,
                        fontColor = Colors.RED,
                        backColor = Colors.BLUE,
                        textAlignment = ObjectMaterialTextAlignment.RIGHT
                )
            }
        }

        @Test
        fun shouldSetMaterialTextWithTextKey() {
            val locale = Locale.GERMANY
            val textKey = TextKey("hi.there")
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            every { player.locale } returns locale
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every {
                playerMapObject.setMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } just Runs
            every { initialState.onStreamIn(any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.setMaterialText(
                    textKey = textKey,
                    index = 1,
                    size = ObjectMaterialSize.SIZE_128X128,
                    fontFace = "Comic Sans",
                    fontSize = 36,
                    isBold = true,
                    fontColor = Colors.RED,
                    backColor = Colors.BLUE,
                    textAlignment = ObjectMaterialTextAlignment.RIGHT
            )

            verify {
                playerMapObject.setMaterialText(
                        text = "Hi there",
                        index = 1,
                        size = ObjectMaterialSize.SIZE_128X128,
                        fontFace = "Comic Sans",
                        fontSize = 36,
                        isBold = true,
                        fontColor = Colors.RED,
                        backColor = Colors.BLUE,
                        textAlignment = ObjectMaterialTextAlignment.RIGHT
                )
            }
        }
    }

    @Test
    fun shouldEdit() {
        every { playerMapObject.edit(any()) } just Runs
        every {
            playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
        } returns playerMapObject
        every { initialState.onStreamIn(any()) } just Runs
        streamableMapObject.onStreamIn(player)

        streamableMapObject.edit(player)

        verify { playerMapObject.edit(player) }
    }

}