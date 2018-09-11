package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CheckpointFactoryTest {

    @Test
    fun shouldCreateCheckpoint() {
        val playerRegistry = mockk<PlayerRegistry>()
        val checkpointFactory = CheckpointFactory(playerRegistry)

        val checkpoint = checkpointFactory.create(
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                size = 4f
        )

        assertThat(checkpoint)
                .satisfies {
                    assertThat(it.coordinates)
                            .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
                    assertThat(it.size)
                            .isEqualTo(4f)
                }
    }

}