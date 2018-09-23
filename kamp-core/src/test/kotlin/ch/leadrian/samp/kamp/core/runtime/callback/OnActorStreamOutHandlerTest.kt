package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnActorStreamOutHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnActorStreamOutListener>(relaxed = true)
        val listener2 = mockk<OnActorStreamOutListener>(relaxed = true)
        val listener3 = mockk<OnActorStreamOutListener>(relaxed = true)
        val actor = mockk<Actor>()
        val forPlayer = mockk<Player>()
        val onActorStreamOutHandler = OnActorStreamOutHandler()
        onActorStreamOutHandler.register(listener1)
        onActorStreamOutHandler.register(listener2)
        onActorStreamOutHandler.register(listener3)

        onActorStreamOutHandler.onActorStreamOut(actor = actor, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onActorStreamOut(actor = actor, forPlayer = forPlayer)
            listener2.onActorStreamOut(actor = actor, forPlayer = forPlayer)
            listener3.onActorStreamOut(actor = actor, forPlayer = forPlayer)
        }
    }

}