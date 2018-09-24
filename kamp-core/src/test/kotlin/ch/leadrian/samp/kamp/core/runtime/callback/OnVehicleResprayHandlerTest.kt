package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleResprayHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleResprayListener>(relaxed = true)
        val listener2 = mockk<OnVehicleResprayListener>(relaxed = true)
        val listener3 = mockk<OnVehicleResprayListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val player = mockk<Player>()
        val colors = vehicleColorsOf(VehicleColor[1], VehicleColor[2])
        val onVehicleResprayHandler = OnVehicleResprayHandler()
        onVehicleResprayHandler.register(listener1)
        onVehicleResprayHandler.register(listener2)
        onVehicleResprayHandler.register(listener3)

        onVehicleResprayHandler.onVehicleRespray(player, vehicle, colors)

        verify(exactly = 1) {
            listener1.onVehicleRespray(player, vehicle, colors)
            listener2.onVehicleRespray(player, vehicle, colors)
            listener3.onVehicleRespray(player, vehicle, colors)
        }
    }

}