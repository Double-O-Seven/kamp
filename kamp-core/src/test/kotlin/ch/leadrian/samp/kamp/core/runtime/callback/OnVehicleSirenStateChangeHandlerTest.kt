package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSirenStateChangeListener
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleSirenStateChangeHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleSirenStateChangeListener>(relaxed = true)
        val listener2 = mockk<OnVehicleSirenStateChangeListener>(relaxed = true)
        val listener3 = mockk<OnVehicleSirenStateChangeListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val player = mockk<Player>()
        val onVehicleSirenStateChangeHandler = OnVehicleSirenStateChangeHandler()
        onVehicleSirenStateChangeHandler.register(listener1)
        onVehicleSirenStateChangeHandler.register(listener2)
        onVehicleSirenStateChangeHandler.register(listener3)

        onVehicleSirenStateChangeHandler.onVehicleSirenStateChange(player, vehicle, VehicleSirenState.ON)

        verify(exactly = 1) {
            listener1.onVehicleSirenStateChange(player, vehicle, VehicleSirenState.ON)
            listener2.onVehicleSirenStateChange(player, vehicle, VehicleSirenState.ON)
            listener3.onVehicleSirenStateChange(player, vehicle, VehicleSirenState.ON)
        }
    }

}