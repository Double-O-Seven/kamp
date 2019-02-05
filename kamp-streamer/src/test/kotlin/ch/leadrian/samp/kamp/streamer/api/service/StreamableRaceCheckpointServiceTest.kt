package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRaceCheckpointImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StreamableRaceCheckpointServiceTest {

    private lateinit var streamableRaceCheckpointService: StreamableRaceCheckpointService
    private val raceCheckpointStreamer = mockk<RaceCheckpointStreamer>()

    @BeforeEach
    fun setUp() {
        streamableRaceCheckpointService = StreamableRaceCheckpointService(raceCheckpointStreamer)
    }

    @Test
    fun shouldCreateStreamableRaceCheckpoint() {
        val expectedStreamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl>()
        every {
            raceCheckpointStreamer.createRaceCheckpoint(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    size = 4f,
                    type = RaceCheckpointType.AIR_FINISH,
                    nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableRaceCheckpoint

        val streamableRaceCheckpoint = streamableRaceCheckpointService.createStreamableRaceCheckpoint(
                coordinates = vector3DOf(150f, 100f, 20f),
                size = 4f,
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(123),
                virtualWorldIds = mutableSetOf(456)
        )

        assertThat(streamableRaceCheckpoint)
                .isEqualTo(expectedStreamableRaceCheckpoint)
    }

    @Test
    fun shouldCreateStreamableRaceCheckpointWithSingleInteriorIdAndVirtualWorldId() {
        val expectedStreamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl>()
        every {
            raceCheckpointStreamer.createRaceCheckpoint(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    size = 4f,
                    type = RaceCheckpointType.AIR_FINISH,
                    nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableRaceCheckpoint

        val streamableRaceCheckpoint = streamableRaceCheckpointService.createStreamableRaceCheckpoint(
                coordinates = vector3DOf(150f, 100f, 20f),
                size = 4f,
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                priority = 69,
                streamDistance = 187f,
                interiorId = 123,
                virtualWorldId = 456
        )

        assertThat(streamableRaceCheckpoint)
                .isEqualTo(expectedStreamableRaceCheckpoint)
    }

    @Test
    fun shouldCreateStreamableRaceCheckpointWithSphere() {
        val expectedStreamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl>()
        every {
            raceCheckpointStreamer.createRaceCheckpoint(
                    coordinates = match { it.x == 150f && it.y == 100f && it.z == 20f },
                    size = 4f,
                    type = RaceCheckpointType.AIR_FINISH,
                    nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableRaceCheckpoint

        val streamableRaceCheckpoint = streamableRaceCheckpointService.createStreamableRaceCheckpoint(
                sphere = sphereOf(
                        x = 150f,
                        y = 100f,
                        z = 20f,
                        radius = 4f
                ),
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(123),
                virtualWorldIds = mutableSetOf(456)
        )

        assertThat(streamableRaceCheckpoint)
                .isEqualTo(expectedStreamableRaceCheckpoint)
    }

    @Test
    fun shouldCreateStreamableRaceCheckpointWithSphereAndSingleInteriorIdAndVirtualWorldId() {
        val expectedStreamableRaceCheckpoint = mockk<StreamableRaceCheckpointImpl>()
        every {
            raceCheckpointStreamer.createRaceCheckpoint(
                    coordinates = match { it.x == 150f && it.y == 100f && it.z == 20f },
                    size = 4f,
                    type = RaceCheckpointType.AIR_FINISH,
                    nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableRaceCheckpoint

        val streamableRaceCheckpoint = streamableRaceCheckpointService.createStreamableRaceCheckpoint(
                sphere = sphereOf(
                        x = 150f,
                        y = 100f,
                        z = 20f,
                        radius = 4f
                ),
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                priority = 69,
                streamDistance = 187f,
                interiorId = 123,
                virtualWorldId = 456
        )

        assertThat(streamableRaceCheckpoint)
                .isEqualTo(expectedStreamableRaceCheckpoint)
    }

}