package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
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

internal class VehicleVelocityPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleVelocityProperty: VehicleVelocityProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleVelocityProperty = VehicleVelocityProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetVelocity() {
        every { nativeFunctionExecutor.getVehicleVelocity(vehicleId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val velocity = vehicleVelocityProperty.getValue(vehicle, property)

        assertThat(velocity)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetVelocity() {
        every { nativeFunctionExecutor.setVehicleVelocity(any(), any(), any(), any()) } returns true

        vehicleVelocityProperty.setValue(vehicle, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setVehicleVelocity(vehicleid = vehicleId.value, X = 1f, Y = 2f, Z = 3f) }
    }
}