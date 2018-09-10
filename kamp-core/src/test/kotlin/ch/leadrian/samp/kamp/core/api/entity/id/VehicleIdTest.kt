package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_VEHICLES - 1, SAMPConstants.MAX_VEHICLES, Int.MAX_VALUE, SAMPConstants.INVALID_VEHICLE_ID])
    fun shouldReturnVehicleId(value: Int) {
        val vehicleId = VehicleId.valueOf(value)

        assertThat(vehicleId.value)
                .isEqualTo(value)
    }

}