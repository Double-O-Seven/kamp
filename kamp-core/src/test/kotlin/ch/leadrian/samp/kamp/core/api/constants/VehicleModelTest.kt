package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleModelTest {

    @ParameterizedTest
    @ValueSource(strings = ["Infernus", "INFERNUS", "infernus"])
    fun shouldReturnExactlyMatchingVehicleModel(modelName: String) {
        val vehicleModel = VehicleModel[modelName]

        assertThat(vehicleModel)
                .isEqualTo(VehicleModel.INFERNUS)
    }

    @Test
    fun givenExactlyOnePartialMatchItShouldReturnIt() {
        val vehicleModel = VehicleModel["inf"]

        assertThat(vehicleModel)
                .isEqualTo(VehicleModel.INFERNUS)
    }

    @Test
    fun givenNoMatchItShouldReturnNull() {
        val vehicleModel = VehicleModel["hahaha"]

        assertThat(vehicleModel)
                .isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["Police", "Luggage Trailer", "Monster"])
    fun givenMultipleMatchesItShouldReturnNull(modelName: String) {
        val vehicleModel = VehicleModel[modelName]

        assertThat(vehicleModel)
                .isNull()
    }

}