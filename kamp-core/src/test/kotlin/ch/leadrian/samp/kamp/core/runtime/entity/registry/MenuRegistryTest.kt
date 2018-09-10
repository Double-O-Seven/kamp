package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
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