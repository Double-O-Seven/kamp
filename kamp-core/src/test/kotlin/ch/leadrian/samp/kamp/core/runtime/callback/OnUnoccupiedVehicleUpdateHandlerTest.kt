package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnUnoccupiedVehicleUpdateHandlerTest {

    private val onUnoccupiedVehicleUpdateHandler = OnUnoccupiedVehicleUpdateHandler()
    private val vehicle = mockk<Vehicle>()
    private val player = mockk<Player>()
    private val coordinates = vector3DOf(1f, 2f, 3f)
    private val velocity = vector3DOf(1f, 2f, 3f)

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                vehicle = vehicle,
                player = player,
                passengerSeat = 3,
                coordinates = coordinates,
                velocity = velocity
        )

        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnUnoccupiedVehicleUpdateListener> {
            every {
                onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 3,
                        coordinates = coordinates,
                        velocity = velocity
                )
            } returns Result.Sync
        }
        val listener2 = mockk<OnUnoccupiedVehicleUpdateListener> {
            every {
                onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 3,
                        coordinates = coordinates,
                        velocity = velocity
                )
            } returns Result.Sync
        }
        val listener3 = mockk<OnUnoccupiedVehicleUpdateListener> {
            every {
                onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 3,
                        coordinates = coordinates,
                        velocity = velocity
                )
            } returns Result.Sync
        }
        val onUnoccupiedVehicleUpdateHandler = OnUnoccupiedVehicleUpdateHandler()
        onUnoccupiedVehicleUpdateHandler.register(listener1)
        onUnoccupiedVehicleUpdateHandler.register(listener2)
        onUnoccupiedVehicleUpdateHandler.register(listener3)

        val result = onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                vehicle = vehicle,
                player = player,
                passengerSeat = 3,
                coordinates = coordinates,
                velocity = velocity
        )

        verify(exactly = 1) {
            listener1.onUnoccupiedVehicleUpdate(
                    vehicle = vehicle,
                    player = player,
                    passengerSeat = 3,
                    coordinates = coordinates,
                    velocity = velocity
            )
            listener2.onUnoccupiedVehicleUpdate(
                    vehicle = vehicle,
                    player = player,
                    passengerSeat = 3,
                    coordinates = coordinates,
                    velocity = velocity
            )
            listener3.onUnoccupiedVehicleUpdate(
                    vehicle = vehicle,
                    player = player,
                    passengerSeat = 3,
                    coordinates = coordinates,
                    velocity = velocity
            )
        }
        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnUnoccupiedVehicleUpdateListener> {
            every {
                onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 3,
                        coordinates = coordinates,
                        velocity = velocity
                )
            } returns Result.Sync
        }
        val listener2 = mockk<OnUnoccupiedVehicleUpdateListener> {
            every {
                onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 3,
                        coordinates = coordinates,
                        velocity = velocity
                )
            } returns Result.Desync
        }
        val listener3 = mockk<OnUnoccupiedVehicleUpdateListener>()
        val onUnoccupiedVehicleUpdateHandler = OnUnoccupiedVehicleUpdateHandler()
        onUnoccupiedVehicleUpdateHandler.register(listener1)
        onUnoccupiedVehicleUpdateHandler.register(listener2)
        onUnoccupiedVehicleUpdateHandler.register(listener3)

        val result = onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                vehicle = vehicle,
                player = player,
                passengerSeat = 3,
                coordinates = coordinates,
                velocity = velocity
        )

        verify(exactly = 1) {
            listener1.onUnoccupiedVehicleUpdate(
                    vehicle = vehicle,
                    player = player,
                    passengerSeat = 3,
                    coordinates = coordinates,
                    velocity = velocity
            )
            listener2.onUnoccupiedVehicleUpdate(
                    vehicle = vehicle,
                    player = player,
                    passengerSeat = 3,
                    coordinates = coordinates,
                    velocity = velocity
            )
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Desync)
    }

}