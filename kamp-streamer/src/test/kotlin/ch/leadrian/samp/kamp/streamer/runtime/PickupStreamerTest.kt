package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamablePickupImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamablePickupFactory
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

internal class PickupStreamerTest {

    private lateinit var pickupStreamer: PickupStreamer

    private val coordinatesBasedGlobalStreamerFactory = mockk<CoordinatesBasedGlobalStreamerFactory>()
    private val coordinatesBasedGlobalStreamer = mockk<CoordinatesBasedGlobalStreamer<StreamablePickupImpl, Rect3d>>()
    private val streamablePickupFactory = mockk<StreamablePickupFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedGlobalStreamerFactory.create<StreamablePickupImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedGlobalStreamer
        pickupStreamer = PickupStreamer(
                coordinatesBasedGlobalStreamerFactory,
                streamablePickupFactory
        )
    }

    @Test
    fun initializeShouldCreateCoordinatesBasedGlobalStreamer() {
        pickupStreamer.initialize()

        verify {
            coordinatesBasedGlobalStreamerFactory.create(
                    any<SpatialIndex3D<StreamablePickupImpl>>(),
                    SAMPConstants.MAX_PICKUPS
            )
        }
    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            pickupStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { coordinatesBasedGlobalStreamer.capacity = any() } just Runs

                pickupStreamer.capacity = 69

                verify { coordinatesBasedGlobalStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { coordinatesBasedGlobalStreamer.capacity } returns 187

                val capacity = pickupStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreatePickupTests {

            @BeforeEach
            fun setUp() {
                every { coordinatesBasedGlobalStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamablePickup() {
                val expectedStreamablePickup = mockk<StreamablePickupImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamablePickupFactory.create(
                            modelId = 1337,
                            coordinates = mutableVector3DOf(1f, 2f, 3f),
                            type = 69,
                            virtualWorldId = 13,
                            interiorIds = mutableSetOf(187),
                            streamDistance = 13.37f,
                            priority = 187,
                            pickupStreamer = pickupStreamer
                    )
                } returns expectedStreamablePickup

                val streamablePickup = pickupStreamer.createPickup(
                        modelId = 1337,
                        coordinates = mutableVector3DOf(1f, 2f, 3f),
                        type = 69,
                        virtualWorldId = 13,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 13.37f,
                        priority = 187
                )

                assertThat(streamablePickup)
                        .isEqualTo(expectedStreamablePickup)
            }

            @Test
            fun shouldAddCreatedPickupToCoordinatesBasedStreamer() {
                val streamablePickup = mockk<StreamablePickupImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamablePickupFactory.create(any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamablePickup

                pickupStreamer.createPickup(
                        modelId = 1337,
                        coordinates = mutableVector3DOf(1f, 2f, 3f),
                        type = 69,
                        virtualWorldId = 13,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 13.37f,
                        priority = 187
                )

                verify { coordinatesBasedGlobalStreamer.add(streamablePickup) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedGlobalStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            pickupStreamer.stream(streamLocations)

            verify { coordinatesBasedGlobalStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamablePickup = mockk<StreamablePickupImpl>()
            every { coordinatesBasedGlobalStreamer.onBoundingBoxChange(any()) } just Runs

            pickupStreamer.onBoundingBoxChange(streamablePickup)

            verify { coordinatesBasedGlobalStreamer.onBoundingBoxChange(streamablePickup) }
        }

    }

}