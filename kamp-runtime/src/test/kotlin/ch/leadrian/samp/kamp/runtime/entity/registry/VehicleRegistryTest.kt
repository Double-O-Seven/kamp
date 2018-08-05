package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class VehicleRegistryTest {

    @Test
    fun shouldRegisterAndGetVehicle() {
        val vehicleId = 50
        val vehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val vehicleRegistry = VehicleRegistry()

        vehicleRegistry.register(vehicle)

        val registeredVehicle = vehicleRegistry.getVehicle(vehicleId)
        assertThat(registeredVehicle)
                .isSameAs(vehicle)
    }

    @Test
    fun givenAnotherVehicleWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val vehicleId = 50
        val alreadyRegisteredVehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val newVehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val vehicleRegistry = VehicleRegistry()
        vehicleRegistry.register(alreadyRegisteredVehicle)

        val caughtThrowable = catchThrowable { vehicleRegistry.register(newVehicle) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredVehicle = vehicleRegistry.getVehicle(vehicleId)
        assertThat(registeredVehicle)
                .isSameAs(alreadyRegisteredVehicle)
    }

    @Test
    fun shouldUnregisterRegisteredVehicle() {
        val vehicleId = 50
        val vehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val vehicleRegistry = VehicleRegistry()
        vehicleRegistry.register(vehicle)

        vehicleRegistry.unregister(vehicle)

        val registeredVehicle = vehicleRegistry.getVehicle(vehicleId)
        assertThat(registeredVehicle)
                .isNull()
    }


    @Test
    fun givenVehicleIsNotRegisteredItShouldThrowAnException() {
        val vehicleId = VehicleId.valueOf(50)
        val vehicle = mockk<Vehicle> {
            every { id } returns vehicleId
        }
        val vehicleRegistry = VehicleRegistry()

        val caughtThrowable = catchThrowable { vehicleRegistry.unregister(vehicle) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherVehicleWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val vehicleId = 50
        val alreadyRegisteredVehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val newVehicle = mockk<Vehicle> {
            every { id } returns VehicleId.valueOf(vehicleId)
        }
        val vehicleRegistry = VehicleRegistry()
        vehicleRegistry.register(alreadyRegisteredVehicle)

        val caughtThrowable = catchThrowable { vehicleRegistry.unregister(newVehicle) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredVehicle = vehicleRegistry.getVehicle(vehicleId)
        assertThat(registeredVehicle)
                .isSameAs(alreadyRegisteredVehicle)
    }

    @Test
    fun shouldReturnAllRegisteredVehicles() {
        val vehicleId1 = VehicleId.valueOf(10)
        val vehicle1 = mockk<Vehicle> {
            every { id } returns vehicleId1
        }
        val vehicleId2 = VehicleId.valueOf(15)
        val vehicle2 = mockk<Vehicle> {
            every { id } returns vehicleId2
        }
        val vehicleId3 = VehicleId.valueOf(30)
        val vehicle3 = mockk<Vehicle> {
            every { id } returns vehicleId3
        }
        val vehicleRegistry = VehicleRegistry()
        vehicleRegistry.register(vehicle1)
        vehicleRegistry.register(vehicle2)
        vehicleRegistry.register(vehicle3)

        val allVehicles = vehicleRegistry.getAllVehicles()

        assertThat(allVehicles)
                .containsExactly(vehicle1, vehicle2, vehicle3)
    }

}