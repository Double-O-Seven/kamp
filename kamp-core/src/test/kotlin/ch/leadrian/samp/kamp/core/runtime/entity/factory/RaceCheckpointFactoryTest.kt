package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RaceCheckpointFactoryTest {

    @Test
    fun shouldCreateRaceCheckpoint() {
        val playerRegistry = mockk<PlayerRegistry>()
        val raceCheckpointFactory = RaceCheckpointFactory(playerRegistry)

        val raceCheckpoint = raceCheckpointFactory.create(
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                size = 4f,
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(x = 5f, y = 6f, z = 7f)
        )

        assertThat(raceCheckpoint)
                .satisfies {
                    assertThat(it.coordinates)
                            .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
                    assertThat(it.size)
                            .isEqualTo(4f)
                    assertThat(it.type)
                            .isEqualTo(RaceCheckpointType.AIR_FINISH)
                    assertThat(it.nextCoordinates)
                            .isEqualTo(vector3DOf(x = 5f, y = 6f, z = 7f))
                }
    }

}