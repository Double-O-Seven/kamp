package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleDeathHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleDeathListener>(relaxed = true)
        val listener2 = mockk<OnVehicleDeathListener>(relaxed = true)
        val listener3 = mockk<OnVehicleDeathListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val player = mockk<Player>()
        val onVehicleDeathHandler = OnVehicleDeathHandler()
        onVehicleDeathHandler.register(listener1)
        onVehicleDeathHandler.register(listener2)
        onVehicleDeathHandler.register(listener3)

        onVehicleDeathHandler.onVehicleDeath(vehicle, player)

        verify(exactly = 1) {
            listener1.onVehicleDeath(vehicle, player)
            listener2.onVehicleDeath(vehicle, player)
            listener3.onVehicleDeath(vehicle, player)
        }
    }

}