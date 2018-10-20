package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ActorCallbackListenerTest {

    private lateinit var actorCallbackListener: ActorCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        actorCallbackListener = ActorCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        actorCallbackListener.initialize()

        verify { callbackListenerManager.register(actorCallbackListener) }
    }

    @Test
    fun shouldExecuteOnStreamIn() {
        val player = mockk<Player>()
        val actor = mockk<Actor> {
            every { onStreamIn(any<Player>()) } just Runs
        }

        actorCallbackListener.onActorStreamIn(actor, player)

        verify { actor.onStreamIn(player) }
    }

    @Test
    fun shouldExecuteOnStreamOut() {
        val player = mockk<Player>()
        val actor = mockk<Actor> {
            every { onStreamOut(any<Player>()) } just Runs
        }

        actorCallbackListener.onActorStreamOut(actor, player)

        verify { actor.onStreamOut(player) }
    }

}