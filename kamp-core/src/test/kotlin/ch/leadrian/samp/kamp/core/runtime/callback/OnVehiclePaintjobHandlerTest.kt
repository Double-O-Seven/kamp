package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehiclePaintjobHandlerTest {

    private val vehicle = mockk<Vehicle>()
    private val player = mockk<Player>()

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehiclePaintjobListener>(relaxed = true)
        val listener2 = mockk<OnVehiclePaintjobListener>(relaxed = true)
        val listener3 = mockk<OnVehiclePaintjobListener>(relaxed = true)
        val onVehiclePaintjobHandler = OnVehiclePaintjobHandler()
        onVehiclePaintjobHandler.register(listener1)
        onVehiclePaintjobHandler.register(listener2)
        onVehiclePaintjobHandler.register(listener3)

        onVehiclePaintjobHandler.onVehiclePaintjob(player, vehicle, 1)

        verify(exactly = 1) {
            listener1.onVehiclePaintjob(player, vehicle, 1)
            listener2.onVehiclePaintjob(player, vehicle, 1)
            listener3.onVehiclePaintjob(player, vehicle, 1)
        }
    }

}