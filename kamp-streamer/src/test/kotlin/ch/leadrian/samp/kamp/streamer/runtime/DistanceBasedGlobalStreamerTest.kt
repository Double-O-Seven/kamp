package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedGlobalStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

internal class DistanceBasedGlobalStreamerTest {

    private lateinit var distanceBasedGlobalStreamer: DistanceBasedGlobalStreamer<TestStreamable>

    private lateinit var asyncExecutor: AsyncExecutor
    private val maxCapacity = 50
    private val streamInCandidateSupplier = mockk<StreamInCandidateSupplier<TestStreamable>>()

    @BeforeEach
    fun setUp() {
        asyncExecutor = mockk {
            every { executeOnMainThread(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { computeOnMainThread(any<() -> Any>()) } answers {
                CompletableFuture.completedFuture(firstArg<() -> Any>().invoke())
            }
        }
        distanceBasedGlobalStreamer = DistanceBasedGlobalStreamer(
                maxCapacity,
                asyncExecutor,
                streamInCandidateSupplier
        )
    }

    @Test
    fun shouldStreamInStreamablesWithinDistance() {
        val streamable1 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamable2 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 50f,
                        coordinates = vector3DOf(120f, 180f, 40f)
                )
        )
        val streamable3 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(1000f, 0f, 20f)
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        assertAll(
                {
                    verify(exactly = 1) {
                        streamable1.onStreamIn()
                        streamable2.onStreamIn()
                    }
                },
                {
                    verify(exactly = 0) {
                        streamable1.onStreamOut()
                        streamable2.onStreamOut()
                        streamable3.onStreamIn()
                    }
                },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable1)).isTrue() },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable2)).isTrue() }
        )
    }

    @Test
    fun givenCapacityOfOneItShouldStreamInOnlyTheClosestStreamable() {
        distanceBasedGlobalStreamer.capacity = 1
        val streamable1 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamable2 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 50f,
                        coordinates = vector3DOf(120f, 180f, 40f)
                )
        )
        val streamable3 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(1000f, 0f, 20f)
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        assertAll(
                { verify(exactly = 1) { streamable2.onStreamIn() } },
                {
                    verify(exactly = 0) {
                        streamable1.onStreamIn()
                        streamable2.onStreamOut()
                        streamable3.onStreamIn()
                    }
                }
        )
    }

    @Test
    fun givenCapacityOfZeroItShouldStreamInNothing() {
        distanceBasedGlobalStreamer.capacity = 0
        val streamable1 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamable2 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 50f,
                        coordinates = vector3DOf(120f, 180f, 40f)
                )
        )
        val streamable3 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(1000f, 0f, 20f)
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        verify(exactly = 0) {
            streamable1.onStreamIn()
            streamable2.onStreamIn()
            streamable3.onStreamIn()
        }
    }

    @Test
    fun shouldCallBeforeStreamBeforeStreaming() {
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)
        val action = mockk<() -> Unit>(relaxed = true)
        distanceBasedGlobalStreamer.beforeStream(action)

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        verifyOrder {
            action.invoke()
            streamable.onStreamIn()
        }
    }

    @Test
    fun shouldNotStreamInDestroyedStreamable() {
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        isDestroyed = true
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        assertAll(
                {
                    verify(exactly = 0) {
                        streamable.onStreamIn()
                        streamable.onStreamOut()
                    }
                },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable)).isFalse() }
        )
    }

    @Test
    fun givenStreamableIsOutOfRangeItShouldStreamOut() {
        val player = mockk<Player>()
        val coordinates = vector3DOf(150f, 100f, 20f)
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = coordinates
                )
        )
        every { streamInCandidateSupplier.getStreamInCandidates(any()) } answers { Stream.of(streamable) }

        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(1000f, 200f, 50f, 1, 0))))

        assertAll(
                {
                    verifyOrder {
                        streamable.onStreamIn()
                        streamable.onStreamOut()
                    }
                },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable)).isFalse() }
        )
    }

    @Test
    fun givenStreamableIsNoLongerStreamInCandidateItShouldStreamOut() {
        val player = mockk<Player>()
        val coordinates = vector3DOf(150f, 100f, 20f)
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = coordinates
                )
        )
        every {
            streamInCandidateSupplier.getStreamInCandidates(any())
        } returnsMany listOf(Stream.of(streamable), Stream.empty())

        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))
        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))

        assertAll(
                {
                    verifyOrder {
                        streamable.onStreamIn()
                        streamable.onStreamOut()
                    }
                },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable)).isFalse() }
        )
    }

    @Test
    fun givenStreamableWasDestroyedItShouldStreamOut() {
        val player = mockk<Player>()
        val coordinates = vector3DOf(150f, 100f, 20f)
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = coordinates
                )
        )
        every {
            streamInCandidateSupplier.getStreamInCandidates(any())
        } answers { Stream.of(streamable) }

        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))
        streamable.destroy()
        distanceBasedGlobalStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))

        assertAll(
                { verify { streamable.onStreamIn() } },
                { verify(exactly = 0) { streamable.onStreamOut() } },
                { assertThat(distanceBasedGlobalStreamer.isStreamedIn(streamable)).isFalse() }
        )
    }

    @Test
    fun shouldStreamInStreamablesAccordingToPriority() {
        val closestCoordinates = vector3DOf(100f, 200f, 40f)
        val streamable1 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = closestCoordinates
                )
        )
        val farthestCoordinates = vector3DOf(150f, 200f, 50f)
        val streamable2 = spyk(
                TestStreamable(
                        priority = 1,
                        streamDistance = 300f,
                        coordinates = farthestCoordinates
                )
        )
        val middleCoordinates = vector3DOf(100f, 220f, 50f)
        val streamable3 = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = middleCoordinates
                )
        )
        val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)
        distanceBasedGlobalStreamer.capacity = 2

        distanceBasedGlobalStreamer.stream(listOf(streamLocation))

        verify(exactly = 1) {
            streamable2.onStreamIn()
            streamable1.onStreamIn()
        }
    }

    private class TestStreamable(
            private val coordinates: Vector3D,
            override val streamDistance: Float,
            override val priority: Int,
            override var isDestroyed: Boolean = false,
            isStreamedIn: Boolean = false
    ) : DistanceBasedGlobalStreamable, AbstractDestroyable() {

        override var isStreamedIn: Boolean = isStreamedIn
            private set

        override fun distanceTo(location: Location): Float = coordinates.distanceTo(location.toLocation())

        override fun onStreamIn() {
            isStreamedIn = true
        }

        override fun onStreamOut() {
            isStreamedIn = false
        }

        override fun onDestroy() {}

    }

}