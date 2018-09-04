package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerMapObjectRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val playerMapObjectRegistry = PlayerMapObjectRegistry()

        val capacity = playerMapObjectRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_OBJECTS)
    }

}