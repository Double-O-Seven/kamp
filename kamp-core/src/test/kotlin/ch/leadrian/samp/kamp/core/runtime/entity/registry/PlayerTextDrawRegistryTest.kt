package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerTextDrawRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val playerTextDrawRegistry = PlayerTextDrawRegistry()

        val capacity = playerTextDrawRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_PLAYER_TEXT_DRAWS)
    }

}