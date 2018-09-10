package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TextDrawRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val textDrawRegistry = TextDrawRegistry()

        val capacity = textDrawRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_TEXT_DRAWS)
    }

}