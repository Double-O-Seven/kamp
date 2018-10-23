package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDestructionListener
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleDestructionHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleDestructionListener>(relaxed = true)
        val listener2 = mockk<OnVehicleDestructionListener>(relaxed = true)
        val listener3 = mockk<OnVehicleDestructionListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val onVehicleDestructionHandler = OnVehicleDestructionHandler()
        onVehicleDestructionHandler.register(listener1)
        onVehicleDestructionHandler.register(listener2)
        onVehicleDestructionHandler.register(listener3)

        onVehicleDestructionHandler.onVehicleDestruction(vehicle)

        verify(exactly = 1) {
            listener1.onVehicleDestruction(vehicle)
            listener2.onVehicleDestruction(vehicle)
            listener3.onVehicleDestruction(vehicle)
        }
    }

}