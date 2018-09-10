package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleColorsTest {

    @Test
    fun toVehicleColorsShouldReturnSameInstance() {
        val expectedVehicleColors = vehicleColorsOf(
                color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[65],
                color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[127]
        )

        val vehicleColors: VehicleColors = expectedVehicleColors.toVehicleColors()

        assertThat(vehicleColors)
                .isNotInstanceOf(MutableVehicleColors::class.java)
                .isSameAs(expectedVehicleColors)
    }

    @Test
    fun toMutableVehicleColorsShouldReturnMutableInstance() {
        val vehicleColors = vehicleColorsOf(
                color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[65],
                color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[127]
        )

        val mutableVehicleColors: MutableVehicleColors = vehicleColors.toMutableVehicleColors()

        assertThat(mutableVehicleColors)
                .satisfies {
                    assertThat(it.color1)
                            .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.VehicleColor[65])
                    assertThat(it.color2)
                            .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.VehicleColor[127])
                }
    }
}