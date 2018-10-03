package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class OnVehicleResprayHandlerTest {

    private val onVehicleResprayHandler = OnVehicleResprayHandler()
    private val player = mockk<Player>()
    private val vehicle = mockk<Vehicle>()
    private val colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6])

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onVehicleResprayHandler.onVehicleRespray(player, vehicle, colors)

        Assertions.assertThat(result)
                .isEqualTo(OnVehicleResprayListener.Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnVehicleResprayListener> {
            every {
                onVehicleRespray(player, vehicle, colors)
            } returns OnVehicleResprayListener.Result.Sync
        }
        val listener2 = mockk<OnVehicleResprayListener> {
            every {
                onVehicleRespray(player, vehicle, colors)
            } returns OnVehicleResprayListener.Result.Sync
        }
        val listener3 = mockk<OnVehicleResprayListener> {
            every {
                onVehicleRespray(player, vehicle, colors)
            } returns OnVehicleResprayListener.Result.Sync
        }
        val onVehicleResprayHandler = OnVehicleResprayHandler()
        onVehicleResprayHandler.register(listener1)
        onVehicleResprayHandler.register(listener2)
        onVehicleResprayHandler.register(listener3)

        val result = onVehicleResprayHandler.onVehicleRespray(player, vehicle, colors)

        verify(exactly = 1) {
            listener1.onVehicleRespray(player, vehicle, colors)
            listener2.onVehicleRespray(player, vehicle, colors)
            listener3.onVehicleRespray(player, vehicle, colors)
        }
        Assertions.assertThat(result)
                .isEqualTo(OnVehicleResprayListener.Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnVehicleResprayListener> {
            every { onVehicleRespray(player, vehicle, colors) } returns OnVehicleResprayListener.Result.Sync
        }
        val listener2 = mockk<OnVehicleResprayListener> {
            every { onVehicleRespray(player, vehicle, colors) } returns OnVehicleResprayListener.Result.Desync
        }
        val listener3 = mockk<OnVehicleResprayListener>()
        val onVehicleResprayHandler = OnVehicleResprayHandler()
        onVehicleResprayHandler.register(listener1)
        onVehicleResprayHandler.register(listener2)
        onVehicleResprayHandler.register(listener3)

        val result = onVehicleResprayHandler.onVehicleRespray(player, vehicle, colors)

        verify(exactly = 1) {
            listener1.onVehicleRespray(player, vehicle, colors)
            listener2.onVehicleRespray(player, vehicle, colors)
        }
        verify { listener3 wasNot Called }
        Assertions.assertThat(result)
                .isEqualTo(OnVehicleResprayListener.Result.Desync)
    }

}