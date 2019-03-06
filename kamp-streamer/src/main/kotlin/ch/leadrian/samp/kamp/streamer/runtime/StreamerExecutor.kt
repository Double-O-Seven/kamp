package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import com.netflix.governator.annotations.Configuration
import java.util.EnumSet
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
                PlayerState.PASSENGER,
                PlayerState.ON_FOOT,
                PlayerState.SPAWNED,
                PlayerState.ENTER_VEHICLE_DRIVER,
                PlayerState.ENTER_VEHICLE_PASSENGER,
                PlayerState.EXIT_VEHICLE,
                PlayerState.SPECTATING
        )
    }

    @Configuration("kamp.streamer.rate.ms")
    private var streamRateInMs: Long = 1000

    private lateinit var scheduledExecutorService: ScheduledExecutorService

    @PostConstruct
    fun initialize() {
        scheduledExecutorService = executorServiceFactory.createSingleThreadScheduledExecutor()
        scheduledExecutorService.scheduleAtFixedRate(
                this::execute,
                streamRateInMs,
                streamRateInMs,
                TimeUnit.MILLISECONDS
        )
        streamers.forEach { log.info("Loaded streamer {}", it::class.qualifiedName) }
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

    internal fun execute() {
        try {
            val streamLocations = getStreamLocations()
            stream(streamLocations)
        } catch (e: Exception) {
            log.error("Exception while streaming", e)
        }
    }

    private fun getStreamLocations(): List<StreamLocation> {
        return asyncExecutor.computeOnMainThread {
            playerService
                    .getAllPlayers()
                    .asSequence()
                    .filter { it.isHuman }
                    .filter { STREAMABLE_PLAYER_STATES.contains(it.state) }
                    .map { StreamLocation(it, it.location) }
                    .toList()
        }.get()
    }

    private fun stream(streamLocations: List<StreamLocation>) {
        streamers.forEach { streamer ->
            try {
                var t = System.currentTimeMillis()
                streamer.stream(streamLocations)
                t = System.currentTimeMillis() - t
                log.trace("Streaming with {} took {} ms", streamer::class.java, t)
            } catch (e: Exception) {
                log.error("Exception while streaming with {}", streamer, e)
            }
        }
    }

}