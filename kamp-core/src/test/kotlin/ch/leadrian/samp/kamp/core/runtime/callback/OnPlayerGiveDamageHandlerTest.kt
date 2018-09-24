package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerGiveDamageHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerGiveDamageListener>(relaxed = true)
        val listener2 = mockk<OnPlayerGiveDamageListener>(relaxed = true)
        val listener3 = mockk<OnPlayerGiveDamageListener>(relaxed = true)
        val player = mockk<Player>()
        val damagedPlayer = mockk<Player>()
        val onPlayerGiveDamageHandler = OnPlayerGiveDamageHandler()
        onPlayerGiveDamageHandler.register(listener1)
        onPlayerGiveDamageHandler.register(listener2)
        onPlayerGiveDamageHandler.register(listener3)

        onPlayerGiveDamageHandler.onPlayerGiveDamage(
                player = player,
                damagedPlayer = damagedPlayer,
                amount = 13.37f,
                weaponModel = WeaponModel.AK47,
                bodyPart = BodyPart.GROIN
        )

        verify(exactly = 1) {
            listener1.onPlayerGiveDamage(
                    player = player,
                    damagedPlayer = damagedPlayer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener2.onPlayerGiveDamage(
                    player = player,
                    damagedPlayer = damagedPlayer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener3.onPlayerGiveDamage(
                    player = player,
                    damagedPlayer = damagedPlayer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
        }
    }

}