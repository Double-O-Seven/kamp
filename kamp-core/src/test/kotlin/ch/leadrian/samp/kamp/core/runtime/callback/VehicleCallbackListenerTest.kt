package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleCallbackListenerTest {

    private lateinit var vehicleCallbackListener: VehicleCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        vehicleCallbackListener = VehicleCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        vehicleCallbackListener.initialize()

        verify { callbackListenerManager.register(vehicleCallbackListener) }
    }

    @Test
    fun shouldExecuteOnSpawn() {
        val vehicle = mockk<Vehicle> {
            every { onSpawn() } just Runs
        }

        vehicleCallbackListener.onVehicleSpawn(vehicle)

        verify { vehicle.onSpawn() }
    }

    @Test
    fun shouldExecuteOnDeath() {
        val killer = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onDeath(killer) } just Runs
        }

        vehicleCallbackListener.onVehicleDeath(vehicle, killer)

        verify { vehicle.onDeath(killer) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldExecuteOnEnter(isPassenger: Boolean) {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onEnter(any(), any()) } just Runs
        }

        vehicleCallbackListener.onPlayerEnterVehicle(player, vehicle, isPassenger)

        verify { vehicle.onEnter(player, isPassenger) }
    }

    @Test
    fun shouldExecuteOnExit() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onExit(any<Player>()) } just Runs
        }

        vehicleCallbackListener.onPlayerExitVehicle(player, vehicle)

        verify { vehicle.onExit(player) }
    }

}