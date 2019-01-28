package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutReceiverDelegate
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.data.Percentage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamablePickupImplTest {

    private lateinit var streamablePickup: StreamablePickupImpl

    private val pickupService: PickupService = mockk()
    private val onStreamablePickupStreamInHandler: OnStreamablePickupStreamInHandler = mockk()
    private val onStreamablePickupStreamOutHandler: OnStreamablePickupStreamOutHandler = mockk()
    private val onPlayerPickUpStreamablePickupHandler: OnPlayerPickUpStreamablePickupHandler = mockk()
    private val pickupStreamer: PickupStreamer = mockk()
    private val onStreamablePickupStreamInReceiver: OnStreamablePickupStreamInReceiverDelegate = mockk()
    private val onStreamablePickupStreamOutReceiver: OnStreamablePickupStreamOutReceiverDelegate = mockk()
    private val onPlayerPickUpStreamablePickupReceiver: OnPlayerPickUpStreamablePickupReceiverDelegate = mockk()

    @BeforeEach
    fun setUp() {
        streamablePickup = StreamablePickupImpl(
                modelId = 1337,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                type = 69,
                virtualWorldId = 13,
                interiorIds = mutableSetOf(187),
                streamDistance = 10f,
                priority = 187,
                pickupStreamer = pickupStreamer,
                pickupService = pickupService,
                onStreamablePickupStreamInHandler = onStreamablePickupStreamInHandler,
                onStreamablePickupStreamOutHandler = onStreamablePickupStreamOutHandler,
                onPlayerPickUpStreamablePickupHandler = onPlayerPickUpStreamablePickupHandler,
                onStreamablePickupStreamInReceiver = onStreamablePickupStreamInReceiver,
                onStreamablePickupStreamOutReceiver = onStreamablePickupStreamOutReceiver,
                onPlayerPickUpStreamablePickupReceiver = onPlayerPickUpStreamablePickupReceiver

        )
    }

    @Test
    fun shouldInitializeExtensionsWithStreamablePickupAsEntity() {
        val entity = streamablePickup.extensions.entity

        assertThat(entity)
                .isEqualTo(streamablePickup)
    }

    @Nested
    inner class ModelIdTests {

        @Test
        fun shouldInitializeModelId() {
            val model = streamablePickup.modelId

            assertThat(model)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldUpdateModelId() {
            streamablePickup.modelId = 127

            assertThat(streamablePickup.modelId)
                    .isEqualTo(127)
        }

    }

    @Nested
    inner class TypeTests {

        @Test
        fun shouldInitializeType() {
            val model = streamablePickup.type

            assertThat(model)
                    .isEqualTo(69)
        }

        @Test
        fun shouldUpdateType() {
            streamablePickup.type = 1234

            assertThat(streamablePickup.type)
                    .isEqualTo(1234)
        }

    }

    @Nested
    inner class CoordinatesTests {

        @BeforeEach
        fun setUp() {
            every { pickupStreamer.onBoundingBoxChange(any()) } just Runs
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
        }

        @Test
        fun shouldInitializeCoordinates() {
            val coordinates = streamablePickup.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @Test
        fun shouldUpdateCoordinates() {
            streamablePickup.coordinates = mutableVector3DOf(11f, 22f, 33f)

            assertThat(streamablePickup.coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun givenPickupIsStreamedInItUpdatePickup() {
            val oldPickup: Pickup = mockk(relaxed = true)
            val newPickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returnsMany listOf(oldPickup, newPickup)
            streamablePickup.onStreamIn()
            clearMocks(oldPickup)

            streamablePickup.coordinates = mutableVector3DOf(11f, 22f, 33f)

            verify {
                oldPickup.destroy()
                pickupService.createPickup(1337, 69, vector3DOf(11f, 22f, 33f), 13)
            }
        }

        @Test
        fun shouldCallOnBoundingBoxChange() {
            streamablePickup.coordinates = mutableVector3DOf(11f, 22f, 33f)

            verify { pickupStreamer.onBoundingBoxChange(streamablePickup) }
        }

    }

    @Nested
    inner class VirtualWorldIdTests {

        @Test
        fun shouldInitializeVirtualWorldId() {
            val virtualWorldId = streamablePickup.virtualWorldId

            assertThat(virtualWorldId)
                    .isEqualTo(13)
        }

        @Test
        fun shouldUpdateVirtualWorldId() {
            streamablePickup.virtualWorldId = 1234

            val virtualWorldId = streamablePickup.virtualWorldId
            assertThat(virtualWorldId)
                    .isEqualTo(1234)
        }

        @Test
        fun givenPickupIsStreamedInItUpdatePickup() {
            val oldPickup: Pickup = mockk(relaxed = true)
            val newPickup: Pickup = mockk(relaxed = true)
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
            every { pickupService.createPickup(any(), any(), any(), any()) } returnsMany listOf(oldPickup, newPickup)
            streamablePickup.onStreamIn()
            clearMocks(oldPickup)

            streamablePickup.virtualWorldId = 1234

            verify {
                oldPickup.destroy()
                pickupService.createPickup(1337, 69, vector3DOf(1f, 2f, 3f), 1234)
            }
        }

    }

    @Test
    fun isStreamedInShouldInitiallyBeFalse() {
        val isStreamedIn = streamablePickup.isStreamedIn

        assertThat(isStreamedIn)
                .isFalse()
    }

    @Nested
    inner class DistanceToTests {

        @Test
        fun shouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 187, worldId = 13)

            val distance = streamablePickup.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenPickupIsNotInInteriorItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 187, worldId = 13)
            streamablePickup.interiorIds = mutableSetOf(0)

            val distance = streamablePickup.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenPickupIsNotInVirtualWorldItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 187, worldId = 13)
            streamablePickup.virtualWorldId = 0

            val distance = streamablePickup.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

    }

    @Nested
    inner class OnStreamInTests {

        @BeforeEach
        fun setUp() {
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
        }

        @Test
        fun shouldStreamInPickup() {
            val pickup: Pickup = mockk {
                every { addOnPlayerPickUpPickupListener(any()) } just Runs
            }
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup

            streamablePickup.onStreamIn()

            verify {
                pickupService.createPickup(1337, 69, vector3DOf(1f, 2f, 3f), 13)
                pickup.addOnPlayerPickUpPickupListener(streamablePickup)
            }
        }

        @Test
        fun isStreamedInShouldBeTrueAfterOnStreamInWasCalled() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()

            val isStreamedIn = streamablePickup.isStreamedIn

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun givenPickupIsAlreadyStreamedInOnStreamInShouldThrowException() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()

            val caughtThrowable = catchThrowable { streamablePickup.onStreamIn() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Pickup is already streamed in")
        }

        @Test
        fun shouldCallOnStreamablePickupStreamInReceiver() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup

            streamablePickup.onStreamIn()

            verify { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(streamablePickup) }
        }

        @Test
        fun shouldCallOnStreamablePickupStreamInHandler() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup

            streamablePickup.onStreamIn()

            verify { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(streamablePickup) }
        }

    }

    @Nested
    inner class OnStreamOutTests {

        @BeforeEach
        fun setUp() {
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamOutHandler.onStreamablePickupStreamOut(any()) } just Runs
            every { onStreamablePickupStreamOutReceiver.onStreamablePickupStreamOut(any()) } just Runs
        }

        @Test
        fun shouldStreamInPickup() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()
            clearMocks(pickup)

            streamablePickup.onStreamOut()

            verify {
                pickup.destroy()
                pickup.removeOnPlayerPickUpPickupListener(streamablePickup)
            }
        }

        @Test
        fun isStreamedInShouldBeFalseAfterOnStreamOutWasCalled() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()
            streamablePickup.onStreamOut()

            val isStreamedIn = streamablePickup.isStreamedIn

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun givenPickupIsAlreadyStreamedOutOnStreamOutShouldThrowException() {
            val caughtThrowable = catchThrowable { streamablePickup.onStreamOut() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Pickup was not streamed in")
        }

        @Test
        fun shouldCallOnStreamablePickupStreamOutReceiver() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()

            streamablePickup.onStreamOut()

            verify { onStreamablePickupStreamOutReceiver.onStreamablePickupStreamOut(streamablePickup) }
        }

        @Test
        fun shouldCallOnStreamablePickupStreamOutHandler() {
            val pickup: Pickup = mockk(relaxed = true)
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()

            streamablePickup.onStreamOut()

            verify { onStreamablePickupStreamOutHandler.onStreamablePickupStreamOut(streamablePickup) }
        }

    }

    @Nested
    inner class OnPlayerPickUpPickupTests {

        private val pickup: Pickup = mockk(relaxed = true)
        private val player: Player = mockk()

        @BeforeEach
        fun setUp() {
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
            every {
                onPlayerPickUpStreamablePickupReceiver.onPlayerPickUpStreamablePickup(any(), any())
            } just Runs
            every {
                onPlayerPickUpStreamablePickupHandler.onPlayerPickUpStreamablePickup(any(), any())
            } just Runs
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()
        }

        @Test
        fun shouldCallOnPlayerPickUpStreamablePickupReceiver() {
            streamablePickup.onPlayerPickUpPickup(player, pickup)

            verify {
                onPlayerPickUpStreamablePickupReceiver.onPlayerPickUpStreamablePickup(player, streamablePickup)
            }
        }

        @Test
        fun shouldCallOnPlayerPickUpStreamablePickupHandler() {
            streamablePickup.onPlayerPickUpPickup(player, pickup)

            verify {
                onPlayerPickUpStreamablePickupHandler.onPlayerPickUpStreamablePickup(player, streamablePickup)
            }
        }

        @Test
        fun givenStreamedInPickupIsAnotherPickupItShouldThrowException() {
            val otherPickup: Pickup = mockk(relaxed = true)

            val caughtThrowable = catchThrowable {
                streamablePickup.onPlayerPickUpPickup(player, otherPickup)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Pickup is not the streamed in pickup")
        }

    }

    @Test
    fun shouldReturnBoundingBox() {
        every { pickupStreamer.onBoundingBoxChange(any()) } just Runs
        streamablePickup.coordinates = vector3DOf(1f, 2f, 3f)

        val boundingBox = streamablePickup.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(-9.0, -8.0, -7.0, 11.0, 12.0, 13.0))
    }

    @Nested
    inner class OnDestroyTests {

        private val pickup: Pickup = mockk(relaxed = true)

        @BeforeEach
        fun setUp() {
            every { onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(any()) } just Runs
            every { onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(any()) } just Runs
            every { pickupService.createPickup(any(), any(), any(), any()) } returns pickup
            streamablePickup.onStreamIn()
        }

        @Test
        fun shouldDestroyPickup() {
            streamablePickup.destroy()

            verify { pickup.destroy() }
        }

        @Test
        fun shouldRemoveCallbackListener() {
            streamablePickup.destroy()

            verify {
                pickup.removeOnPlayerPickUpPickupListener(streamablePickup)
            }
        }

        @Test
        fun shouldDestroyExtensions() {
            streamablePickup.destroy()

            val isDestroyed = streamablePickup.extensions.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }
    }
}