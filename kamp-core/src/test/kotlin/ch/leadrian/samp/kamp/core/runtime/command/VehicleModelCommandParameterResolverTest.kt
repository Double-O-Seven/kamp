package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleModelCommandParameterResolverTest {

    private val vehicleModelCommandParameterResolver = VehicleModelCommandParameterResolver()

    @Test
    fun givenValidModelIdItShouldReturnVehicleModel() {
        val vehicleModel = vehicleModelCommandParameterResolver.resolve(SAMPConstants.VEHICLE_DUMPER.toString())

        assertThat(vehicleModel)
                .isEqualTo(VehicleModel.DUMPER)
    }

    @Test
    fun givenInvalidModelIdItShouldReturnNull() {
        val vehicleModel = vehicleModelCommandParameterResolver.resolve("999999")

        assertThat(vehicleModel)
                .isNull()
    }

    @Test
    fun givenValidVehicleModelNameItShouldReturnVehicleModel() {
        val vehicleModel = vehicleModelCommandParameterResolver.resolve("FBI Rancher")

        assertThat(vehicleModel)
                .isEqualTo(VehicleModel.FBIRANCHER)
    }

    @Test
    fun givenVehicleModelEnumNameItShouldReturnVehicleModel() {
        val vehicleModel = vehicleModelCommandParameterResolver.resolve("FBIRANCHER")

        assertThat(vehicleModel)
                .isEqualTo(VehicleModel.FBIRANCHER)
    }

    @Test
    fun givenInvalidVehicleModelNameItShouldReturnNull() {
        val vehicleModel = vehicleModelCommandParameterResolver.resolve("hahaha")

        assertThat(vehicleModel)
                .isNull()
    }

}