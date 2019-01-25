package ch.leadrian.samp.kamp.core.runtime.entity.delegate

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

internal class VehicleAngleDelegateTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleAngleDelegate: VehicleAngleDelegate

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleAngleDelegate = VehicleAngleDelegate(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetAngle() {
        every { nativeFunctionExecutor.getVehicleZAngle(vehicleId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val angle = vehicleAngleDelegate.getValue(vehicle, property)

        assertThat(angle)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetAngle() {
        every { nativeFunctionExecutor.setVehicleZAngle(any(), any()) } returns true

        vehicleAngleDelegate.setValue(vehicle, property, 4f)

        verify { nativeFunctionExecutor.setVehicleZAngle(vehicleid = vehicleId.value, z_angle = 4f) }
    }

}