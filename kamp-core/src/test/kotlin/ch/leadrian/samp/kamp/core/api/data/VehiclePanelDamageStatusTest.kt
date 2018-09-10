package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehiclePanelDamageStatusTest {

    @Test
    fun shouldReturnValues() {
        val vehiclePanelDamageStatus = VehiclePanelDamageStatus(0x0FEDCBA9)

        assertThat(vehiclePanelDamageStatus)
                .satisfies {
                    assertThat(it.frontLeftPanel)
                            .isEqualTo(0x9)
                    assertThat(it.frontRightPanel)
                            .isEqualTo(0xA)
                    assertThat(it.rearLeftPanel)
                            .isEqualTo(0xB)
                    assertThat(it.rearRightPanel)
                            .isEqualTo(0xC)
                    assertThat(it.windshield)
                            .isEqualTo(0xD)
                    assertThat(it.frontBumper)
                            .isEqualTo(0xE)
                    assertThat(it.rearBumper)
                            .isEqualTo(0xF)
                }
    }

    @Test
    fun shouldSetValues() {
        val vehiclePanelDamageStatus = VehiclePanelDamageStatus(0)

        vehiclePanelDamageStatus.apply {
            frontLeftPanel = 0x9
            frontRightPanel = 0xA
            rearLeftPanel = 0xB
            rearRightPanel = 0xC
            windshield = 0xD
            frontBumper = 0xE
            rearBumper = 0xF
        }

        assertThat(vehiclePanelDamageStatus.value)
                .isEqualTo(0x0FEDCBA9)
    }

}