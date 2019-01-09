package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateMachineFactory
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.data.Percentage.withPercentage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.EnumSource
import java.util.Locale
import java.util.stream.Stream

internal class StreamableMapObjectImplTest {

    private lateinit var streamableMapObject: StreamableMapObjectImpl
    private val playerMapObjectService = mockk<PlayerMapObjectService>()
    private val onStreamableMapObjectMovedHandler = mockk<OnStreamableMapObjectMovedHandler>()
    private val onPlayerEditStreamableMapObjectHandler = mockk<OnPlayerEditStreamableMapObjectHandler>()
    private val onPlayerSelectStreamableMapObjectHandler = mockk<OnPlayerSelectStreamableMapObjectHandler>()
    private val onStreamableMapObjectStreamInHandler = mockk<OnStreamableMapObjectStreamInHandler>()
    private val onStreamableMapObjectStreamOutHandler = mockk<OnStreamableMapObjectStreamOutHandler>()
    private val onStreamableMapObjectMovedReceiver = mockk<OnStreamableMapObjectMovedReceiverDelegate>()
    private val onPlayerEditStreamableMapObjectReceiver = mockk<OnPlayerEditStreamableMapObjectReceiverDelegate>()
    private val onPlayerSelectStreamableMapObjectReceiver = mockk<OnPlayerSelectStreamableMapObjectReceiverDelegate>()
    private val onStreamableMapObjectStreamInReceiver = mockk<OnStreamableMapObjectStreamInReceiverDelegate>()
    private val onStreamableMapObjectStreamOutReceiver = mockk<OnStreamableMapObjectStreamOutReceiverDelegate>()
    private val textProvider = mockk<TextProvider>()
    private val mapObjectStreamer = mockk<MapObjectStreamer>()
    private val streamableMapObjectStateMachineFactory = mockk<StreamableMapObjectStateMachineFactory>()
    private val playerId = PlayerId.valueOf(69)
    private lateinit var player: Player
    private val playerMapObjectId = PlayerMapObjectId.valueOf(69)
    private lateinit var playerMapObject: PlayerMapObject
    private val initialCoordinates = mutableVector3DOf(1f, 2f, 3f)
    private val initialRotation = mutableVector3DOf(4f, 5f, 6f)
    private val streamableMapObjectStateMachine = mockk<StreamableMapObjectStateMachine>()
    private val modelId = 1337
    private val streamDistance = 75f

    @BeforeEach
    fun setUp() {
        every {
            streamableMapObjectStateMachineFactory.create(any(), mapObjectStreamer, initialCoordinates, initialRotation)
        } returns streamableMapObjectStateMachine
        player = mockk {
            every { id } returns playerId
        }
        playerMapObject = mockk {
            every { id } returns playerMapObjectId
            every { addOnPlayerEditPlayerMapObjectListener(any()) } just Runs
            every { addOnPlayerSelectPlayerMapObjectListener(any()) } just Runs
            every { this@mockk.player } returns this@StreamableMapObjectImplTest.player
        }
        streamableMapObject = StreamableMapObjectImpl(
                modelId = modelId,
                priority = 0,
                streamDistance = streamDistance,
                coordinates = initialCoordinates,
                rotation = initialRotation,
                interiorIds = mutableSetOf(),
                virtualWorldIds = mutableSetOf(),
                onStreamableMapObjectMovedHandler = onStreamableMapObjectMovedHandler,
                onPlayerSelectStreamableMapObjectHandler = onPlayerSelectStreamableMapObjectHandler,
                onPlayerEditStreamableMapObjectHandler = onPlayerEditStreamableMapObjectHandler,
                onStreamableMapObjectStreamInHandler = onStreamableMapObjectStreamInHandler,
                onStreamableMapObjectStreamOutHandler = onStreamableMapObjectStreamOutHandler,
                playerMapObjectService = playerMapObjectService,
                textProvider = textProvider,
                mapObjectStreamer = mapObjectStreamer,
                streamableMapObjectStateMachineFactory = streamableMapObjectStateMachineFactory,
                onStreamableMapObjectMovedReceiver = onStreamableMapObjectMovedReceiver,
                onPlayerEditStreamableMapObjectReceiver = onPlayerEditStreamableMapObjectReceiver,
                onPlayerSelectStreamableMapObjectReceiver = onPlayerSelectStreamableMapObjectReceiver,
                onStreamableMapObjectStreamInReceiver = onStreamableMapObjectStreamInReceiver,
                onStreamableMapObjectStreamOutReceiver = onStreamableMapObjectStreamOutReceiver
        )
    }

    @Nested
    inner class OnStreamInTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { playerMapObject.player } returns player
            every { playerMapObject.addOnPlayerEditPlayerMapObjectListener(any()) } just Runs
            every { playerMapObject.addOnPlayerSelectPlayerMapObjectListener(any()) } just Runs
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        }

        @Test
        fun shouldInitiallyNotBeStreamedIn() {
            val isStreamedIn = streamableMapObject.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun shouldDrawDistanceShouldReturnStreamDistance() {
            val drawDistance = streamableMapObject.drawDistance

            assertThat(drawDistance)
                    .isEqualTo(streamDistance)
        }

        @Test
        fun shouldStreamIn() {
            val coordinates = vector3DOf(1f, 2f, 3f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns coordinates
                every { this@mockk.rotation } returns rotation
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = modelId,
                        coordinates = coordinates,
                        rotation = rotation,
                        drawDistance = streamDistance
                )
            } returns playerMapObject

            streamableMapObject.onStreamIn(player)

            verify {
                currentState.onStreamIn(playerMapObject)
            }
        }

        @Test
        fun shouldCallOnStreamInHandlers() {
            val coordinates = vector3DOf(1f, 2f, 3f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns coordinates
                every { this@mockk.rotation } returns rotation
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = modelId,
                        coordinates = coordinates,
                        rotation = rotation,
                        drawDistance = streamDistance
                )
            } returns playerMapObject

            streamableMapObject.onStreamIn(player)

            verify {
                onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(streamableMapObject, player)
                onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(streamableMapObject, player)
            }
        }

        @Test
        fun givenItIsAlreadyStreamedInItShouldThrowAnException() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            val caughtThrowable = catchThrowable { streamableMapObject.onStreamIn(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable map object is already streamed in")
        }

        @Test
        fun givenMaterialTextItShouldSetIt() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObject.setMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } just Runs
            streamableMapObject.setMaterialText(
                    text = "Hi there",
                    size = ObjectMaterialSize.SIZE_128X128,
                    index = 1,
                    backColor = Colors.BLUE,
                    fontColor = Colors.RED,
                    isBold = true,
                    fontFace = "Comic Sans",
                    textAlignment = ObjectMaterialTextAlignment.LEFT,
                    fontSize = 24
            )
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.onStreamIn(player)

            verify {
                playerMapObject.setMaterialText(
                        text = "Hi there",
                        size = ObjectMaterialSize.SIZE_128X128,
                        index = 1,
                        backColor = Colors.BLUE,
                        fontColor = Colors.RED,
                        isBold = true,
                        fontFace = "Comic Sans",
                        textAlignment = ObjectMaterialTextAlignment.LEFT,
                        fontSize = 24
                )
            }
        }

        @Test
        fun givenMaterialItShouldSetIt() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObject.setMaterial(any(), any(), any(), any(), any())
            } just Runs
            streamableMapObject.setMaterial(
                    index = 1,
                    modelId = 815,
                    color = Colors.RED,
                    txdName = "txd",
                    textureName = "texture"
            )
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.onStreamIn(player)

            verify {
                playerMapObject.setMaterial(
                        index = 1,
                        modelId = 815,
                        color = Colors.RED,
                        txdName = "txd",
                        textureName = "texture"
                )
            }
        }

        @Test
        fun givenCameraCollisionIsDisabledItShouldDisableItOnPlayerMapObject() {
            every { playerMapObject.disableCameraCollision() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.disableCameraCollision()

            streamableMapObject.onStreamIn(player)

            verify { playerMapObject.disableCameraCollision() }
        }

        @Test
        fun shouldRegisterAsOnPlayerEditPlayerMapObjectListener() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.onStreamIn(player)

            verify { playerMapObject.addOnPlayerEditPlayerMapObjectListener(streamableMapObject) }
        }

        @Test
        fun shouldRegisterAsOnPlayerSelectPlayerMapObjectListener() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.onStreamIn(player)

            verify { playerMapObject.addOnPlayerSelectPlayerMapObjectListener(streamableMapObject) }
        }

        @Test
        fun isStreamedInShouldReturnTrue() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            val isStreamedIn = streamableMapObject.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isTrue()
        }
    }

    @Nested
    inner class RefreshTests {

        private val player = mockk<Player>()
        private lateinit var currentState: StreamableMapObjectState
        private val oldPlayerMapObject = mockk<PlayerMapObject>(relaxed = true)

        @BeforeEach
        fun setUp() {
            currentState = mockk {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { oldPlayerMapObject.destroy() } just Runs
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { currentState.onStreamIn(any()) } just Runs
            every { playerMapObject.player } returns player
            every { playerMapObject.addOnPlayerEditPlayerMapObjectListener(any()) } just Runs
            every { playerMapObject.addOnPlayerSelectPlayerMapObjectListener(any()) } just Runs
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns oldPlayerMapObject
            streamableMapObject.onStreamIn(player)
            clearMocks(playerMapObjectService)
        }

        @Test
        fun shouldBeRecreated() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject

            streamableMapObject.refresh()

            verify {
                playerMapObjectService.createPlayerMapObject(
                        player = player,
                        modelId = modelId,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        drawDistance = streamDistance
                )
            }
        }

        @Test
        fun givenMaterialTextItShouldSetIt() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObject.setMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } just Runs
            streamableMapObject.setMaterialText(
                    text = "Hi there",
                    size = ObjectMaterialSize.SIZE_128X128,
                    index = 1,
                    backColor = Colors.BLUE,
                    fontColor = Colors.RED,
                    isBold = true,
                    fontFace = "Comic Sans",
                    textAlignment = ObjectMaterialTextAlignment.LEFT,
                    fontSize = 24
            )
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject

            streamableMapObject.refresh()

            verify {
                playerMapObject.setMaterialText(
                        text = "Hi there",
                        size = ObjectMaterialSize.SIZE_128X128,
                        index = 1,
                        backColor = Colors.BLUE,
                        fontColor = Colors.RED,
                        isBold = true,
                        fontFace = "Comic Sans",
                        textAlignment = ObjectMaterialTextAlignment.LEFT,
                        fontSize = 24
                )
            }
        }

        @Test
        fun givenMaterialItShouldSetIt() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObject.setMaterial(any(), any(), any(), any(), any())
            } just Runs
            streamableMapObject.setMaterial(
                    index = 1,
                    modelId = 815,
                    color = Colors.RED,
                    txdName = "txd",
                    textureName = "texture"
            )
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject

            streamableMapObject.refresh()

            verify {
                playerMapObject.setMaterial(
                        index = 1,
                        modelId = 815,
                        color = Colors.RED,
                        txdName = "txd",
                        textureName = "texture"
                )
            }
        }

        @Test
        fun givenCameraCollisionIsDisabledItShouldDisableItOnPlayerMapObject() {
            every { playerMapObject.disableCameraCollision() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.disableCameraCollision()

            streamableMapObject.refresh()

            verify { playerMapObject.disableCameraCollision() }
        }

        @Test
        fun shouldRegisterAsOnPlayerEditPlayerMapObjectListener() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.refresh()

            verify {
                playerMapObject.addOnPlayerEditPlayerMapObjectListener(streamableMapObject)
            }
        }

        @Test
        fun shouldRegisterAsOnPlayerSelectPlayerMapObjectListener() {
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.refresh()

            verify {
                playerMapObject.addOnPlayerSelectPlayerMapObjectListener(streamableMapObject)
            }
        }
    }

    @Nested
    inner class OnStreamOutTests {

        private val player = mockk<Player>()
        private val currentState = mockk<StreamableMapObjectState>()

        @BeforeEach
        fun setUp() {
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { currentState.onStreamIn(any()) } just Runs
            every { playerMapObject.player } returns player
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamOutReceiver.onStreamableMapObjectStreamOut(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(any(), any()) } just Runs
        }

        @Test
        fun shouldDestroyPlayerMapObject() {
            every { playerMapObject.destroy() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            streamableMapObject.onStreamOut(player)

            verify {
                playerMapObject.destroy()
            }
        }

        @Test
        fun isStreamedInShouldReturnFalse() {
            every { playerMapObject.destroy() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)
            streamableMapObject.onStreamOut(player)

            val isStreamedIn = streamableMapObject.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun shouldCallOnStreamOutHandlers() {
            every { playerMapObject.destroy() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            streamableMapObject.onStreamOut(player)

            verify {
                onStreamableMapObjectStreamOutReceiver.onStreamableMapObjectStreamOut(streamableMapObject, player)
                onStreamableMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(streamableMapObject, player)
            }
        }
    }

    @Test
    fun shouldGetBoundingBox() {
        val currentState = mockk<StreamableMapObjectState> {
            every { coordinates } returns vector3DOf(1f, 2f, 3f)
        }
        every { streamableMapObjectStateMachine.currentState } returns currentState
        val boundingBox = streamableMapObject.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(
                        Rect3d(
                                -74.0,
                                -73.0,
                                -72.0,
                                76.0,
                                77.0,
                                78.0
                        )
                )
    }

    @Nested
    inner class CoordinatesTests {

        @Test
        fun shouldReturnCoordinates() {
            val currentState = mockk<StreamableMapObjectState> {
                every { coordinates } returns vector3DOf(11f, 22f, 33f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val coordinates = streamableMapObject.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun setShouldTransitionToFixedCoordinatesState() {
            every { mapObjectStreamer.onBoundingBoxChange(any()) } just Runs
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.coordinates = newCoordinates

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(newCoordinates, rotation)
            }
        }

        @Test
        fun setShouldCallOnBoundingBoxChangeOnMapObjectStreamer() {
            every { mapObjectStreamer.onBoundingBoxChange(any()) } just Runs
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs
            val newCoordinates = vector3DOf(100f, 200f, 300f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns newCoordinates
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            streamableMapObject.coordinates = newCoordinates

            verify { mapObjectStreamer.onBoundingBoxChange(streamableMapObject) }
        }
    }

    @Nested
    inner class RotationTests {

        @Test
        fun shouldReturnRotation() {
            val currentState = mockk<StreamableMapObjectState> {
                every { rotation } returns vector3DOf(11f, 22f, 33f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val rotation = streamableMapObject.rotation

            assertThat(rotation)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun setShouldTransitionToFixedRotationState() {
            val newRotation = vector3DOf(123f, 456f, 789f)
            val coordinates = vector3DOf(11f, 22f, 33f)
            val currentState = mockk<StreamableMapObjectState> {
                every { this@mockk.coordinates } returns coordinates
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.rotation = newRotation

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(coordinates, newRotation)
            }
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
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { playerMapObject.disableCameraCollision() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            streamableMapObject.disableCameraCollision()

            verify {
                playerMapObject.disableCameraCollision()
            }
        }
    }

    @Test
    fun shouldSetMaterial() {
        every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        every {
            playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
        } returns playerMapObject
        every { playerMapObject.setMaterial(any(), any(), any(), any(), any()) } just Runs
        val currentState = mockk<StreamableMapObjectState> {
            every { onStreamIn(any()) } just Runs
            every { coordinates } returns vector3DOf(1f, 2f, 3f)
            every { rotation } returns vector3DOf(4f, 5f, 6f)
        }
        every { streamableMapObjectStateMachine.currentState } returns currentState
        streamableMapObject.onStreamIn(player)

        streamableMapObject.setMaterial(3, 69, "txd", "texture", Colors.RED)

        verify { playerMapObject.setMaterial(3, 69, "txd", "texture", Colors.RED) }
    }

    @Nested
    inner class SetMaterialTextTests {

        @BeforeEach
        fun setUp() {
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        }

        @Test
        fun shouldSetMaterialTextWithSimpleString() {
            every { player.locale } returns Locale.GERMANY
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every {
                playerMapObject.setMaterialText(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } just Runs
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
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
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
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
        every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
        every { playerMapObject.edit(any()) } just Runs
        every {
            playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
        } returns playerMapObject
        val currentState = mockk<StreamableMapObjectState> {
            every { onStreamIn(any()) } just Runs
            every { coordinates } returns vector3DOf(1f, 2f, 3f)
            every { rotation } returns vector3DOf(4f, 5f, 6f)
        }
        every { streamableMapObjectStateMachine.currentState } returns currentState
        streamableMapObject.onStreamIn(player)

        streamableMapObject.edit(player)

        verify { playerMapObject.edit(player) }
    }

    @Nested
    inner class MoveToTests {

        @BeforeEach
        fun setUp() {
            every { streamableMapObjectStateMachine.transitionToMoving(any(), any(), any(), any(), any()) } just Runs
        }

        @Test
        fun shouldTransitionToMovingState() {
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            val moving = mockk<StreamableMapObjectState.Moving> {
                every { duration } returns 1337L
            }
            every { streamableMapObjectStateMachine.currentState } returnsMany listOf(
                    currentState,
                    currentState,
                    currentState,
                    moving
            )

            streamableMapObject.moveTo(
                    coordinates = vector3DOf(123f, 456f, 789f),
                    speed = 7f,
                    rotation = vector3DOf(8f, 9f, 10f)
            )

            verify {
                streamableMapObjectStateMachine.transitionToMoving(
                        origin = vector3DOf(1f, 2f, 3f),
                        destination = vector3DOf(123f, 456f, 789f),
                        startRotation = vector3DOf(4f, 5f, 6f),
                        targetRotation = vector3DOf(8f, 9f, 10f),
                        speed = 7f
                )
            }
        }

        @Test
        fun shouldReturnMovementDuration() {
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            val moving = mockk<StreamableMapObjectState.Moving> {
                every { duration } returns 1337L
            }
            every { streamableMapObjectStateMachine.currentState } returnsMany listOf(
                    currentState,
                    currentState,
                    currentState,
                    moving
            )

            val duration = streamableMapObject.moveTo(
                    coordinates = vector3DOf(123f, 456f, 789f),
                    speed = 7f,
                    rotation = vector3DOf(8f, 9f, 10f)
            )

            assertThat(duration)
                    .isEqualTo(1337)
        }

        @Test
        fun givenCurrentStateIsAttachedToVehicleItShouldRemoveOnDestroyListener() {
            val vehicle = mockk<Vehicle> {
                every { removeOnDestroyListener(any()) } just Runs
            }
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
                every { this@mockk.vehicle } returns vehicle
            }
            val moving = mockk<StreamableMapObjectState.Moving> {
                every { duration } returns 1337L
            }
            every { streamableMapObjectStateMachine.currentState } returnsMany listOf(
                    currentState,
                    currentState,
                    currentState,
                    moving
            )

            streamableMapObject.moveTo(
                    coordinates = vector3DOf(123f, 456f, 789f),
                    speed = 7f,
                    rotation = vector3DOf(8f, 9f, 10f)
            )

            verify { vehicle.removeOnDestroyListener(streamableMapObject) }
        }

    }

    @Nested
    inner class IsMovingTests {

        @Test
        fun givenCurrentStateIsMovingItShouldReturnTrue() {
            val moving = mockk<StreamableMapObjectState.Moving>()
            every { streamableMapObjectStateMachine.currentState } returns moving

            val isMoving = streamableMapObject.isMoving

            assertThat(isMoving)
                    .isTrue()
        }

        @ParameterizedTest
        @ArgumentsSource(NonMovingStateProvider::class)
        fun givenCurrentStateIsNotMovingItShouldReturnFalse(currentState: StreamableMapObjectState) {
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val isMoving = streamableMapObject.isMoving

            assertThat(isMoving)
                    .isFalse()
        }

    }

    @Nested
    inner class OnMovedTests {

        @BeforeEach
        fun setUp() {
            every { onStreamableMapObjectMovedReceiver.onStreamableMapObjectMoved(any()) } just Runs
            every { onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(any()) } just Runs
        }

        @Test
        fun shouldCallOnStreamableMapObjectMovedHandler() {
            streamableMapObject.onMoved()

            verify { onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(streamableMapObject) }
        }

        @Test
        fun shouldCallOnStreamableMapObjectMovedReceiver() {
            streamableMapObject.onMoved()

            verify { onStreamableMapObjectMovedReceiver.onStreamableMapObjectMoved(streamableMapObject) }
        }

    }

    @Nested
    inner class StopTests {

        @Test
        fun givenItIsMovingItShouldTransitionToFixedCoordinates() {
            val moving = mockk<StreamableMapObjectState.Moving> {
                every { coordinates } returns vector3DOf(111f, 222f, 333f)
                every { rotation } returns vector3DOf(30f, 60f, 90f)
            }
            streamableMapObjectStateMachine.apply {
                every { transitionToFixedCoordinates(any(), any()) } just Runs
                every { currentState } returns moving
            }

            streamableMapObject.stop()

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(
                        coordinates = vector3DOf(111f, 222f, 333f),
                        rotation = vector3DOf(30f, 60f, 90f)
                )
            }
        }

        @Test
        fun givenItIsNotMovingItShouldDoNothing() {
            val currentState = mockk<StreamableMapObjectState>()
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val caughtThrowable = catchThrowable { streamableMapObject.stop() }

            assertThat(caughtThrowable)
                    .isNull()
        }

    }

    @Test
    fun shouldAttachToPlayer() {
        val vehicle = mockk<Vehicle> {
            every { removeOnDestroyListener(any()) } just Runs
        }
        val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
            every { this@mockk.vehicle } returns vehicle
        }
        every { streamableMapObjectStateMachine.currentState } returns currentState
        val player = mockk<Player>()
        every { streamableMapObjectStateMachine.transitionToAttachedToPlayer(any(), any(), any()) } just Runs
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)

        streamableMapObject.attachTo(
                player = player,
                offset = offset,
                rotation = rotation
        )

        verify {
            vehicle.removeOnDestroyListener(streamableMapObject)
            streamableMapObjectStateMachine.transitionToAttachedToPlayer(
                    player = player,
                    offset = offset,
                    rotation = rotation
            )
        }
    }

    @Test
    fun shouldAttachToVehicle() {
        val oldVehicle = mockk<Vehicle> {
            every { removeOnDestroyListener(any()) } just Runs
        }
        val oldCurrentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
            every { vehicle } returns oldVehicle
        }
        every { streamableMapObjectStateMachine.currentState } returns oldCurrentState
        val newVehicle = mockk<Vehicle> {
            every { addOnDestroyListener(any()) } just Runs
        }
        every { streamableMapObjectStateMachine.transitionToAttachedToVehicle(any(), any(), any()) } just Runs
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        streamableMapObject.attachTo(
                vehicle = newVehicle,
                offset = offset,
                rotation = rotation
        )

        verify {
            streamableMapObjectStateMachine.transitionToAttachedToVehicle(
                    vehicle = newVehicle,
                    offset = offset,
                    rotation = rotation
            )
            newVehicle.addOnDestroyListener(streamableMapObject)
        }
    }

    @Nested
    inner class IsAttachedTests {

        @ParameterizedTest
        @ArgumentsSource(NonAttachedStateProvider::class)
        fun givenCurrentStateIsNotAttachedItShouldReturnFalse(currentState: StreamableMapObjectState) {
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val isAttached = streamableMapObject.isAttached

            assertThat(isAttached)
                    .isFalse()
        }

        @ParameterizedTest
        @ArgumentsSource(AttachedStateProvider::class)
        fun givenCurrentStateIsAttachedItShouldReturnTrue(currentState: StreamableMapObjectState) {
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val isAttached = streamableMapObject.isAttached

            assertThat(isAttached)
                    .isTrue()
        }

    }

    @Nested
    inner class DetachTests {

        @Test
        fun givenItIsAttachedItShouldTransitionToFixedCoordinates() {
            val attached = mockk<StreamableMapObjectState.Attached> {
                every { coordinates } returns vector3DOf(111f, 222f, 333f)
                every { rotation } returns vector3DOf(30f, 60f, 90f)
            }
            streamableMapObjectStateMachine.apply {
                every { transitionToFixedCoordinates(any(), any()) } just Runs
                every { currentState } returns attached
            }

            streamableMapObject.detach()

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(
                        coordinates = vector3DOf(111f, 222f, 333f),
                        rotation = vector3DOf(30f, 60f, 90f)
                )
            }
        }

        @Test
        fun givenItIsNotAttachedItShouldDoNothing() {
            val currentState = mockk<StreamableMapObjectState>()
            every { streamableMapObjectStateMachine.currentState } returns currentState

            val caughtThrowable = catchThrowable { streamableMapObject.detach() }

            assertThat(caughtThrowable)
                    .isNull()
        }

    }

    @Nested
    inner class DistanceToTests {

        @Test
        fun shouldReturnDistanceToLocation() {
            val currentState = mockk<StreamableMapObjectState> {
                every { coordinates } returns vector3DOf(10f, 2f, 3f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)

            val distance = streamableMapObject.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, withPercentage(0.01))
        }

        @Test
        fun givenMapObjectIsNotInInteriorItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableMapObject.interiorIds = mutableSetOf(0)

            val distance = streamableMapObject.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenMapObjectIsNotInVirtualWorldItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableMapObject.virtualWorldIds = mutableSetOf(0)

            val distance = streamableMapObject.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

    }

    @Nested
    inner class OnPlayerDisconnectTests {

        @Test
        fun shouldNotBeStreamedInForPlayer() {
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onStreamIn(player)

            streamableMapObject.onPlayerDisconnect(player, DisconnectReason.QUIT)

            assertThat(streamableMapObject.isStreamedIn(player))
                    .isFalse()
        }

        @Test
        fun givenCurrentStateIsAttachedToPlayerItShouldTransitionToFixedCoordinates() {
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToPlayer> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.player } returns this@StreamableMapObjectImplTest.player
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onPlayerDisconnect(player, DisconnectReason.QUIT)

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(newCoordinates, rotation)
            }
        }

        @Test
        fun givenCurrentStateIsAttachedToAnotherPlayerItShouldNotTransitionToFixedCoordinates() {
            val otherPlayer = mockk<Player>()
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToPlayer> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.player } returns otherPlayer
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onPlayerDisconnect(player, DisconnectReason.QUIT)

            verify(exactly = 0) {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any())
            }
        }
    }

    @Nested
    inner class OnDestroyTests {

        private val vehicle = mockk<Vehicle>()

        @Test
        fun givenCurrentStateIsAttachedToVehicleItShouldTransitionToFixedCoordinates() {
            every { this@OnDestroyTests.vehicle.removeOnDestroyListener(any()) } just Runs
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.vehicle } returns this@OnDestroyTests.vehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onDestroy(vehicle)

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(newCoordinates, rotation)
            }
        }

        @Test
        fun givenCurrentStateIsAttachedToVehicleItUnregisterAsVehicleOnDestroyListener() {
            every { this@OnDestroyTests.vehicle.removeOnDestroyListener(any()) } just Runs
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.vehicle } returns this@OnDestroyTests.vehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onDestroy(vehicle)

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(newCoordinates, rotation)
                this@OnDestroyTests.vehicle.removeOnDestroyListener(streamableMapObject)
            }
        }

        @Test
        fun givenCurrentStateIsAttachedToAnotherVehicleItShouldNotTransitionToFixedCoordinates() {
            val otherVehicle = mockk<Vehicle>()
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.vehicle } returns otherVehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onDestroy(vehicle)

            verify(exactly = 0) {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any())
            }
        }
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun shouldDestroyPlayerMapObjects() {
            every { onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { playerMapObject.destroy() } just Runs
            val coordinates = vector3DOf(1f, 2f, 3f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns coordinates
                every { this@mockk.rotation } returns rotation
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)

            streamableMapObject.destroy()

            verify { playerMapObject.destroy() }
        }

        @Test
        fun shouldCallOnDestroyHandlers() {
            every { streamableMapObjectStateMachine.currentState } returns mockk()
            val onDestroy = mockk<StreamableMapObjectImpl.() -> Unit>(relaxed = true)
            streamableMapObject.onDestroy(onDestroy)

            streamableMapObject.destroy()

            verify { onDestroy.invoke(streamableMapObject) }
        }

        @Test
        fun givenStreamableMapObjectIsDestroyedItShouldCallOnDestroyHandlersOnlyOnce() {
            every { streamableMapObjectStateMachine.currentState } returns mockk()
            val onDestroy = mockk<StreamableMapObjectImpl.() -> Unit>(relaxed = true)
            streamableMapObject.onDestroy(onDestroy)

            streamableMapObject.destroy()
            streamableMapObject.destroy()

            verify(exactly = 1) { onDestroy.invoke(streamableMapObject) }
        }

        @Test
        fun givenCurrentStateIsAttachedToVehicleItShouldRemoveOnDestroyListeners() {
            val vehicle = mockk<Vehicle> {
                every { removeOnDestroyListener(any()) } just Runs
            }
            val state = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { this@mockk.vehicle } returns vehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns state

            streamableMapObject.destroy()

            verify { vehicle.removeOnDestroyListener(streamableMapObject) }
        }

    }

    @Nested
    inner class IsDestroyedTests {

        @Test
        fun shouldInitiallyReturnFalse() {
            val isDestroyed = streamableMapObject.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun givenDestroyWasCalledItShouldReturnTrue() {
            every { streamableMapObjectStateMachine.currentState } returns mockk()

            streamableMapObject.destroy()

            val isDestroyed = streamableMapObject.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

    }

    @Test
    fun shouldReturnBoundingBox() {
        val currentState = mockk<StreamableMapObjectState.Attached.ToPlayer> {
            every { this@mockk.coordinates } returns vector3DOf(200f, 300f, 400f)
        }
        every { streamableMapObjectStateMachine.currentState } returns currentState

        val boundingBox = streamableMapObject.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(125.0, 225.0, 325.0, 275.0, 375.0, 475.0))
    }

    @Nested
    inner class OnPlayerEditPlayerMapObjectTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerEditStreamableMapObjectReceiver
                        .onPlayerEditStreamableMapObject(any(), any(), any(), any(), any())
            } just Runs
            every {
                onPlayerEditStreamableMapObjectHandler
                        .onPlayerEditStreamableMapObject(any(), any(), any(), any(), any())
            } just Runs
        }

        @Test
        fun givenResponseIsFinalItShouldUpdateCoordinatesAndRotation() {
            val vehicle = mockk<Vehicle> {
                every { removeOnDestroyListener(any()) } just Runs
            }
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { this@mockk.vehicle } returns vehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { mapObjectStreamer.onBoundingBoxChange(any()) } just Runs
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onPlayerEditPlayerMapObject(
                    playerMapObject,
                    ObjectEditResponse.FINAL,
                    vector3DOf(11f, 22f, 33f),
                    vector3DOf(44f, 55f, 66f)
            )

            verify {
                vehicle.removeOnDestroyListener(streamableMapObject)
                streamableMapObjectStateMachine.transitionToFixedCoordinates(
                        coordinates = vector3DOf(11f, 22f, 33f),
                        rotation = vector3DOf(44f, 55f, 66f)
                )
                mapObjectStreamer.onBoundingBoxChange(streamableMapObject)
            }
        }

        @ParameterizedTest
        @EnumSource(ObjectEditResponse::class, mode = EnumSource.Mode.EXCLUDE, names = ["FINAL"])
        fun givenResponseIsNotFinalItShouldNotUpdateCoordinatesAndRotation(response: ObjectEditResponse) {
            every { mapObjectStreamer.onBoundingBoxChange(any()) } just Runs
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onPlayerEditPlayerMapObject(
                    playerMapObject,
                    response,
                    vector3DOf(11f, 22f, 33f),
                    vector3DOf(44f, 55f, 66f)
            )

            verify(exactly = 0) {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(
                        coordinates = vector3DOf(11f, 22f, 33f),
                        rotation = vector3DOf(44f, 55f, 66f)
                )
                mapObjectStreamer.onBoundingBoxChange(streamableMapObject)
            }
        }

        @Test
        fun shouldCallOnPlayerEditStreamableMapObjectHandler() {
            streamableMapObject.onPlayerEditPlayerMapObject(
                    playerMapObject = playerMapObject,
                    response = ObjectEditResponse.UPDATE,
                    offset = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f)
            )

            verify {
                onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                        player = player,
                        streamableMapObject = streamableMapObject,
                        response = ObjectEditResponse.UPDATE,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldCallOnPlayerEditStreamableMapObjectReceiver() {
            streamableMapObject.onPlayerEditPlayerMapObject(
                    playerMapObject = playerMapObject,
                    response = ObjectEditResponse.UPDATE,
                    offset = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f)
            )

            verify {
                onPlayerEditStreamableMapObjectReceiver.onPlayerEditStreamableMapObject(
                        player = player,
                        streamableMapObject = streamableMapObject,
                        response = ObjectEditResponse.UPDATE,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

    }

    @Nested
    inner class OnPlayerSelectPlayerMapObjectTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerSelectStreamableMapObjectReceiver
                        .onPlayerSelectStreamableMapObject(any(), any(), any(), any())
            } just Runs
            every {
                onPlayerSelectStreamableMapObjectHandler
                        .onPlayerSelectStreamableMapObject(any(), any(), any(), any())
            } just Runs
        }

        @Test
        fun shouldCallOnPlayerSelectStreamableMapObjectHandler() {
            streamableMapObject.onPlayerSelectPlayerMapObject(
                    playerMapObject,
                    1337,
                    vector3DOf(4f, 5f, 6f)
            )

            verify {
                onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(
                        player,
                        streamableMapObject,
                        1337,
                        vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldCallOnPlayerSelectStreamableMapObjectReceiver() {
            streamableMapObject.onPlayerSelectPlayerMapObject(
                    playerMapObject,
                    1337,
                    vector3DOf(4f, 5f, 6f)
            )

            verify {
                onPlayerSelectStreamableMapObjectReceiver.onPlayerSelectStreamableMapObject(
                        player,
                        streamableMapObject,
                        1337,
                        vector3DOf(4f, 5f, 6f)
                )
            }
        }

    }

    private class NonMovingStateProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(mockk<StreamableMapObjectState.FixedCoordinates>()),
                        Arguments.of(mockk<StreamableMapObjectState.Attached.ToVehicle>()),
                        Arguments.of(mockk<StreamableMapObjectState.Attached.ToPlayer>())
                )

    }

    private class NonAttachedStateProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(mockk<StreamableMapObjectState.FixedCoordinates>()),
                        Arguments.of(mockk<StreamableMapObjectState.Moving>())
                )

    }

    private class AttachedStateProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(mockk<StreamableMapObjectState.Attached.ToPlayer>()),
                        Arguments.of(mockk<StreamableMapObjectState.Attached.ToVehicle>()),
                        Arguments.of(mockk<StreamableMapObjectState.Attached>())
                )

    }

}