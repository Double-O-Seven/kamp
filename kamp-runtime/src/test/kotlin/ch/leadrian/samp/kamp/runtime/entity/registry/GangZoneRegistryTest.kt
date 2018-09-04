package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GangZoneRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val gangZoneRegistry = GangZoneRegistry()

        val capacity = gangZoneRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_GANG_ZONES)
    }

}