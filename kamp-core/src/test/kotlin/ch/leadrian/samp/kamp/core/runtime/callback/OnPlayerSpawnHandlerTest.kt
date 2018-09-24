package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerSpawnHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerSpawnListener>(relaxed = true)
        val listener2 = mockk<OnPlayerSpawnListener>(relaxed = true)
        val listener3 = mockk<OnPlayerSpawnListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerSpawnHandler = OnPlayerSpawnHandler()
        onPlayerSpawnHandler.register(listener1)
        onPlayerSpawnHandler.register(listener2)
        onPlayerSpawnHandler.register(listener3)

        onPlayerSpawnHandler.onPlayerSpawn(player)

        verify(exactly = 1) {
            listener1.onPlayerSpawn(player)
            listener2.onPlayerSpawn(player)
            listener3.onPlayerSpawn(player)
        }
    }

}