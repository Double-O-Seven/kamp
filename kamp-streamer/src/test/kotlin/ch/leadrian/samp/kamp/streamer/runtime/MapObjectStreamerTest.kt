package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDestructionListener
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.reflect.KClass

internal class MapObjectStreamerTest {

    private lateinit var mapObjectStreamer: MapObjectStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedPlayerStreamerFactory = mockk<CoordinatesBasedPlayerStreamerFactory>()
    private val coordinatesBasedPlayerStreamer = mockk<CoordinatesBasedPlayerStreamer<StreamableMapObjectImpl, Rect3d>>()
    private val streamableMapObjectFactory = mockk<StreamableMapObjectFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedPlayerStreamerFactory.create<StreamableMapObjectImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        mapObjectStreamer = MapObjectStreamer(
                callbackListenerManager,
                coordinatesBasedPlayerStreamerFactory,
                streamableMapObjectFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateCoordinatesBasedPlayerStreamer() {
            mapObjectStreamer.initialize()

            verify {
                coordinatesBasedPlayerStreamerFactory.create(any<SpatialIndex3D<StreamableMapObjectImpl>>(), SAMPConstants.MAX_OBJECTS - 1)
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            mapObjectStreamer.initialize()

            verify { callbackListenerManager.register(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            mapObjectStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity = any() } just Runs

                mapObjectStreamer.capacity = 69

                verify { coordinatesBasedPlayerStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity } returns 187

                val capacity = mapObjectStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreateMapObjectTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.registerOnlyAs(any<KClass<*>>(), any(), any()) } just Runs
                every { coordinatesBasedPlayerStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableMapObject() {
                val expectedStreamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(
                            modelId = 1337,
                            priority = 0,
                            streamDistance = 300f,
                            coordinates = vector3DOf(150f, 100f, 20f),
                            rotation = vector3DOf(1f, 2f, 3f),
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456),
                            mapObjectStreamer = mapObjectStreamer
                    )
                } returns expectedStreamableMapObject

                val streamableMapObject = mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableMapObject)
                        .isEqualTo(expectedStreamableMapObject)
            }

            @Test
            fun shouldRegisterCreatedMapObjectAsCallbackListener() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapObject

                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify {
                    callbackListenerManager.registerOnlyAs<OnPlayerDisconnectListener>(streamableMapObject)
                    callbackListenerManager.registerOnlyAs<OnVehicleDestructionListener>(streamableMapObject)
                }
            }

            @Test
            fun shouldRegisterOnDestroyListener() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapObject

                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify {
                    streamableMapObject.addOnDestroyListener(mapObjectStreamer)
                }
            }

            @Test
            fun shouldAddCreatedMapObjectToCoordinatesBasedStreamer() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapObject

                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { coordinatesBasedPlayerStreamer.add(streamableMapObject) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            mapObjectStreamer.stream(streamLocations)

            verify { coordinatesBasedPlayerStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableMapObject = mockk<StreamableMapObjectImpl>()
            every { coordinatesBasedPlayerStreamer.onBoundingBoxChange(any()) } just Runs

            mapObjectStreamer.onBoundingBoxChange(streamableMapObject)

            verify { coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamableMapObject) }
        }

        @Nested
        inner class OnStateChangeTests {

            @BeforeEach
            fun setUp() {
                every { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(any()) } just Runs
            }

            @ParameterizedTest
            @CsvSource(
                    "true, false",
                    "false, true",
                    "true, true"
            )
            fun givenObjectIsMovingOrAttachedItShouldCallDelegate(isMoving: Boolean, isAttached: Boolean) {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { this@mockk.isMoving } returns isMoving
                    every { this@mockk.isAttached } returns isAttached
                }

                mapObjectStreamer.onStateChange(streamableMapObject)

                verify { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamableMapObject) }
            }

            @Test
            fun givenObjectIsNeitherMovingNorAttachedItShouldNotCallDelegate() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { isMoving } returns false
                    every { isAttached } returns false
                }

                mapObjectStreamer.onStateChange(streamableMapObject)

                verify(exactly = 0) { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamableMapObject) }
            }
        }

        @Nested
        inner class OnDestroyTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.unregisterOnlyAs(any<KClass<*>>(), any()) } just Runs
            }

            @Test
            fun shouldUnregisterStreamableMapObjectAsCallbackListener() {
                val streamableMapObject = mockk<StreamableMapObjectImpl>()

                mapObjectStreamer.onDestroy(streamableMapObject)

                verify {
                    callbackListenerManager.unregisterOnlyAs<OnPlayerDisconnectListener>(streamableMapObject)
                    callbackListenerManager.unregisterOnlyAs<OnVehicleDestructionListener>(streamableMapObject)
                }
            }

            @Test
            fun givenDestroyableIsNotInstanceOfStreamableMapObjectImplItShouldDoNothing() {
                val destroyable = mockk<Destroyable>()

                mapObjectStreamer.onDestroy(destroyable)

                verify(exactly = 0) {
                    callbackListenerManager.unregisterOnlyAs<OnPlayerDisconnectListener>(any())
                    callbackListenerManager.unregisterOnlyAs<OnVehicleDestructionListener>(any())
                }
            }
        }

    }

}