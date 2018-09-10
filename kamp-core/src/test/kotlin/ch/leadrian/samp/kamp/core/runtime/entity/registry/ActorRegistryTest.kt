package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ActorRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val actorRegistry = ActorRegistry()

        val capacity = actorRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_ACTORS)
    }

}