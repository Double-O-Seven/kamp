package ch.leadrian.samp.kamp.core.runtime.entity.registry

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerClassRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val playerClassRegistry = PlayerClassRegistry()

        val capacity = playerClassRegistry.capacity

        assertThat(capacity)
                .isEqualTo(320)
    }

}