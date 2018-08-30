package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.CarModType
import ch.leadrian.samp.kamp.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VehicleComponentsImplTest {

    private lateinit var vehicleComponents: VehicleComponentsImpl

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val vehicleId = VehicleId.valueOf(69)
    private val vehicle = mockk<Vehicle> {
        every { id } returns vehicleId
    }

    @BeforeEach
    fun setUp() {
        vehicleComponents = VehicleComponentsImpl(
                vehicle = vehicle,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
    }

    @Test
    fun shouldAddVehicleComponent() {
        every { nativeFunctionExecutor.addVehicleComponent(any(), any()) } returns true

        vehicleComponents.add(VehicleComponentModel.BULLBARS_CHROME_GRILL)

        verify {
            nativeFunctionExecutor.addVehicleComponent(
                    vehicleid = vehicleId.value,
                    componentid = VehicleComponentModel.BULLBARS_CHROME_GRILL.value
            )
        }
    }

    @Test
    fun shouldRemoveVehicleComponent() {
        every { nativeFunctionExecutor.removeVehicleComponent(any(), any()) } returns true

        vehicleComponents.remove(VehicleComponentModel.BULLBARS_CHROME_GRILL)

        verify {
            nativeFunctionExecutor.removeVehicleComponent(
                    vehicleid = vehicleId.value,
                    componentid = VehicleComponentModel.BULLBARS_CHROME_GRILL.value
            )
        }
    }

    @Nested
    inner class GetVehicleComponentInSlotTests {

        @Test
        fun shouldReturnVehicleComponentModel() {
            every {
                nativeFunctionExecutor.getVehicleComponentInSlot(
                        vehicleid = vehicleId.value,
                        slot = CarModType.HOOD.value
                )
            } returns VehicleComponentModel.BULLBARS_CHROME_GRILL.value

            val componentModel = vehicleComponents[CarModType.HOOD]

            assertThat(componentModel)
                    .isEqualTo(VehicleComponentModel.BULLBARS_CHROME_GRILL)
        }

        @Test
        fun givenVehicleComponentInSlotIsZeroItShouldReturnNull() {
            every {
                nativeFunctionExecutor.getVehicleComponentInSlot(
                        vehicleid = vehicleId.value,
                        slot = CarModType.HOOD.value
                )
            } returns 0

            val componentModel = vehicleComponents[CarModType.HOOD]

            assertThat(componentModel)
                    .isNull()
        }
    }

}