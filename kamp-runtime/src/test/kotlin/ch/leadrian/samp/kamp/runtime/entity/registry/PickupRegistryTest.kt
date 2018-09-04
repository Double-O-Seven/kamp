package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PickupRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val pickupRegistry = PickupRegistry()

        val capacity = pickupRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_PICKUPS)
    }

}