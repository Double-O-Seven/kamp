package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerRegistryTest {

    @Test
    fun shouldHaveExpectedCapacity() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns 69
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)

        val capacity = playerRegistry.capacity

        assertThat(capacity)
                .isEqualTo(69)
    }

}