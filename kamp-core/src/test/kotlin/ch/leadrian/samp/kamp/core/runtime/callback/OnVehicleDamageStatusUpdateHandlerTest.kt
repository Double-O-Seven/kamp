package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnVehicleDamageStatusUpdateHandlerTest {

    private val onVehicleDamageStatusUpdateHandler = OnVehicleDamageStatusUpdateHandler()
    private val vehicle = mockk<Vehicle>()
    private val player = mockk<Player>()

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicle, player)

        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnVehicleDamageStatusUpdateListener> {
            every {
                onVehicleDamageStatusUpdate(vehicle, player)
            } returns Result.Sync
        }
        val listener2 = mockk<OnVehicleDamageStatusUpdateListener> {
            every {
                onVehicleDamageStatusUpdate(vehicle, player)
            } returns Result.Sync
        }
        val listener3 = mockk<OnVehicleDamageStatusUpdateListener> {
            every {
                onVehicleDamageStatusUpdate(vehicle, player)
            } returns Result.Sync
        }
        val onVehicleDamageStatusUpdateHandler = OnVehicleDamageStatusUpdateHandler()
        onVehicleDamageStatusUpdateHandler.register(listener1)
        onVehicleDamageStatusUpdateHandler.register(listener2)
        onVehicleDamageStatusUpdateHandler.register(listener3)

        val result = onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicle, player)

        verify(exactly = 1) {
            listener1.onVehicleDamageStatusUpdate(vehicle, player)
            listener2.onVehicleDamageStatusUpdate(vehicle, player)
            listener3.onVehicleDamageStatusUpdate(vehicle, player)
        }
        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnVehicleDamageStatusUpdateListener> {
            every {
                onVehicleDamageStatusUpdate(vehicle, player)
            } returns Result.Sync
        }
        val listener2 = mockk<OnVehicleDamageStatusUpdateListener> {
            every {
                onVehicleDamageStatusUpdate(vehicle, player)
            } returns Result.Desync
        }
        val listener3 = mockk<OnVehicleDamageStatusUpdateListener>()
        val onVehicleDamageStatusUpdateHandler = OnVehicleDamageStatusUpdateHandler()
        onVehicleDamageStatusUpdateHandler.register(listener1)
        onVehicleDamageStatusUpdateHandler.register(listener2)
        onVehicleDamageStatusUpdateHandler.register(listener3)

        val result = onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicle, player)

        verify(exactly = 1) {
            listener1.onVehicleDamageStatusUpdate(vehicle, player)
            listener2.onVehicleDamageStatusUpdate(vehicle, player)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Desync)
    }

}