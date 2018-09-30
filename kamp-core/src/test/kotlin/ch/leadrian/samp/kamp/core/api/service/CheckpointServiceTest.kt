package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.runtime.entity.factory.CheckpointFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CheckpointServiceTest {

    private lateinit var checkpointService: CheckpointService

    private val checkpoint = mockk<Checkpoint>()
    private val checkpointFactory = mockk<CheckpointFactory>()

    @BeforeEach
    fun setUp() {
        every { checkpointFactory.create(any(), any()) } returns checkpoint
        checkpointService = CheckpointService(checkpointFactory)
    }

    @Test
    fun shouldCreateCheckpointWithCoordinatesAndSize() {
        val createdCheckpoint = checkpointService.createCheckpoint(vector3DOf(x = 1f, y = 2f, z = 3f), 4f)

        verify { checkpointFactory.create(vector3DOf(x = 1f, y = 2f, z = 3f), 4f) }
        assertThat(createdCheckpoint)
                .isEqualTo(checkpoint)
    }

    @Test
    fun shouldCreateCheckpointWithSphere() {
        val createdCheckpoint = checkpointService.createCheckpoint(sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f))

        verify { checkpointFactory.create(vector3DOf(x = 1f, y = 2f, z = 3f), 4f) }
        assertThat(createdCheckpoint)
                .isEqualTo(checkpoint)
    }

}