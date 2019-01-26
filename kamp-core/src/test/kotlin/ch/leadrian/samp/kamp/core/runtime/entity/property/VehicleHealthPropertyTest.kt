package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class VehicleHealthPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleHealthProperty: VehicleHealthProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleHealthProperty = VehicleHealthProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetHealth() {
        every { nativeFunctionExecutor.getVehicleHealth(vehicleId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val health = vehicleHealthProperty.getValue(vehicle, property)

        assertThat(health)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetHealth() {
        every { nativeFunctionExecutor.setVehicleHealth(any(), any()) } returns true

        vehicleHealthProperty.setValue(vehicle, property, 4f)

        verify { nativeFunctionExecutor.setVehicleHealth(vehicleid = vehicleId.value, health = 4f) }
    }

}