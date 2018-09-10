package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MapObjectRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val mapObjectRegistry = MapObjectRegistry()

        val capacity = mapObjectRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_OBJECTS)
    }

}