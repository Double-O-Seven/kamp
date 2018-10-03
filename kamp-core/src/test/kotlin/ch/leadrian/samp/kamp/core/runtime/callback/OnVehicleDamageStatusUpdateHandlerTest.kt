package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleDamageStatusUpdateHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleDamageStatusUpdateListener>(relaxed = true)
        val listener2 = mockk<OnVehicleDamageStatusUpdateListener>(relaxed = true)
        val listener3 = mockk<OnVehicleDamageStatusUpdateListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val player = mockk<Player>()
        val onVehicleDamageStatusUpdateHandler = OnVehicleDamageStatusUpdateHandler()
        onVehicleDamageStatusUpdateHandler.register(listener1)
        onVehicleDamageStatusUpdateHandler.register(listener2)
        onVehicleDamageStatusUpdateHandler.register(listener3)

        onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicle, player)

        verify(exactly = 1) {
            listener1.onVehicleDamageStatusUpdate(vehicle, player)
            listener2.onVehicleDamageStatusUpdate(vehicle, player)
            listener3.onVehicleDamageStatusUpdate(vehicle, player)
        }
    }

}