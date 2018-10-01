package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.core.runtime.entity.factory.RaceCheckpointFactory
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RaceCheckpointServiceTest {

    private lateinit var raceCheckpointService: RaceCheckpointService

    private val raceCheckpointFactory = mockk<RaceCheckpointFactory>()

    @BeforeEach
    fun setUp() {
        raceCheckpointService = RaceCheckpointService(raceCheckpointFactory)
    }

    @Test
    fun shouldCreateRaceCheckpointWithCoordinatesAndAngle() {
        val raceCheckpoint = mockk<RaceCheckpoint>()
        every {
            raceCheckpointFactory.create(
                    coordinates = vector3DOf(1f, 2f, 3f),
                    size = 4f,
                    type = RaceCheckpointType.AIR_NORMAL,
                    nextCoordinates = vector3DOf(5f, 6f, 7f)
            )
        } returns raceCheckpoint

        val createdRaceCheckpoint = raceCheckpointService.createRaceCheckpoint(
                coordinates = vector3DOf(1f, 2f, 3f),
                size = 4f,
                type = RaceCheckpointType.AIR_NORMAL,
                nextCoordinates = vector3DOf(5f, 6f, 7f)
        )

        assertThat(createdRaceCheckpoint)
                .isEqualTo(raceCheckpoint)
    }

    @Test
    fun shouldCreateRaceCheckpointWithSphere() {
        val raceCheckpoint = mockk<RaceCheckpoint>()
        every {
            raceCheckpointFactory.create(
                    coordinates = vector3DOf(1f, 2f, 3f),
                    size = 4f,
                    type = RaceCheckpointType.AIR_NORMAL,
                    nextCoordinates = vector3DOf(5f, 6f, 7f)
            )
        } returns raceCheckpoint

        val createdRaceCheckpoint = raceCheckpointService.createRaceCheckpoint(
                sphere = sphereOf(1f, 2f, 3f, 4f),
                type = RaceCheckpointType.AIR_NORMAL,
                nextCoordinates = vector3DOf(5f, 6f, 7f)
        )

        assertThat(createdRaceCheckpoint)
                .isEqualTo(raceCheckpoint)
    }

}