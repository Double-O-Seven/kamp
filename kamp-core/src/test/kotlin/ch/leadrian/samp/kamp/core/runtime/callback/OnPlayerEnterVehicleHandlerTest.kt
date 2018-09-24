package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class OnPlayerEnterVehicleHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldCallAllListeners(isPassenger: Boolean) {
        val listener1 = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle>()
        val onPlayerEnterVehicleHandler = OnPlayerEnterVehicleHandler()
        onPlayerEnterVehicleHandler.register(listener1)
        onPlayerEnterVehicleHandler.register(listener2)
        onPlayerEnterVehicleHandler.register(listener3)

        onPlayerEnterVehicleHandler.onPlayerEnterVehicle(player, vehicle, isPassenger)

        verify(exactly = 1) {
            listener1.onPlayerEnterVehicle(player, vehicle, isPassenger)
            listener2.onPlayerEnterVehicle(player, vehicle, isPassenger)
            listener3.onPlayerEnterVehicle(player, vehicle, isPassenger)
        }
    }

}