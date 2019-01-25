package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class VehicleLocationDelegateTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleLocationDelegate: VehicleLocationDelegate

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleLocationDelegate = VehicleLocationDelegate(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetLocation() {
        every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { vehicle.interiorId } returns 5
        every { vehicle.virtualWorldId } returns 6

        val location = vehicleLocationDelegate.getValue(vehicle, property)

        assertThat(location)
                .isEqualTo(locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6))
    }

    @Test
    fun shouldSetLocation() {
        vehicle.apply {
            every { coordinates = any() } just Runs
            every { interiorId = any() } just Runs
            every { virtualWorldId = any() } just Runs
        }

        vehicleLocationDelegate.setValue(
                vehicle,
                property,
                locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6)
        )

        verify {
            vehicle.coordinates = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6)
            vehicle.interiorId = 5
            vehicle.virtualWorldId = 6
        }
    }
}