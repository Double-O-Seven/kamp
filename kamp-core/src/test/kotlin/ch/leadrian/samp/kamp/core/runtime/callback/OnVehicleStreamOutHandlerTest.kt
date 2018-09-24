package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleStreamOutHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleStreamOutListener>(relaxed = true)
        val listener2 = mockk<OnVehicleStreamOutListener>(relaxed = true)
        val listener3 = mockk<OnVehicleStreamOutListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val forPlayer = mockk<Player>()
        val onVehicleStreamOutHandler = OnVehicleStreamOutHandler()
        onVehicleStreamOutHandler.register(listener1)
        onVehicleStreamOutHandler.register(listener2)
        onVehicleStreamOutHandler.register(listener3)

        onVehicleStreamOutHandler.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer)
            listener2.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer)
            listener3.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer)
        }
    }

}