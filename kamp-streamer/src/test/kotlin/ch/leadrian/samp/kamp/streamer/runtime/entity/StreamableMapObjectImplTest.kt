package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateMachineFactory
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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
import java.util.*
import java.util.stream.Stream

internal class StreamableMapObjectImplTest {

    private lateinit var streamableMapObject: StreamableMapObjectImpl
    private val playerMapObjectService = mockk<PlayerMapObjectService>()
    private val onStreamableMapObjectMovedHandler = mockk<OnStreamableMapObjectMovedHandler>()
    private val onPlayerEditStreamableMapObjectHandler = mockk<OnPlayerEditStreamableMapObjectHandler>()
    private val onPlayerSelectStreamableMapObjectHandler = mockk<OnPlayerSelectStreamableMapObjectHandler>()
    private val onStreamMapObjectStreamInHandler = mockk<OnStreamableMapObjectStreamInHandler>()
    private val onStreamMapObjectStreamOutHandler = mockk<OnStreamableMapObjectStreamOutHandler>()
    private val textProvider = mockk<TextProvider>()
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
            streamableMapObjectStateMachineFactory.create(any(), initialCoordinates, initialRotation)
        } returns streamableMapObjectStateMachine
        player = mockk {
            every { id } returns playerId
        }
        playerMapObject = mockk {
            every { id } returns playerMapObjectId
            every { onEdit(any()) } just Runs
            every { onSelect(any()) } just Runs
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
                onStreamableMapObjectStreamInHandler = onStreamMapObjectStreamInHandler,
                onStreamableMapObjectStreamOutHandler = onStreamMapObjectStreamOutHandler,
                playerMapObjectService = playerMapObjectService,
                textProvider = textProvider,
                streamableMapObjectStateMachineFactory = streamableMapObjectStateMachineFactory
        )
    }

    @Nested
    inner class OnStreamInTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { playerMapObject.player } returns player
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
            val onStreamIn1 = mockk<StreamableMapObject.(Player) -> Unit>(relaxed = true)
            val onStreamIn2 = mockk<StreamableMapObject.(Player) -> Unit>(relaxed = true)
            streamableMapObject.onStreamIn(onStreamIn1)
            streamableMapObject.onStreamIn(onStreamIn2)

            streamableMapObject.onStreamIn(player)

            verify {
                onStreamIn1.invoke(streamableMapObject, player)
                onStreamIn2.invoke(streamableMapObject, player)
                onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(streamableMapObject, player)
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
        fun givenOnEditHandlersItShouldRegisterThem() {
            val onEdit = mockk<StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit>(relaxed = true)
            every {
                onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(any(), any(), any(), any(), any())
            } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onEdit(onEdit)

            streamableMapObject.onStreamIn(player)

            val slot = slot<PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit>()
            verify { playerMapObject.onEdit(capture(slot)) }
            slot.captured.invoke(
                    playerMapObject,
                    ObjectEditResponse.UPDATE,
                    vector3DOf(1f, 2f, 3f),
                    vector3DOf(4f, 5f, 6f)
            )
            verify {
                onEdit.invoke(
                        streamableMapObject,
                        player,
                        ObjectEditResponse.UPDATE,
                        vector3DOf(1f, 2f, 3f),
                        vector3DOf(4f, 5f, 6f)
                )
                onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                        player,
                        streamableMapObject,
                        ObjectEditResponse.UPDATE,
                        vector3DOf(1f, 2f, 3f),
                        vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun givenOnSelectHandlersItShouldRegisterThem() {
            val onSelect = mockk<StreamableMapObject.(Player, Int, Vector3D) -> Unit>(relaxed = true)
            every {
                onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(any(), any(), any(), any())
            } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onSelect(onSelect)

            streamableMapObject.onStreamIn(player)

            val slot = slot<PlayerMapObject.(Int, Vector3D) -> Unit>()
            verify { playerMapObject.onSelect(capture(slot)) }
            slot.captured.invoke(
                    playerMapObject,
                    modelId,
                    vector3DOf(4f, 5f, 6f)
            )
            verify {
                onSelect.invoke(
                        streamableMapObject,
                        player,
                        modelId,
                        vector3DOf(4f, 5f, 6f)
                )
                onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(
                        player,
                        streamableMapObject,
                        modelId,
                        vector3DOf(4f, 5f, 6f)
                )
            }
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
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
        fun givenOnEditHandlersItShouldRegisterThem() {
            val onEdit = mockk<StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit>(relaxed = true)
            every {
                onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(any(), any(), any(), any(), any())
            } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onEdit(onEdit)

            streamableMapObject.refresh()

            val slot = slot<PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit>()
            verify { playerMapObject.onEdit(capture(slot)) }
            slot.captured.invoke(
                    playerMapObject,
                    ObjectEditResponse.UPDATE,
                    vector3DOf(1f, 2f, 3f),
                    vector3DOf(4f, 5f, 6f)
            )
            verify {
                onEdit.invoke(
                        streamableMapObject,
                        player,
                        ObjectEditResponse.UPDATE,
                        vector3DOf(1f, 2f, 3f),
                        vector3DOf(4f, 5f, 6f)
                )
                onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                        player,
                        streamableMapObject,
                        ObjectEditResponse.UPDATE,
                        vector3DOf(1f, 2f, 3f),
                        vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun givenOnSelectHandlersItShouldRegisterThem() {
            val onSelect = mockk<StreamableMapObject.(Player, Int, Vector3D) -> Unit>(relaxed = true)
            every {
                onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(any(), any(), any(), any())
            } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { coordinates } returns vector3DOf(1f, 2f, 3f)
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            streamableMapObject.onSelect(onSelect)

            streamableMapObject.refresh()

            val slot = slot<PlayerMapObject.(Int, Vector3D) -> Unit>()
            verify { playerMapObject.onSelect(capture(slot)) }
            slot.captured.invoke(
                    playerMapObject,
                    modelId,
                    vector3DOf(4f, 5f, 6f)
            )
            verify {
                onSelect.invoke(
                        streamableMapObject,
                        player,
                        modelId,
                        vector3DOf(4f, 5f, 6f)
                )
                onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(
                        player,
                        streamableMapObject,
                        modelId,
                        vector3DOf(4f, 5f, 6f)
                )
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
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
            every { onStreamMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(any(), any()) } just Runs
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
            val onStreamOut1 = mockk<StreamableMapObject.(Player) -> Unit>(relaxed = true)
            val onStreamOut2 = mockk<StreamableMapObject.(Player) -> Unit>(relaxed = true)
            streamableMapObject.onStreamOut(onStreamOut1)
            streamableMapObject.onStreamOut(onStreamOut2)

            streamableMapObject.onStreamOut(player)

            verify {
                onStreamOut1.invoke(streamableMapObject, player)
                onStreamOut2.invoke(streamableMapObject, player)
                onStreamMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(streamableMapObject, player)
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
        fun setShouldCallOnBoundingBoxChanged() {
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs
            val newCoordinates = vector3DOf(100f, 200f, 300f)
            val currentState = mockk<StreamableMapObjectState> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { rotation } returns vector3DOf(4f, 5f, 6f)
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            val onBoundingBoxChanged = mockk<StreamableMapObjectImpl.(Rect3d) -> Unit>(relaxed = true)
            streamableMapObject.onBoundingBoxChanged(onBoundingBoxChanged)

            streamableMapObject.coordinates = newCoordinates

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
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
        every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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

        @Test
        fun shouldSetMaterialTextWithSimpleString() {
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
        every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
            every { streamableMapObjectStateMachine.currentState } returnsMany listOf(currentState, currentState, moving)

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
            every { streamableMapObjectStateMachine.currentState } returnsMany listOf(currentState, currentState, moving)

            val duration = streamableMapObject.moveTo(
                    coordinates = vector3DOf(123f, 456f, 789f),
                    speed = 7f,
                    rotation = vector3DOf(8f, 9f, 10f)
            )

            assertThat(duration)
                    .isEqualTo(1337)
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
            every { onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(any()) } just Runs
        }

        @Test
        fun shouldCallOnStreamableMapObjectMovedHandler() {
            streamableMapObject.onMoved()

            verify { onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(streamableMapObject) }
        }

        @Test
        fun shouldCallOnMovedHandlers() {
            val onMoved = mockk<StreamableMapObject.() -> Unit>(relaxed = true)
            streamableMapObject.onMoved(onMoved)

            streamableMapObject.onMoved()

            verify { onMoved.invoke(streamableMapObject) }
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
            streamableMapObjectStateMachine.transitionToAttachedToPlayer(
                    player = player,
                    offset = offset,
                    rotation = rotation
            )
        }
    }

    @Test
    fun shouldAttachToVehicle() {
        val vehicle = mockk<Vehicle>()
        every { streamableMapObjectStateMachine.transitionToAttachedToVehicle(any(), any(), any()) } just Runs
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        streamableMapObject.attachTo(
                vehicle = vehicle,
                offset = offset,
                rotation = rotation
        )

        verify {
            streamableMapObjectStateMachine.transitionToAttachedToVehicle(
                    vehicle = vehicle,
                    offset = offset,
                    rotation = rotation
            )
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
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
    inner class OnVehicleDestructionTests {

        private val vehicle = mockk<Vehicle>()

        @Test
        fun givenCurrentStateIsAttachedToVehicleItShouldTransitionToFixedCoordinates() {
            val newCoordinates = vector3DOf(123f, 456f, 789f)
            val rotation = vector3DOf(4f, 5f, 6f)
            val currentState = mockk<StreamableMapObjectState.Attached.ToVehicle> {
                every { onStreamIn(any()) } just Runs
                every { this@mockk.coordinates } returns newCoordinates
                every { this@mockk.rotation } returns rotation
                every { this@mockk.vehicle } returns this@OnVehicleDestructionTests.vehicle
            }
            every { streamableMapObjectStateMachine.currentState } returns currentState
            every { streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any()) } just Runs

            streamableMapObject.onVehicleDestruction(vehicle)

            verify {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(newCoordinates, rotation)
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

            streamableMapObject.onVehicleDestruction(vehicle)

            verify(exactly = 0) {
                streamableMapObjectStateMachine.transitionToFixedCoordinates(any(), any())
            }
        }
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun shouldDestroyPlayerMapObjects() {
            every { onStreamMapObjectStreamInHandler.onStreamableMapObjectStreamIn(any(), any()) } just Runs
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
            val onDestroy = mockk<StreamableMapObjectImpl.() -> Unit>(relaxed = true)
            streamableMapObject.onDestroy(onDestroy)

            streamableMapObject.destroy()

            verify { onDestroy.invoke(streamableMapObject) }
        }

        @Test
        fun givenStreamableMapObjectIsDestroyedItShouldCallOnDestroyHandlersOnlyOnce() {
            val onDestroy = mockk<StreamableMapObjectImpl.() -> Unit>(relaxed = true)
            streamableMapObject.onDestroy(onDestroy)

            streamableMapObject.destroy()
            streamableMapObject.destroy()

            verify(exactly = 1) { onDestroy.invoke(streamableMapObject) }
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