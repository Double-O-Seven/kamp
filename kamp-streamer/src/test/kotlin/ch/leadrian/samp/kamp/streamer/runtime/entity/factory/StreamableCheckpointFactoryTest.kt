package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamableCheckpointFactoryTest {

    private lateinit var streamableCheckpointFactory: StreamableCheckpointFactory
    private val checkpointStreamer = mockk<CheckpointStreamer>()

    @BeforeEach
    fun setUp() {
        streamableCheckpointFactory = StreamableCheckpointFactory(mockk(), mockk(), mockk(), mockk(), mockk())
    }

    @Test
    fun shouldCreateStreamableCheckpoint() {
        val streamableCheckpoint = streamableCheckpointFactory.create(
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                size = 4f,
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(12, 34),
                virtualWorldIds = mutableSetOf(56, 78, 90),
                checkpointStreamer = checkpointStreamer
        )

        assertAll(
                { assertThat(streamableCheckpoint.coordinates).isEqualTo(vector3DOf(1f, 2f, 3f)) },
                { assertThat(streamableCheckpoint.size).isEqualTo(4f) },
                { assertThat(streamableCheckpoint.priority).isEqualTo(69) },
                { assertThat(streamableCheckpoint.streamDistance).isEqualTo(187f) },
                { assertThat(streamableCheckpoint.interiorIds).containsExactlyInAnyOrder(12, 34) },
                { assertThat(streamableCheckpoint.virtualWorldIds).containsExactlyInAnyOrder(56, 78, 90) }
        )
    }

}