package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnVehicleSpawnHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnVehicleSpawnListener>(relaxed = true)
        val listener2 = mockk<OnVehicleSpawnListener>(relaxed = true)
        val listener3 = mockk<OnVehicleSpawnListener>(relaxed = true)
        val vehicle = mockk<Vehicle>()
        val onVehicleSpawnHandler = OnVehicleSpawnHandler()
        onVehicleSpawnHandler.register(listener1)
        onVehicleSpawnHandler.register(listener2)
        onVehicleSpawnHandler.register(listener3)

        onVehicleSpawnHandler.onVehicleSpawn(vehicle)

        verify(exactly = 1) {
            listener1.onVehicleSpawn(vehicle)
            listener2.onVehicleSpawn(vehicle)
            listener3.onVehicleSpawn(vehicle)
        }
    }

}