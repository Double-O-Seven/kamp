package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleStreamInHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleStreamInListener>(relaxed = true)
        val listener2 = mockk<OnVehicleStreamInListener>(relaxed = true)
        val listener3 = mockk<OnVehicleStreamInListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val forPlayer = mockk<Player>()
        val onVehicleStreamInHandler = OnVehicleStreamInHandler()
        onVehicleStreamInHandler.register(listener1)
        onVehicleStreamInHandler.register(listener2)
        onVehicleStreamInHandler.register(listener3)

        onVehicleStreamInHandler.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer)
            listener2.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer)
            listener3.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer)
        }
    }

}