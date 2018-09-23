package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnActorStreamInHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnActorStreamInListener>(relaxed = true)
        val listener2 = mockk<OnActorStreamInListener>(relaxed = true)
        val listener3 = mockk<OnActorStreamInListener>(relaxed = true)
        val actor = mockk<Actor>()
        val forPlayer = mockk<Player>()
        val onActorStreamInHandler = OnActorStreamInHandler()
        onActorStreamInHandler.register(listener1)
        onActorStreamInHandler.register(listener2)
        onActorStreamInHandler.register(listener3)

        onActorStreamInHandler.onActorStreamIn(actor = actor, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onActorStreamIn(actor = actor, forPlayer = forPlayer)
            listener2.onActorStreamIn(actor = actor, forPlayer = forPlayer)
            listener3.onActorStreamIn(actor = actor, forPlayer = forPlayer)
        }
    }

}