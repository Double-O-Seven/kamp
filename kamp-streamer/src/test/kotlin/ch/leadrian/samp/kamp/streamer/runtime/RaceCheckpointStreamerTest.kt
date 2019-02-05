package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRaceCheckpointImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableRaceCheckpointFactory
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

internal class RaceCheckpointStreamerTest {

    private lateinit var raceCheckpointStreamer: RaceCheckpointStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedPlayerStreamerFactory = mockk<CoordinatesBasedPlayerStreamerFactory>()
    private val coordinatesBasedPlayerStreamer = mockk<CoordinatesBasedPlayerStreamer<StreamableRaceCheckpointImpl, Rect3d>>()
    private val streamableRaceCheckpointFactory = mockk<StreamableRaceCheckpointFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedPlayerStreamerFactory.create<StreamableRaceCheckpointImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        raceCheckpointStreamer = RaceCheckpointStreamer(
                callbackListenerManager,
                coordinatesBasedPlayerStreamerFactory,
                streamableRaceCheckpointFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateCoordinatesBasedPlayerStreamer() {
            raceCheckpointStreamer.initialize()

            verify {
                coordinatesBasedPlayerStreamerFactory.create(any<SpatialIndex3D<StreamableRaceCheckpointImpl>>(), 1)
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            raceCheckpointStreamer.initialize()

            verify { callbackListenerManager.register(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            raceCheckpointStreamer.initialize()
        }

        @Nested
        inner class CreateRaceCheckpointTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.registerOnlyAs(any<KClass<*>>(), any(), any()) } just Runs
                every { coordinatesBasedPlayerStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableRaceCheckpoint() {
                val expectedStreamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableRaceCheckpointFactory.create(
                            coordinates = vector3DOf(150f, 100f, 20f),
                            size = 4f,
                            type = RaceCheckpointType.AIR_FINISH,
                            nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                            priority = 0,
                            streamDistance = 300f,
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456),
                            raceCheckpointStreamer = raceCheckpointStreamer
                    )
                } returns expectedStreamableRaceCheckpoint

                val streamableRaceCheckpoint = raceCheckpointStreamer.createRaceCheckpoint(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        size = 4f,
                        type = RaceCheckpointType.AIR_FINISH,
                        nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableRaceCheckpoint)
                        .isEqualTo(expectedStreamableRaceCheckpoint)
            }

            @Test
            fun shouldAddCreatedRaceCheckpointToCoordinatesBasedStreamer() {
                val streamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableRaceCheckpointFactory.create(
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any()
                    )
                } returns streamableRaceCheckpoint

                raceCheckpointStreamer.createRaceCheckpoint(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        size = 4f,
                        type = RaceCheckpointType.AIR_FINISH,
                        nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { coordinatesBasedPlayerStreamer.add(streamableRaceCheckpoint) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            raceCheckpointStreamer.stream(streamLocations)

            verify { coordinatesBasedPlayerStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl>()
            every { coordinatesBasedPlayerStreamer.onBoundingBoxChange(any()) } just Runs

            raceCheckpointStreamer.onBoundingBoxChange(streamableRaceCheckpoint)

            verify { coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamableRaceCheckpoint) }
        }
    }
}