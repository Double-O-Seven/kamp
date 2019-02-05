package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamableRaceCheckpointFactoryTest {

    private lateinit var streamableRaceCheckpointFactory: StreamableRaceCheckpointFactory
    private val raceCheckpointStreamer = mockk<RaceCheckpointStreamer>()
    private val raceCheckpointService = mockk<RaceCheckpointService>()

    @BeforeEach
    fun setUp() {
        val raceCheckpoint = mockk<RaceCheckpoint>(relaxed = true)
        every {
            raceCheckpointService.createRaceCheckpoint(any(), any(), any(), any())
        } returns raceCheckpoint
        streamableRaceCheckpointFactory = StreamableRaceCheckpointFactory(
                raceCheckpointService,
                mockk(),
                mockk(),
                mockk(),
                mockk()
        )
    }

    @Test
    fun shouldCreateStreamableRaceCheckpoint() {
        val streamableRaceCheckpoint = streamableRaceCheckpointFactory.create(
                coordinates = vector3DOf(1f, 2f, 3f),
                size = 4f,
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(12, 34),
                virtualWorldIds = mutableSetOf(56, 78, 90),
                raceCheckpointStreamer = raceCheckpointStreamer
        )

        assertAll(
                {
                    raceCheckpointService.createRaceCheckpoint(
                            coordinates = vector3DOf(1f, 2f, 3f),
                            size = 4f,
                            type = RaceCheckpointType.AIR_FINISH,
                            nextCoordinates = vector3DOf(1001f, 2002f, 3003f)
                    )
                },
                { assertThat(streamableRaceCheckpoint.priority).isEqualTo(69) },
                { assertThat(streamableRaceCheckpoint.streamDistance).isEqualTo(187f) },
                { assertThat(streamableRaceCheckpoint.interiorIds).containsExactlyInAnyOrder(12, 34) },
                { assertThat(streamableRaceCheckpoint.virtualWorldIds).containsExactlyInAnyOrder(56, 78, 90) }
        )
    }

}