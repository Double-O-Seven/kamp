package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleLightsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehiclePanelDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleTiresDamageStatus
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class VehicleDamageStatusPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleDamageStatusProperty: VehicleDamageStatusProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleDamageStatusProperty = VehicleDamageStatusProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetDamageStatus() {
        every {
            nativeFunctionExecutor.getVehicleDamageStatus(vehicleId.value, any(), any(), any(), any())
        } answers {
            secondArg<ReferenceInt>().value = 10
            thirdArg<ReferenceInt>().value = 20
            arg<ReferenceInt>(3).value = 30
            arg<ReferenceInt>(4).value = 40
            true
        }

        val damageStatus = vehicleDamageStatusProperty.getValue(vehicle, property)

        Assertions.assertThat(damageStatus)
                .isEqualTo(
                        VehicleDamageStatus(
                                panels = VehiclePanelDamageStatus(10),
                                doors = VehicleDoorsDamageStatus(20),
                                lights = VehicleLightsDamageStatus(30),
                                tires = VehicleTiresDamageStatus(40)
                        )
                )
    }

    @Test
    fun shouldSetDamageStatus() {
        every {
            nativeFunctionExecutor.updateVehicleDamageStatus(any(), any(), any(), any(), any())
        } returns true

        vehicleDamageStatusProperty.setValue(
                vehicle,
                property,
                VehicleDamageStatus(
                        panels = VehiclePanelDamageStatus(10),
                        doors = VehicleDoorsDamageStatus(20),
                        lights = VehicleLightsDamageStatus(30),
                        tires = VehicleTiresDamageStatus(40)
                )
        )

        verify {
            nativeFunctionExecutor.updateVehicleDamageStatus(
                    vehicleid = vehicleId.value,
                    panels = 10,
                    doors = 20,
                    lights = 30,
                    tires = 40
            )
        }
    }

}