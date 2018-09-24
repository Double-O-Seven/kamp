package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class OnPlayerExitVehicleHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldCallAllListeners(isPassenger: Boolean) {
        val listener1 = mockk<OnPlayerExitVehicleListener>(relaxed = true)
        val listener2 = mockk<OnPlayerExitVehicleListener>(relaxed = true)
        val listener3 = mockk<OnPlayerExitVehicleListener>(relaxed = true)
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle>()
        val onPlayerExitVehicleHandler = OnPlayerExitVehicleHandler()
        onPlayerExitVehicleHandler.register(listener1)
        onPlayerExitVehicleHandler.register(listener2)
        onPlayerExitVehicleHandler.register(listener3)

        onPlayerExitVehicleHandler.onPlayerExitVehicle(player, vehicle)

        verify(exactly = 1) {
            listener1.onPlayerExitVehicle(player, vehicle)
            listener2.onPlayerExitVehicle(player, vehicle)
            listener3.onPlayerExitVehicle(player, vehicle)
        }
    }

}