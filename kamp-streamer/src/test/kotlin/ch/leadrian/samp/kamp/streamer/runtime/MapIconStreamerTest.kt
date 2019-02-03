package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapIconImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapIconFactory
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
import kotlin.reflect.KClass

internal class MapIconStreamerTest {

    private lateinit var mapIconStreamer: MapIconStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedPlayerStreamerFactory = mockk<CoordinatesBasedPlayerStreamerFactory>()
    private val coordinatesBasedPlayerStreamer = mockk<CoordinatesBasedPlayerStreamer<StreamableMapIconImpl, Rect3d>>()
    private val streamableMapIconFactory = mockk<StreamableMapIconFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedPlayerStreamerFactory.create<StreamableMapIconImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        mapIconStreamer = MapIconStreamer(
                callbackListenerManager,
                coordinatesBasedPlayerStreamerFactory,
                streamableMapIconFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateCoordinatesBasedPlayerStreamer() {
            mapIconStreamer.initialize()

            verify {
                coordinatesBasedPlayerStreamerFactory.create(any<SpatialIndex3D<StreamableMapIconImpl>>(), 100)
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            mapIconStreamer.initialize()

            verify { callbackListenerManager.register(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            mapIconStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity = any() } just Runs

                mapIconStreamer.capacity = 69

                verify { coordinatesBasedPlayerStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity } returns 187

                val capacity = mapIconStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreateMapIconTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.registerOnlyAs(any<KClass<*>>(), any(), any()) } just Runs
                every { coordinatesBasedPlayerStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableMapIcon() {
                val expectedStreamableMapIcon = mockk<StreamableMapIconImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapIconFactory.create(
                            coordinates = vector3DOf(150f, 100f, 20f),
                            type = MapIconType.AIR_YARD,
                            color = Colors.PINK,
                            style = MapIconStyle.LOCAL_CHECKPOINT,
                            priority = 0,
                            streamDistance = 300f,
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456),
                            mapIconStreamer = mapIconStreamer
                    )
                } returns expectedStreamableMapIcon

                val streamableMapIcon = mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableMapIcon)
                        .isEqualTo(expectedStreamableMapIcon)
            }

            @Test
            fun shouldRegisterCreatedMapIconAsCallbackListener() {
                val streamableMapIcon = mockk<StreamableMapIconImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapIconFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapIcon

                mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify {
                    callbackListenerManager.registerOnlyAs<OnPlayerDisconnectListener>(streamableMapIcon)
                }
            }

            @Test
            fun shouldRegisterOnDestroyListener() {
                val streamableMapIcon = mockk<StreamableMapIconImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapIconFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapIcon

                mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify {
                    streamableMapIcon.addOnDestroyListener(mapIconStreamer)
                }
            }

            @Test
            fun shouldAddCreatedMapIconToCoordinatesBasedStreamer() {
                val streamableMapIcon = mockk<StreamableMapIconImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableMapIconFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapIcon

                mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { coordinatesBasedPlayerStreamer.add(streamableMapIcon) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            mapIconStreamer.stream(streamLocations)

            verify { coordinatesBasedPlayerStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableMapIcon = mockk<StreamableMapIconImpl>()
            every { coordinatesBasedPlayerStreamer.onBoundingBoxChange(any()) } just Runs

            mapIconStreamer.onBoundingBoxChange(streamableMapIcon)

            verify { coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamableMapIcon) }
        }

        @Nested
        inner class OnDestroyTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.unregisterOnlyAs(any<KClass<*>>(), any()) } just Runs
            }

            @Test
            fun shouldUnregisterStreamableMapIconAsCallbackListener() {
                val streamableMapIcon = mockk<StreamableMapIconImpl>()

                mapIconStreamer.onDestroy(streamableMapIcon)

                verify {
                    callbackListenerManager.unregisterOnlyAs<OnPlayerDisconnectListener>(streamableMapIcon)
                }
            }

            @Test
            fun givenDestroyableIsNotInstanceOfStreamableMapIconImplItShouldDoNothing() {
                val destroyable = mockk<Destroyable>()

                mapIconStreamer.onDestroy(destroyable)

                verify(exactly = 0) {
                    callbackListenerManager.unregisterOnlyAs<OnPlayerDisconnectListener>(any())
                }
            }
        }

    }

}