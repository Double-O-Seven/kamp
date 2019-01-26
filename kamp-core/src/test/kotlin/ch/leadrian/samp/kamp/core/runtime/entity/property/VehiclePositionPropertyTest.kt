package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
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

internal class VehiclePositionPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehiclePositionProperty: VehiclePositionProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehiclePositionProperty = VehiclePositionProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetPosition() {
        every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { vehicle.angle } returns 4f

        val position = vehiclePositionProperty.getValue(vehicle, property)

        assertThat(position)
                .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
    }

    @Test
    fun shouldSetPosition() {
        vehicle.apply {
            every { coordinates = any() } just Runs
            every { angle = any() } just Runs
        }

        vehiclePositionProperty.setValue(
                vehicle,
                property,
                positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
        )

        verify {
            vehicle.coordinates = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
            vehicle.angle = 4f
        }
    }
}