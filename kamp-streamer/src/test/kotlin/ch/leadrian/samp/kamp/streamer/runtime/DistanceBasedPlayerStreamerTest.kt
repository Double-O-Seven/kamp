package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

internal class DistanceBasedPlayerStreamerTest {

    private lateinit var distanceBasedPlayerStreamer: DistanceBasedPlayerStreamer<TestStreamable>

    private lateinit var asyncExecutor: AsyncExecutor
    private val playerService = mockk<PlayerService>()
    private val maxCapacity = 50
    private val streamInCandidateSupplier = mockk<StreamInCandidateSupplier<TestStreamable>>()

    @BeforeEach
    fun setUp() {
        every { playerService.getMaxPlayers() } returns 50
        asyncExecutor = mockk {
            every { executeOnMainThread(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { computeOnMainThread(any<() -> Any>()) } answers {
                CompletableFuture.completedFuture(firstArg<() -> Any>().invoke())
            }
        }
        distanceBasedPlayerStreamer = DistanceBasedPlayerStreamer(
                maxCapacity,
                asyncExecutor,
                streamInCandidateSupplier,
                playerService
        )
    }

    @Test
    fun shouldStreamInStreamablesWithinDistance() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 1) {
            streamable1.onStreamIn(player)
            streamable2.onStreamIn(player)
        }
        verify(exactly = 0) {
            streamable1.onStreamOut(player)
            streamable2.onStreamOut(player)
            streamable3.onStreamIn(player)
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable1, player))
                .isTrue()
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable2, player))
                .isTrue()
    }

    @Test
    fun givenCapacityOfOneItShouldStreamInOnlyTheClosestStreamable() {
        distanceBasedPlayerStreamer.capacity = 1
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 1) {
            streamable2.onStreamIn(player)
        }
        verify(exactly = 0) {
            streamable1.onStreamIn(player)
            streamable2.onStreamOut(player)
            streamable3.onStreamIn(player)
        }
    }

    @Test
    fun givenCapacityOfZeroItShouldStreamInNothing() {
        distanceBasedPlayerStreamer.capacity = 0
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 0) {
            streamable1.onStreamIn(any())
            streamable2.onStreamIn(any())
            streamable3.onStreamIn(any())
        }
    }

    @Test
    fun shouldNotBeStreamedInAfterOnPlayerDisconnect() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)
        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        distanceBasedPlayerStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT)

        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun shouldCallBeforeStreamBeforeStreaming() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f)
                )
        )
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)
        val action = mockk<() -> Unit>(relaxed = true)
        distanceBasedPlayerStreamer.beforeStream(action)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verifyOrder {
            action.invoke()
            streamable.onStreamIn(player)
        }
    }

    @Test
    fun shouldNotStreamInDestroyedStreamable() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        isDestroyed = true
                )
        )
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 0) {
            streamable.onStreamIn(any())
            streamable.onStreamOut(any())
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun shouldNotStreamInAlreadyStreamedInStreamable() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        streamedInForPlayers = *arrayOf(player)
                )
        )
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable)

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 0) {
            streamable.onStreamIn(any())
            streamable.onStreamOut(any())
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun givenStreamableIsOutOfRangeItShouldStreamOut() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
        val coordinates = vector3DOf(150f, 100f, 20f)
        val streamable = spyk(
                TestStreamable(
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = coordinates
                )
        )
        every { streamInCandidateSupplier.getStreamInCandidates(any()) } answers { Stream.of(streamable) }

        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))))
        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(1000f, 200f, 50f, 1, 0))))

        verifyOrder {
            streamable.onStreamIn(player)
            streamable.onStreamOut(player)
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun givenStreamableIsNoLongerStreamInCandidateItShouldStreamOut() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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

        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))
        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))

        verifyOrder {
            streamable.onStreamIn(player)
            streamable.onStreamOut(player)
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun givenStreamableWasDestroyedItShouldStreamOut() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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

        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))
        streamable.destroy()
        distanceBasedPlayerStreamer.stream(listOf(StreamLocation(player, locationOf(coordinates, 1, 0))))

        verify {
            streamable.onStreamIn(player)
        }
        verify(exactly = 0) {
            streamable.onStreamOut(player)
        }
        assertThat(distanceBasedPlayerStreamer.isStreamedIn(streamable, player))
                .isFalse()
    }

    @Test
    fun shouldStreamInStreamablesAccordingToPriority() {
        val player = mockk<Player> {
            every { isConnected } returns true
        }
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
        val streamLocation = StreamLocation(player, locationOf(100f, 200f, 50f, 1, 0))
        every {
            streamInCandidateSupplier.getStreamInCandidates(streamLocation)
        } returns Stream.of(streamable1, streamable2, streamable3)
        distanceBasedPlayerStreamer.capacity = 2

        distanceBasedPlayerStreamer.stream(listOf(streamLocation))

        verify(exactly = 1) {
            streamable2.onStreamIn(player)
            streamable1.onStreamIn(player)
        }
    }

    private class TestStreamable(
            private val coordinates: Vector3D,
            override val streamDistance: Float,
            override val priority: Int,
            override var isDestroyed: Boolean = false,
            vararg streamedInForPlayers: Player
    ) : DistanceBasedPlayerStreamable, AbstractDestroyable() {

        private val isStreamedIn: MutableMap<Player, Boolean> = mutableMapOf()

        init {
            streamedInForPlayers.forEach {
                isStreamedIn[it] = true
            }
        }

        override fun distanceTo(location: Location): Float = coordinates.distanceTo(location.toLocation())

        override fun onStreamIn(forPlayer: Player) {
            isStreamedIn[forPlayer] = true
        }

        override fun onStreamOut(forPlayer: Player) {
            isStreamedIn[forPlayer] = false
        }

        override fun isStreamedIn(forPlayer: Player): Boolean = isStreamedIn[forPlayer] ?: false

        override fun onDestroy() {}

    }

}