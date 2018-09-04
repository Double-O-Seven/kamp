package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.id.ActorId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ActorRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val actorRegistry = ActorRegistry()

        val capacity = actorRegistry.capacity

        assertThat(capacity)
                .isEqualTo(SAMPConstants.MAX_ACTORS)
    }

}