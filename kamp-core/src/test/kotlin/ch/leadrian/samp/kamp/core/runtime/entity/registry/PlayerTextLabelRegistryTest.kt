package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerTextLabelRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val playerTextLabelRegistry = PlayerTextLabelRegistry()

        val capacity = playerTextLabelRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_3DTEXT_PLAYER)
    }

}