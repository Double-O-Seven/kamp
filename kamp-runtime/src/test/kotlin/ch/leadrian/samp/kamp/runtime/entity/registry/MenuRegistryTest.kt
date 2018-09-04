package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MenuRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val menuRegistry = MenuRegistry()

        val capacity = menuRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_MENUS)
    }

}