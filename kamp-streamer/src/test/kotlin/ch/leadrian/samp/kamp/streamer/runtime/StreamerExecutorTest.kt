package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import ch.leadrian.samp.kamp.streamer.api.Streamer
import ch.leadrian.samp.kamp.streamer.api.entity.StreamLocation
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

internal class StreamerExecutorTest {

    private lateinit var streamerExecutor: StreamerExecutor
    private val streamer1 = mockk<Streamer>(relaxed = true)
    private val streamer2 = mockk<Streamer>(relaxed = true)
    private val executorServiceFactory = mockk<ExecutorServiceFactory>()
    private val asyncExecutor = TestAsyncExecutor()
    private val playerService = mockk<PlayerService>()

    @BeforeEach
    fun setUp() {
        streamerExecutor = StreamerExecutor(
                setOf(streamer1, streamer2),
                executorServiceFactory,
                asyncExecutor,
                playerService
        )
    }

    @Test
    fun initializeShouldScheduleAtFixedRate() {
        val scheduledFuture = mockk<ScheduledFuture<*>>()
        val scheduledExecutorService = mockk<ScheduledExecutorService> {
            every { scheduleAtFixedRate(any(), any(), any(), any()) } returns scheduledFuture
        }
        every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService

        streamerExecutor.initialize()

        verify { scheduledExecutorService.scheduleAtFixedRate(any(), 1000, 1000, TimeUnit.MILLISECONDS) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shutdownShouldShutdownExecutorServiceAndAwaitTermination(terminated: Boolean) {
        val scheduledFuture = mockk<ScheduledFuture<*>>()
        val scheduledExecutorService = mockk<ScheduledExecutorService> {
            every { scheduleAtFixedRate(any(), any(), any(), any()) } returns scheduledFuture
            every { shutdown() } just Runs
            every { awaitTermination(any(), any()) } returns terminated
        }
        every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService
        streamerExecutor.initialize()

        streamerExecutor.shutdown()

        verifyOrder {
            scheduledExecutorService.shutdown()
            scheduledExecutorService.awaitTermination(3, TimeUnit.MINUTES)
        }
    }

    @Nested
    inner class ExecuteTests {

        @Test
        fun scheduleAtFixedRateShouldExecuteStreaming() {
            val location = locationOf(1f, 2f, 3f, 4, 5)
            val player = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { this@mockk.location } returns location
            }
            every { playerService.getAllPlayers() } returns listOf(player)
            val scheduledFuture = mockk<ScheduledFuture<*>>()
            val scheduledExecutorService = mockk<ScheduledExecutorService> {
                every { scheduleAtFixedRate(any(), any(), any(), any()) } answers {
                    firstArg<Runnable>().run()
                    scheduledFuture
                }
            }
            every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService

            streamerExecutor.initialize()

            verify {
                streamer1.stream(listOf(StreamLocation(player, location)))
                streamer2.stream(listOf(StreamLocation(player, location)))
            }
        }

        @ParameterizedTest
        @EnumSource(PlayerState::class, mode = Mode.EXCLUDE, names = ["NONE", "WASTED"])
        fun shouldStreamForAllPlayersInAllowedState(state: PlayerState) {
            val location1 = locationOf(1f, 2f, 3f, 4, 5)
            val player1 = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns state
                every { location } returns location1
            }
            val location2 = locationOf(6f, 7f, 8f, 9, 10)
            val player2 = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns state
                every { location } returns location2
            }
            every { playerService.getAllPlayers() } returns listOf(player1, player2)

            streamerExecutor.execute()

            verify(exactly = 1) {
                streamer1.stream(listOf(StreamLocation(player1, location1), StreamLocation(player2, location2)))
                streamer2.stream(listOf(StreamLocation(player1, location1), StreamLocation(player2, location2)))
            }
        }

        @Test
        fun shouldOnlyStreamForHumanPlayers() {
            val location1 = locationOf(1f, 2f, 3f, 4, 5)
            val player1 = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { location } returns location1
            }
            val location2 = locationOf(6f, 7f, 8f, 9, 10)
            val player2 = mockk<Player> {
                every { isHuman } returns false
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { location } returns location2
            }
            every { playerService.getAllPlayers() } returns listOf(player1, player2)

            streamerExecutor.execute()

            verify(exactly = 1) {
                streamer1.stream(listOf(StreamLocation(player1, location1)))
                streamer2.stream(listOf(StreamLocation(player1, location1)))
            }
        }

        @ParameterizedTest
        @EnumSource(PlayerState::class, mode = Mode.INCLUDE, names = ["NONE", "WASTED"])
        fun givenNotStreamableStateItShouldNotStreamPlayer(state: PlayerState) {
            val location1 = locationOf(1f, 2f, 3f, 4, 5)
            val player1 = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { location } returns location1
            }
            val location2 = locationOf(6f, 7f, 8f, 9, 10)
            val player2 = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns state
                every { location } returns location2
            }
            every { playerService.getAllPlayers() } returns listOf(player1, player2)

            streamerExecutor.execute()

            verify(exactly = 1) {
                streamer1.stream(listOf(StreamLocation(player1, location1)))
                streamer2.stream(listOf(StreamLocation(player1, location1)))
            }
        }

        @Test
        fun givenStreamerThrowsExceptionItShouldCatchIt() {
            val location = locationOf(1f, 2f, 3f, 4, 5)
            val player = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { this@mockk.location } returns location
            }
            every { playerService.getAllPlayers() } returns listOf(player)
            every { streamer1.stream(any()) } throws RuntimeException()

            val caughtException = catchThrowable { streamerExecutor.execute() }

            assertThat(caughtException)
                    .isNull()
            verify(exactly = 1) {
                streamer1.stream(listOf(StreamLocation(player, location)))
                streamer2.stream(listOf(StreamLocation(player, location)))
            }
        }

        @Test
        fun givenGettingLocationsThrowsExceptionItShouldCatchIt() {
            val location = locationOf(1f, 2f, 3f, 4, 5)
            val player = mockk<Player> {
                every { isHuman } returns true
                every { this@mockk.state } returns PlayerState.ON_FOOT
                every { this@mockk.location } throws RuntimeException()
            }
            every { playerService.getAllPlayers() } returns listOf(player)

            val caughtException = catchThrowable { streamerExecutor.execute() }

            assertThat(caughtException)
                    .isNull()
            verify(exactly = 0) {
                streamer1.stream(listOf(StreamLocation(player, location)))
                streamer2.stream(listOf(StreamLocation(player, location)))
            }
        }

    }

    private class TestAsyncExecutor : AsyncExecutor {

        override fun execute(onSuccess: (() -> Unit)?, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> Unit) {
            throw UnsupportedOperationException()
        }

        override fun <T> compute(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> T) {
            throw UnsupportedOperationException()
        }

        override fun executeOnMainThread(action: () -> Unit) {
            throw UnsupportedOperationException()
        }

        override fun <T> computeOnMainThread(action: () -> T): CompletableFuture<T> = completedFuture(action())
    }

}