package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val vehicleRegistry = VehicleRegistry()

        val capacity = vehicleRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_VEHICLES)
    }

}