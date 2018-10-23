package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage.withPercentage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

    @BeforeEach
    fun setUp() {
        streamableMapObject = StreamableMapObject(
                modelId = 1337,
                priority = 0,
                streamDistance = 75f,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                rotation = mutableVector3DOf(4f, 5f, 6f),
                interiorIds = mutableSetOf(1),
                virtualWorldIds = mutableSetOf(1337),
                onStreamableMapObjectMovedHandler = onStreamableMapObjectMovedHandler,
                onPlayerSelectStreamableMapObjectHandler = onPlayerSelectStreamableMapObjectHandler,
                onPlayerEditStreamableMapObjectHandler = onPlayerEditStreamableMapObjectHandler,
                playerMapObjectService = playerMapObjectService,
                textProvider = textProvider,
                streamableMapObjectStateFactory = streamableMapObjectStateFactory
        )
        player = mockk {
            every { id } returns playerId
        }
        playerMapObject = mockk {
            every { id } returns playerMapObjectId
            every { onEdit(any()) } just Runs
            every { onSelect(any()) } just Runs
            every { this@mockk.player } returns this@StreamableMapObjectTest.player
        }
    }

    @Test
    fun shouldGetBoundingBox() {
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
        fun shouldBeInitialized() {
            assertThat(streamableMapObject.coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @Test
        fun shouldUpdateWithNewValue() {
            streamableMapObject.coordinates = mutableVector3DOf(11f, 22f, 33f)

            assertThat(streamableMapObject.coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun shouldUpdatePlayerMapObject() {
            every { playerMapObject.coordinates = any() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)

            streamableMapObject.coordinates = mutableVector3DOf(11f, 22f, 33f)

            verify {
                playerMapObject.coordinates = vector3DOf(11f, 22f, 33f)
            }
        }

        @Test
        fun setShouldCallOnBoundingBoxChanged() {
            val onBoundingBoxChanged = mockk<StreamableMapObject.(Rect3d) -> Unit>(relaxed = true)
            streamableMapObject.onBoundingBoxChanged(onBoundingBoxChanged)

            streamableMapObject.coordinates = mutableVector3DOf(100f, 200f, 300f)

            val slot = slot<Rect3d>()
            verify {
                onBoundingBoxChanged.invoke(streamableMapObject, capture(slot))
            }
            assertThat(slot.captured)
                    .isEqualTo(Rect3d(
                            25f.toDouble(),
                            125f.toDouble(),
                            225f.toDouble(),
                            175f.toDouble(),
                            275f.toDouble(),
                            375f.toDouble()
                    ))
        }

        @ParameterizedTest
        @CsvSource(
                "1000, 2000, 500, 90, 10, 20, 30, 980, 2010, 530",
                "1000, 2000, 500, 180, 10, 20, 30, 990, 1980, 530",
                "1000, 2000, 500, 0, 10, 20, 30, 1010, 2020, 530"
        )
        fun givenItIsAttachedToPlayerItShouldReturnAbsolutePosition(
                x: Float,
                y: Float,
                z: Float,
                angle: Float,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                expectedX: Float,
                expectedY: Float,
                expectedZ: Float
        ) {
            every { player.isConnected } returns true
            every { player.coordinates } returns vector3DOf(x, y, z)
            every { player.angle } returns angle
            every { playerMapObject.coordinates = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.attachTo(player, vector3DOf(offsetX, offsetY, offsetZ), vector3DOf(0f, 0f, 0f))

            val coordinates = streamableMapObject.coordinates

            assertThat(coordinates.x)
                    .isCloseTo(expectedX, withPercentage(0.05))
            assertThat(coordinates.y)
                    .isCloseTo(expectedY, withPercentage(0.05))
            assertThat(coordinates.z)
                    .isCloseTo(expectedZ, withPercentage(0.05))
        }

        @ParameterizedTest
        @CsvSource(
                "1000, 2000, 500, 90, 10, 20, 30, 980, 2010, 530",
                "1000, 2000, 500, 180, 10, 20, 30, 990, 1980, 530",
                "1000, 2000, 500, 0, 10, 20, 30, 1010, 2020, 530"
        )
        fun givenItIsAttachedToVehicleItShouldReturnAbsolutePosition(
                x: Float,
                y: Float,
                z: Float,
                angle: Float,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                expectedX: Float,
                expectedY: Float,
                expectedZ: Float
        ) {
            val vehicle = mockk<Vehicle> {
                every { isDestroyed } returns false
                every { coordinates } returns vector3DOf(x, y, z)
                every { this@mockk.angle } returns angle
            }
            every { playerMapObject.coordinates = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.attachTo(vehicle, vector3DOf(offsetX, offsetY, offsetZ), vector3DOf(0f, 0f, 0f))

            val coordinates = streamableMapObject.coordinates

            assertThat(coordinates.x)
                    .isCloseTo(expectedX, withPercentage(0.05))
            assertThat(coordinates.y)
                    .isCloseTo(expectedY, withPercentage(0.05))
            assertThat(coordinates.z)
                    .isCloseTo(expectedZ, withPercentage(0.05))
        }

        @Test
        fun givenItIsAttachedSetShouldNotDoAnything() {
            every { player.isConnected } returns true
            every { playerMapObject.coordinates = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)
            val onBoundingBoxChanged = mockk<StreamableMapObject.(Rect3d) -> Unit>(relaxed = true)
            streamableMapObject.onBoundingBoxChanged(onBoundingBoxChanged)
            streamableMapObject.attachTo(player, vector3DOf(0f, 0f, 0f), vector3DOf(0f, 0f, 0f))

            streamableMapObject.coordinates = mutableVector3DOf(100f, 200f, 300f)

            verify(exactly = 0) {
                onBoundingBoxChanged.invoke(any(), any())
                playerMapObject.coordinates = any()
            }
        }

        @Test
        fun givenItIsMovingItShouldReturnCurrentCoordinates() {
            val timer = mockk<Timer>(relaxed = true)
            // Should be half way through, needs to travel distance of 3741.657387 units
            streamableMapObject.moveTo(vector3DOf(1001f, 2002f, 3003f), 935.41434675f)

            val coordinates = streamableMapObject.coordinates

            assertThat(coordinates.x)
                    .isCloseTo(501f, withPercentage(0.05))
            assertThat(coordinates.y)
                    .isCloseTo(1002f, withPercentage(0.05))
            assertThat(coordinates.z)
                    .isCloseTo(1503f, withPercentage(0.05))
        }

        @Test
        fun givenItIsMovingSetShouldCancelMovement() {
            val timer = mockk<Timer>(relaxed = true)
            streamableMapObject.moveTo(vector3DOf(1f, 2f, 3f), 7f)

            streamableMapObject.coordinates = mutableVector3DOf(11f, 22f, 33f)

            assertThat(streamableMapObject.isMoving)
                    .isFalse()
            verify { timer.stop() }
        }
    }

    @Nested
    inner class RotationTests {

        @Test
        fun shouldBeInitialized() {
            assertThat(streamableMapObject.rotation)
                    .isEqualTo(vector3DOf(4f, 5f, 6f))
        }

        @Test
        fun shouldUpdateWithNewValue() {
            streamableMapObject.rotation = mutableVector3DOf(11f, 22f, 33f)

            assertThat(streamableMapObject.rotation)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun shouldUpdatePlayerMapObject() {
            every { playerMapObject.rotation = any() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)

            streamableMapObject.rotation = mutableVector3DOf(11f, 22f, 33f)

            verify {
                playerMapObject.rotation = vector3DOf(11f, 22f, 33f)
            }
        }

        @Test
        fun givenItIsAttachedToPlayerItShouldReturnAbsolutePosition() {
            every { player.isConnected } returns true
            every { player.angle } returns 10f
            every { playerMapObject.rotation = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.attachTo(player, vector3DOf(1f, 2f, 3f), vector3DOf(44f, 55f, 66f))

            val rotation = streamableMapObject.rotation

            assertThat(rotation.x)
                    .isEqualTo(44f)
            assertThat(rotation.y)
                    .isEqualTo(55f)
            assertThat(rotation.z)
                    .isCloseTo(76f, withPercentage(0.05))
        }

        @Test
        fun givenItIsAttachedSetShouldNotDoAnything() {
            every { player.isConnected } returns true
            every { playerMapObject.rotation = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)
            streamableMapObject.attachTo(player, vector3DOf(0f, 0f, 0f), vector3DOf(0f, 0f, 0f))

            streamableMapObject.rotation = mutableVector3DOf(100f, 200f, 300f)

            verify(exactly = 0) {
                playerMapObject.rotation = any()
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
            every { playerMapObject.disableCameraCollision() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
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

    @Nested
    inner class AttachToTests {

        @Test
        fun shouldNotBeAttachedInitially() {
            assertThat(streamableMapObject.isAttached)
                    .isFalse()
        }

        @Test
        fun shouldAttachToPlayer() {
            val onAttach = mockk<StreamableMapObject.() -> Unit>(relaxed = true)
            every { player.isConnected } returns true
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.attachTo(player, vector3DOf(11f, 22f, 33f), vector3DOf(0f, 0f, 0f))

            assertThat(streamableMapObject.isAttached)
                    .isTrue()
            verify {
                playerMapObject.attachTo(player, vector3DOf(11f, 22f, 33f), vector3DOf(0f, 0f, 0f))
                onAttach.invoke(streamableMapObject)
            }
        }

        @Test
        fun shouldAttachToVehicle() {
            val onAttach = mockk<StreamableMapObject.() -> Unit>(relaxed = true)
            val vehicleId = VehicleId.valueOf(69)
            val vehicle = mockk<Vehicle> {
                every { id } returns vehicleId
                every { isDestroyed } returns false
            }
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            every { playerMapObject.attachTo(any<Vehicle>(), any(), any()) } just Runs
            streamableMapObject.onStreamIn(player)

            streamableMapObject.attachTo(vehicle, vector3DOf(11f, 22f, 33f), vector3DOf(0f, 0f, 0f))

            assertThat(streamableMapObject.isAttached)
                    .isTrue()
            verify {
                playerMapObject.attachTo(vehicle, vector3DOf(11f, 22f, 33f), vector3DOf(0f, 0f, 0f))
                onAttach.invoke(streamableMapObject)
            }
        }

        @Test
        fun shouldDetachFromPlayer() {
            every { player.isConnected } returns true
            every { player.coordinates } returns vector3DOf(1000f, 2000f, 500f)
            every { player.angle } returns 90f
            every { playerMapObject.coordinates = any() } just Runs
            every { playerMapObject.rotation = any() } just Runs
            every { playerMapObject.attachTo(any<Player>(), any(), any()) } just Runs
            every { playerMapObject.destroy() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)
            streamableMapObject.attachTo(player, vector3DOf(10f, 20f, 30f), vector3DOf(1f, 2f, 3f))

            streamableMapObject.detach()

            assertThat(streamableMapObject.isAttached)
                    .isFalse()
            assertThat(streamableMapObject.coordinates)
                    .isEqualTo(vector3DOf(980f, 2010f, 530f))
            assertThat(streamableMapObject.rotation)
                    .isEqualTo(vector3DOf(1f, 2f, 93f))
            verify { playerMapObject.destroy() }
        }

        @Test
        fun shouldDetachFromVehicle() {
            val vehicleId = VehicleId.valueOf(69)
            val vehicle = mockk<Vehicle> {
                every { id } returns vehicleId
                every { isDestroyed } returns false
                every { coordinates } returns vector3DOf(1000f, 2000f, 500f)
                every { angle } returns 90f
            }
            every { playerMapObject.coordinates = any() } just Runs
            every { playerMapObject.rotation = any() } just Runs
            every { playerMapObject.attachTo(any<Vehicle>(), any(), any()) } just Runs
            every { playerMapObject.destroy() } just Runs
            every {
                playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
            } returns playerMapObject
            streamableMapObject.onStreamIn(player)
            streamableMapObject.attachTo(vehicle, vector3DOf(10f, 20f, 30f), vector3DOf(1f, 2f, 3f))

            streamableMapObject.detach()

            assertThat(streamableMapObject.isAttached)
                    .isFalse()
            assertThat(streamableMapObject.coordinates)
                    .isEqualTo(vector3DOf(980f, 2010f, 530f))
            assertThat(streamableMapObject.rotation)
                    .isEqualTo(vector3DOf(1f, 2f, 93f))
            verify { playerMapObject.destroy() }
        }
    }

    @Test
    fun shouldEdit() {
        every { playerMapObject.edit(any()) } just Runs
        every {
            playerMapObjectService.createPlayerMapObject(any(), any(), any(), any(), any())
        } returns playerMapObject
        streamableMapObject.onStreamIn(player)

        streamableMapObject.edit(player)

        verify { playerMapObject.edit(player) }
    }

}