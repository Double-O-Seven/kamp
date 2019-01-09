package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StreamableTextLabelStateFactoryTest {

    private lateinit var streamableTextLabelStateFactory: StreamableTextLabelStateFactory
    private val streamableTextLabel = mockk<StreamableTextLabelImpl>()
    private val playerTextLabelService = mockk<PlayerTextLabelService>()
    private val asyncExecutor = mockk<AsyncExecutor>()

    @BeforeEach
    fun setUp() {
        streamableTextLabelStateFactory = StreamableTextLabelStateFactory(playerTextLabelService, asyncExecutor)
    }

    @Test
    fun shouldCreateFixedCoordinates() {
        val fixedCoordinates = streamableTextLabelStateFactory.createFixedCoordinates(streamableTextLabel, vector3DOf(1f, 2f, 3f))

        assertThat(fixedCoordinates.coordinates)
                .isEqualTo(vector3DOf(1f, 2f, 3f))
    }

    @Test
    fun shouldCreateAttachedToVehicle() {
        val vehicle = mockk<Vehicle>()
        val attachedToVehicle = streamableTextLabelStateFactory.createAttachedToVehicle(
                streamableTextLabel,
                vector3DOf(1f, 2f, 3f),
                vehicle
        )

        assertThat(attachedToVehicle)
                .satisfies {
                    assertThat(it.offset)
                            .isEqualTo(vector3DOf(1f, 2f, 3f))
                    assertThat(it.vehicle)
                            .isEqualTo(vehicle)
                }
    }

    @Test
    fun shouldCreateAttachedToPlayer() {
        val player = mockk<Player>()
        val attachedToPlayer = streamableTextLabelStateFactory.createAttachedToPlayer(
                streamableTextLabel,
                vector3DOf(1f, 2f, 3f),
                player
        )

        assertThat(attachedToPlayer)
                .satisfies {
                    assertThat(it.offset)
                            .isEqualTo(vector3DOf(1f, 2f, 3f))
                    assertThat(it.player)
                            .isEqualTo(player)
                }
    }

}