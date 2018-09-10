package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleDoorsDamageStatusTest {

    @Test
    fun shouldReturnValues() {
        val vehicleDoorsDamageStatus = VehicleDoorsDamageStatus(0b0111_0110_0101_0011)

        assertThat(vehicleDoorsDamageStatus)
                .satisfies {
                    assertThat(it.hood)
                            .isEqualTo(VehicleDoorDamageStatusValue(0b011))
                    assertThat(it.trunk)
                            .isEqualTo(VehicleDoorDamageStatusValue(0b101))
                    assertThat(it.driver)
                            .isEqualTo(VehicleDoorDamageStatusValue(0b110))
                    assertThat(it.coDriver)
                            .isEqualTo(VehicleDoorDamageStatusValue(0b111))
                }
    }

    @Test
    fun shouldSetValues() {
        val vehicleDoorsDamageStatus = VehicleDoorsDamageStatus(0)

        vehicleDoorsDamageStatus.apply {
            hood = VehicleDoorDamageStatusValue(0b011)
            trunk = VehicleDoorDamageStatusValue(0b101)
            driver = VehicleDoorDamageStatusValue(0b110)
            coDriver = VehicleDoorDamageStatusValue(0b111)
        }

        assertThat(vehicleDoorsDamageStatus.value)
                .isEqualTo(0b0111_0110_0101_0011)
    }

}
