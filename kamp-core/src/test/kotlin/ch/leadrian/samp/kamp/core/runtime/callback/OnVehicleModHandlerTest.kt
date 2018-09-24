package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener.Result
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnVehicleModHandlerTest {

    private val onVehicleModHandler = OnVehicleModHandler()
    private val player = mockk<Player>()
    private val vehicle = mockk<Vehicle>()
    private val component = VehicleComponentModel.BULLBARS_CHROME_GRILL

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onVehicleModHandler.onVehicleMod(player, vehicle, component)

        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnVehicleModListener> {
            every {
                onVehicleMod(player, vehicle, component)
            } returns Result.Sync
        }
        val listener2 = mockk<OnVehicleModListener> {
            every {
                onVehicleMod(player, vehicle, component)
            } returns Result.Sync
        }
        val listener3 = mockk<OnVehicleModListener> {
            every {
                onVehicleMod(player, vehicle, component)
            } returns Result.Sync
        }
        val onVehicleModHandler = OnVehicleModHandler()
        onVehicleModHandler.register(listener1)
        onVehicleModHandler.register(listener2)
        onVehicleModHandler.register(listener3)

        val result = onVehicleModHandler.onVehicleMod(player, vehicle, component)

        verify(exactly = 1) {
            listener1.onVehicleMod(player, vehicle, component)
            listener2.onVehicleMod(player, vehicle, component)
            listener3.onVehicleMod(player, vehicle, component)
        }
        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnVehicleModListener> {
            every { onVehicleMod(player, vehicle, component) } returns Result.Sync
        }
        val listener2 = mockk<OnVehicleModListener> {
            every { onVehicleMod(player, vehicle, component) } returns Result.Desync
        }
        val listener3 = mockk<OnVehicleModListener>()
        val onVehicleModHandler = OnVehicleModHandler()
        onVehicleModHandler.register(listener1)
        onVehicleModHandler.register(listener2)
        onVehicleModHandler.register(listener3)

        val result = onVehicleModHandler.onVehicleMod(player, vehicle, component)

        verify(exactly = 1) {
            listener1.onVehicleMod(player, vehicle, component)
            listener2.onVehicleMod(player, vehicle, component)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Desync)
    }

}