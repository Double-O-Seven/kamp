package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerGiveDamageActorHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
        val listener2 = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
        val listener3 = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
        val player = mockk<Player>()
        val actor = mockk<Actor>()
        val onPlayerGiveDamageActorHandler = OnPlayerGiveDamageActorHandler()
        onPlayerGiveDamageActorHandler.register(listener1)
        onPlayerGiveDamageActorHandler.register(listener2)
        onPlayerGiveDamageActorHandler.register(listener3)

        onPlayerGiveDamageActorHandler.onPlayerGiveDamageActor(player, actor, 13.37f, WeaponModel.AK47, BodyPart.GROIN)

        verify(exactly = 1) {
            listener1.onPlayerGiveDamageActor(player, actor, 13.37f, WeaponModel.AK47, BodyPart.GROIN)
            listener2.onPlayerGiveDamageActor(player, actor, 13.37f, WeaponModel.AK47, BodyPart.GROIN)
            listener3.onPlayerGiveDamageActor(player, actor, 13.37f, WeaponModel.AK47, BodyPart.GROIN)
        }
    }

}