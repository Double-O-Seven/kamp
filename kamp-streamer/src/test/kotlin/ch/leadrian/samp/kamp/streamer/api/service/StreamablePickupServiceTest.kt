package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamablePickupImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamablePickupServiceTest {

    private lateinit var streamablePickupService: StreamablePickupService
    private val pickupStreamer: PickupStreamer = mockk()

    @BeforeEach
    fun setUp() {
        streamablePickupService = StreamablePickupService(pickupStreamer)
    }

    @Nested
    inner class CreateStreamablePickupTests {

        private val expectedStreamablePickup: StreamablePickupImpl = mockk()

        @Test
        fun shouldCreateStreamablePickupUsingVector3DAndSetOfInteriorIds() {
            every {
                pickupStreamer.createPickup(
                        modelId = 127,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        type = 4,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamablePickup

            val streamablePickup = streamablePickupService.createStreamablePickup(
                    modelId = 127,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    type = 4,
                    virtualWorldId = 69,
                    interiorIds = mutableSetOf(187),
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamablePickup)
                    .isEqualTo(expectedStreamablePickup)
        }

        @Test
        fun shouldCreateStreamablePickupUsingVector3DAndInteriorId() {
            every {
                pickupStreamer.createPickup(
                        modelId = 127,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        type = 4,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamablePickup

            val streamablePickup = streamablePickupService.createStreamablePickup(
                    modelId = 127,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    type = 4,
                    virtualWorldId = 69,
                    interiorId = 187,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamablePickup)
                    .isEqualTo(expectedStreamablePickup)
        }

        @Test
        fun shouldCreateStreamablePickupUsingLocation() {
            every {
                pickupStreamer.createPickup(
                        modelId = 127,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        type = 4,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamablePickup

            val streamablePickup = streamablePickupService.createStreamablePickup(
                    modelId = 127,
                    location = locationOf(
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            interiorId = 187,
                            worldId = 69
                    ),
                    type = 4,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamablePickup)
                    .isEqualTo(expectedStreamablePickup)
        }
    }

    @Test
    fun shouldSetMaxStreamedInPickups() {
        every { pickupStreamer.capacity = any() } just Runs

        streamablePickupService.setMaxStreamedInPickups(500)

        verify { pickupStreamer.capacity = 500 }
    }

}