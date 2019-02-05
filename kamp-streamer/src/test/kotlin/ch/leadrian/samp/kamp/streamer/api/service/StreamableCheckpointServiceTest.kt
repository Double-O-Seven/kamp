package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCheckpointImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StreamableCheckpointServiceTest {

    private lateinit var streamableCheckpointService: StreamableCheckpointService
    private val checkpointStreamer = mockk<CheckpointStreamer>()

    @BeforeEach
    fun setUp() {
        streamableCheckpointService = StreamableCheckpointService(checkpointStreamer)
    }

    @Test
    fun shouldCreateStreamableCheckpoint() {
        val expectedStreamableCheckpoint = mockk<StreamableCheckpointImpl>()
        every {
            checkpointStreamer.createCheckpoint(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    size = 4f,
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableCheckpoint

        val streamableCheckpoint = streamableCheckpointService.createStreamableCheckpoint(
                coordinates = vector3DOf(150f, 100f, 20f),
                size = 4f,
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(123),
                virtualWorldIds = mutableSetOf(456)
        )

        assertThat(streamableCheckpoint)
                .isEqualTo(expectedStreamableCheckpoint)
    }

    @Test
    fun shouldCreateStreamableCheckpointWithSingleInteriorIdAndVirtualWorldId() {
        val expectedStreamableCheckpoint = mockk<StreamableCheckpointImpl>()
        every {
            checkpointStreamer.createCheckpoint(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    size = 4f,
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableCheckpoint

        val streamableCheckpoint = streamableCheckpointService.createStreamableCheckpoint(
                coordinates = vector3DOf(150f, 100f, 20f),
                size = 4f,
                priority = 69,
                streamDistance = 187f,
                interiorId = 123,
                virtualWorldId = 456
        )

        assertThat(streamableCheckpoint)
                .isEqualTo(expectedStreamableCheckpoint)
    }

    @Test
    fun shouldCreateStreamableCheckpointWithSphere() {
        val expectedStreamableCheckpoint = mockk<StreamableCheckpointImpl>()
        every {
            checkpointStreamer.createCheckpoint(
                    coordinates = match { it.x == 150f && it.y == 100f && it.z == 20f },
                    size = 4f,
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableCheckpoint

        val streamableCheckpoint = streamableCheckpointService.createStreamableCheckpoint(
                sphere = sphereOf(
                        x = 150f,
                        y = 100f,
                        z = 20f,
                        radius = 4f
                ),
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(123),
                virtualWorldIds = mutableSetOf(456)
        )

        assertThat(streamableCheckpoint)
                .isEqualTo(expectedStreamableCheckpoint)
    }

    @Test
    fun shouldCreateStreamableCheckpointWithSphereAndSingleInteriorIdAndVirtualWorldId() {
        val expectedStreamableCheckpoint = mockk<StreamableCheckpointImpl>()
        every {
            checkpointStreamer.createCheckpoint(
                    coordinates = match { it.x == 150f && it.y == 100f && it.z == 20f },
                    size = 4f,
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )
        } returns expectedStreamableCheckpoint

        val streamableCheckpoint = streamableCheckpointService.createStreamableCheckpoint(
                sphere = sphereOf(
                        x = 150f,
                        y = 100f,
                        z = 20f,
                        radius = 4f
                ),
                priority = 69,
                streamDistance = 187f,
                interiorId = 123,
                virtualWorldId = 456
        )

        assertThat(streamableCheckpoint)
                .isEqualTo(expectedStreamableCheckpoint)
    }

}