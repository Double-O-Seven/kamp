package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Quaternion
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class VehicleRotationQuaternionPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Quaternion> = mockk()

    private lateinit var vehicleRotationQuaternionProperty: VehicleRotationQuaternionProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleRotationQuaternionProperty = VehicleRotationQuaternionProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetRotationQuaternion() {
        every { nativeFunctionExecutor.getVehicleRotationQuat(vehicleId.value, any(), any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            arg<ReferenceFloat>(4).value = 4f
            true
        }

        val rotationQuaternion = vehicleRotationQuaternionProperty.getValue(vehicle, property)

        assertThat(rotationQuaternion)
                .isEqualTo(quaternionOf(x = 2f, y = 3f, z = 4f, w = 1f))
    }

}