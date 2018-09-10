package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.CarModType
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
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

        vehicleComponents.add(ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL)

        verify {
            nativeFunctionExecutor.addVehicleComponent(
                    vehicleid = vehicleId.value,
                    componentid = ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL.value
            )
        }
    }

    @Test
    fun shouldRemoveVehicleComponent() {
        every { nativeFunctionExecutor.removeVehicleComponent(any(), any()) } returns true

        vehicleComponents.remove(ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL)

        verify {
            nativeFunctionExecutor.removeVehicleComponent(
                    vehicleid = vehicleId.value,
                    componentid = ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL.value
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
                        slot = ch.leadrian.samp.kamp.core.api.constants.CarModType.HOOD.value
                )
            } returns ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL.value

            val componentModel = vehicleComponents[ch.leadrian.samp.kamp.core.api.constants.CarModType.HOOD]

            assertThat(componentModel)
                    .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel.BULLBARS_CHROME_GRILL)
        }

        @Test
        fun givenVehicleComponentInSlotIsZeroItShouldReturnNull() {
            every {
                nativeFunctionExecutor.getVehicleComponentInSlot(
                        vehicleid = vehicleId.value,
                        slot = ch.leadrian.samp.kamp.core.api.constants.CarModType.HOOD.value
                )
            } returns 0

            val componentModel = vehicleComponents[ch.leadrian.samp.kamp.core.api.constants.CarModType.HOOD]

            assertThat(componentModel)
                    .isNull()
        }
    }

}