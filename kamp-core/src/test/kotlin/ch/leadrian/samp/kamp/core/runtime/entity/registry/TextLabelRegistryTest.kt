package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TextLabelRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val textLabelRegistry = TextLabelRegistry()

        val capacity = textLabelRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_3DTEXT_GLOBAL)
    }

}