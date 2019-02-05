package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCheckpointImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableCheckpointFactory
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

internal class CheckpointStreamerTest {

    private lateinit var checkpointStreamer: CheckpointStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedPlayerStreamerFactory = mockk<CoordinatesBasedPlayerStreamerFactory>()
    private val coordinatesBasedPlayerStreamer = mockk<CoordinatesBasedPlayerStreamer<StreamableCheckpointImpl, Rect3d>>()
    private val streamableCheckpointFactory = mockk<StreamableCheckpointFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedPlayerStreamerFactory.create<StreamableCheckpointImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        checkpointStreamer = CheckpointStreamer(
                callbackListenerManager,
                coordinatesBasedPlayerStreamerFactory,
                streamableCheckpointFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateCoordinatesBasedPlayerStreamer() {
            checkpointStreamer.initialize()

            verify {
                coordinatesBasedPlayerStreamerFactory.create(any<SpatialIndex3D<StreamableCheckpointImpl>>(), 1)
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            checkpointStreamer.initialize()

            verify { callbackListenerManager.register(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            checkpointStreamer.initialize()
        }

        @Nested
        inner class CreateCheckpointTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.registerOnlyAs(any<KClass<*>>(), any(), any()) } just Runs
                every { coordinatesBasedPlayerStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableCheckpoint() {
                val expectedStreamableCheckpoint = mockk<StreamableCheckpointImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableCheckpointFactory.create(
                            coordinates = vector3DOf(150f, 100f, 20f),
                            size = 4f,
                            priority = 0,
                            streamDistance = 300f,
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456),
                            checkpointStreamer = checkpointStreamer
                    )
                } returns expectedStreamableCheckpoint

                val streamableCheckpoint = checkpointStreamer.createCheckpoint(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        size = 4f,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableCheckpoint)
                        .isEqualTo(expectedStreamableCheckpoint)
            }

            @Test
            fun shouldAddCreatedCheckpointToCoordinatesBasedStreamer() {
                val streamableCheckpoint = mockk<StreamableCheckpointImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableCheckpointFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returns streamableCheckpoint

                checkpointStreamer.createCheckpoint(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        size = 4f,
                        priority = 0,
                        streamDistance = 300f,
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { coordinatesBasedPlayerStreamer.add(streamableCheckpoint) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            checkpointStreamer.stream(streamLocations)

            verify { coordinatesBasedPlayerStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableCheckpoint = mockk<StreamableCheckpointImpl>()
            every { coordinatesBasedPlayerStreamer.onBoundingBoxChange(any()) } just Runs

            checkpointStreamer.onBoundingBoxChange(streamableCheckpoint)

            verify { coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamableCheckpoint) }
        }
    }
}