package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelFactory
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
import java.util.Locale

internal class TextLabelStreamerTest {

    private lateinit var textLabelStreamer: TextLabelStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedPlayerStreamerFactory = mockk<CoordinatesBasedPlayerStreamerFactory>()
    private val coordinatesBasedPlayerStreamer = mockk<CoordinatesBasedPlayerStreamer<StreamableTextLabelImpl, Rect3d>>()
    private val streamableTextLabelFactory = mockk<StreamableTextLabelFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedPlayerStreamerFactory.create<StreamableTextLabelImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        textLabelStreamer = TextLabelStreamer(
                callbackListenerManager,
                coordinatesBasedPlayerStreamerFactory,
                streamableTextLabelFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateCoordinatesBasedPlayerStreamer() {
            textLabelStreamer.initialize()

            verify {
                coordinatesBasedPlayerStreamerFactory.create(
                        any<SpatialIndex3D<StreamableTextLabelImpl>>(),
                        SAMPConstants.MAX_3DTEXT_PLAYER
                )
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            textLabelStreamer.initialize()

            verify { callbackListenerManager.register(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            textLabelStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity = any() } just Runs

                textLabelStreamer.capacity = 69

                verify { coordinatesBasedPlayerStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { coordinatesBasedPlayerStreamer.capacity } returns 187

                val capacity = textLabelStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreateTextLabelTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.register(any()) } just Runs
                every { coordinatesBasedPlayerStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableTextLabel() {
                val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
                val textSupplier: (Locale) -> String = { "Test" }
                every {
                    streamableTextLabelFactory.create(
                            textSupplier = textSupplier,
                            color = Colors.RED,
                            testLOS = true,
                            priority = 0,
                            streamDistance = 300f,
                            coordinates = vector3DOf(150f, 100f, 20f),
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456),
                            textLabelStreamer = textLabelStreamer
                    )
                } returns expectedStreamableTextLabel

                val streamableTextLabel = textLabelStreamer.createTextLabel(
                        textSupplier = textSupplier,
                        color = Colors.RED,
                        testLOS = true,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableTextLabel)
                        .isEqualTo(expectedStreamableTextLabel)
            }

            @Test
            fun shouldRegisterCreatedTextLabelAsCallbackListener() {
                val streamableTextLabel = mockk<StreamableTextLabelImpl>()
                every {
                    streamableTextLabelFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableTextLabel

                textLabelStreamer.createTextLabel(
                        textSupplier = { "Test" },
                        color = Colors.RED,
                        testLOS = true,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { callbackListenerManager.register(streamableTextLabel) }
            }

            @Test
            fun shouldAddCreatedTextLabelToCoordinatesBasedStreamer() {
                val streamableTextLabel = mockk<StreamableTextLabelImpl>()
                every {
                    streamableTextLabelFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableTextLabel

                textLabelStreamer.createTextLabel(
                        textSupplier = { "Test" },
                        color = Colors.RED,
                        testLOS = true,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { coordinatesBasedPlayerStreamer.add(streamableTextLabel) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            textLabelStreamer.stream(streamLocations)

            verify { coordinatesBasedPlayerStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableTextLabel = mockk<StreamableTextLabelImpl>()
            every { coordinatesBasedPlayerStreamer.onBoundingBoxChange(any()) } just Runs

            textLabelStreamer.onBoundingBoxChange(streamableTextLabel)

            verify { coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamableTextLabel) }
        }

        @Nested
        inner class OnStateChangeTests {

            @BeforeEach
            fun setUp() {
                every { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(any()) } just Runs
            }

            @Test
            fun givenTextLabelIsAttachedItShouldRemoveItFromSpatialIndex() {
                val streamableTextLabel = mockk<StreamableTextLabelImpl> {
                    every { isAttached } returns true
                }

                textLabelStreamer.onStateChange(streamableTextLabel)

                verify { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamableTextLabel) }
            }

            @Test
            fun givenTextLabelIsNotAttachedItShouldNotRemoveItFromSpatialIndex() {
                val streamableTextLabel = mockk<StreamableTextLabelImpl> {
                    every { isAttached } returns false
                }

                textLabelStreamer.onStateChange(streamableTextLabel)

                verify(exactly = 0) { coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamableTextLabel) }
            }
        }

    }

}