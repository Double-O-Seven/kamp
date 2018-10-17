package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.streamer.entity.StreamLocation
import com.netflix.governator.annotations.Configuration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StreamerExecutor
@Inject
constructor(
        private val streamers: Set<@JvmSuppressWildcards Streamer>,
        private val executorServiceFactory: ExecutorServiceFactory,
        private val asyncExecutor: AsyncExecutor,
        private val playerService: PlayerService
) {

    private companion object {

        val log = loggerFor<StreamerExecutor>()

        private val STREAMABLE_PLAYER_STATES: Set<PlayerState> = EnumSet.of(
                PlayerState.DRIVER,
                PlayerState.ON_FOOT,
                PlayerState.ENTER_VEHICLE_DRIVER,
                PlayerState.ENTER_VEHICLE_PASSENGER,
                PlayerState.EXIT_VEHICLE
        )
    }

    @Configuration("kamp.streamer.rate.ms")
    private var streamRateInMs: Long = 1000

    private lateinit var scheduledExecutorService: ScheduledExecutorService

    @PostConstruct
    fun initialize() {
        scheduledExecutorService = executorServiceFactory.createSingleThreadScheduledExecutor()
        scheduledExecutorService.scheduleAtFixedRate(this::execute, streamRateInMs, streamRateInMs, TimeUnit.MILLISECONDS)
    }

    @PreDestroy
    fun shutdown() {
        scheduledExecutorService.apply {
            log.info("Shutting down...")
            shutdown()
            log.info("Awaiting termination...")
            val terminated = awaitTermination(3, TimeUnit.MINUTES)
            if (terminated) {
                log.info("Terminated")
            } else {
                log.warn("Failed to terminate after 3 minutes")
            }
        }
    }

    private fun execute() {
        try {
            val streamLocations = getStreamLocations()
            stream(streamLocations)
        } catch (e: Exception) {
            log.error("Exception while streaming", e)
        }
    }

    private fun stream(streamLocations: List<StreamLocation>) {
        streamers.forEach { streamer ->
            try {
                var t = System.currentTimeMillis()
                streamer.stream(streamLocations)
                t = System.currentTimeMillis() - t
                log.info("Streaming with {} took {} ms", streamer::class.java, t)
            } catch (e: Exception) {
                log.error("Exception while streaming with {}", streamer, e)
            }
        }
    }

    private fun getStreamLocations(): List<StreamLocation> {
        val streamLocations = CompletableFuture<List<StreamLocation>>()
        asyncExecutor.executeOnMainThread { getStreamLocations(streamLocations) }
        return streamLocations.get()
    }

    private fun getStreamLocations(result: CompletableFuture<List<StreamLocation>>) {
        try {
            val streamLocations = playerService
                    .getAllPlayers()
                    .asSequence()
                    .filter { STREAMABLE_PLAYER_STATES.contains(it.state) }
                    .map { StreamLocation(it, it.location) }
                    .toList()
            result.complete(streamLocations)
        } catch (e: Exception) {
            result.completeExceptionally(e)
        }
    }

}