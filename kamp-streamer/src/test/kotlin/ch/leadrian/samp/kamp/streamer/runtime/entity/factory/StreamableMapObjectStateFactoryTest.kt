package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StreamableMapObjectStateFactoryTest {

    private lateinit var streamableMapObjectStateFactory: StreamableMapObjectStateFactory
    private val timeProvider = mockk<TimeProvider>()
    private val timerExecutor = mockk<TimerExecutor>()
    private val asyncExecutor = mockk<AsyncExecutor>()

    @BeforeEach
    fun setUp() {
        streamableMapObjectStateFactory = StreamableMapObjectStateFactory(timeProvider, timerExecutor, asyncExecutor)
    }

    @Test
    fun shouldCreateFixedCoordinates() {
        val state = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = vector3DOf(1f, 2f, 3f),
                rotation = vector3DOf(4f, 5f, 6f)
        )

        assertThat(state)
                .satisfies {
                    assertThat(it.coordinates)
                            .isEqualTo(vector3DOf(1f, 2f, 3f))
                    assertThat(it.rotation)
                            .isEqualTo(vector3DOf(4f, 5f, 6f))
                }
    }

    @Test
    fun shouldCreateMoving() {
        every { timeProvider.getCurrentTimeInMs() } returns 0
        every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
        val state = streamableMapObjectStateFactory.createMoving(
                origin = vector3DOf(1f, 2f, 3f),
                destination = vector3DOf(4f, 5f, 6f),
                speed = 7f,
                startRotation = vector3DOf(8f, 9f, 10f),
                targetRotation = vector3DOf(11f, 12f, 13f),
                onMoved = {}
        )

        assertThat(state)
                .satisfies {
                    assertThat(it.coordinates)
                            .isEqualTo(vector3DOf(1f, 2f, 3f))
                    assertThat(it.rotation)
                            .isEqualTo(vector3DOf(8f, 9f, 10f))
                }
    }

    @Test
    fun shouldReturnAttachedToPlayer() {
        val player = mockk<Player> {
            every { coordinates } returns vector3DOf(0f, 0f, 0f)
            every { angle } returns 0f
        }

        val state = streamableMapObjectStateFactory.createAttachedToPlayer(
                player = player,
                offset = vector3DOf(1f, 2f, 3f),
                attachRotation = vector3DOf(4f, 5f, 6f)
        )

        assertThat(state.player)
                .isEqualTo(player)
    }

    @Test
    fun shouldReturnAttachedToVehicle() {
        val vehicle = mockk<Vehicle> {
            every { coordinates } returns vector3DOf(0f, 0f, 0f)
            every { angle } returns 0f
        }

        val state = streamableMapObjectStateFactory.createAttachedToVehicle(
                vehicle = vehicle,
                offset = vector3DOf(1f, 2f, 3f),
                attachRotation = vector3DOf(4f, 5f, 6f)
        )

        assertThat(state.vehicle)
                .isEqualTo(vehicle)
    }

}