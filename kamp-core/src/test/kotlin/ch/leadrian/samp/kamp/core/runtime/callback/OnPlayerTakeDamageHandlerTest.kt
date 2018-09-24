package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTakeDamageListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerTakeDamageHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val listener2 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val listener3 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val player = mockk<Player>()
        val issuer = mockk<Player>()
        val onPlayerTakeDamageHandler = OnPlayerTakeDamageHandler()
        onPlayerTakeDamageHandler.register(listener1)
        onPlayerTakeDamageHandler.register(listener2)
        onPlayerTakeDamageHandler.register(listener3)

        onPlayerTakeDamageHandler.onPlayerTakeDamage(
                player = player,
                issuer = issuer,
                amount = 13.37f,
                weaponModel = WeaponModel.AK47,
                bodyPart = BodyPart.GROIN
        )

        verify(exactly = 1) {
            listener1.onPlayerTakeDamage(
                    player = player,
                    issuer = issuer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener2.onPlayerTakeDamage(
                    player = player,
                    issuer = issuer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener3.onPlayerTakeDamage(
                    player = player,
                    issuer = issuer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
        }
    }

    @Test
    fun givenNoIssuerItShouldCallAllListeners() {
        val listener1 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val listener2 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val listener3 = mockk<OnPlayerTakeDamageListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerTakeDamageHandler = OnPlayerTakeDamageHandler()
        onPlayerTakeDamageHandler.register(listener1)
        onPlayerTakeDamageHandler.register(listener2)
        onPlayerTakeDamageHandler.register(listener3)

        onPlayerTakeDamageHandler.onPlayerTakeDamage(
                player = player,
                issuer = null,
                amount = 13.37f,
                weaponModel = WeaponModel.AK47,
                bodyPart = BodyPart.GROIN
        )

        verify(exactly = 1) {
            listener1.onPlayerTakeDamage(
                    player = player,
                    issuer = null,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener2.onPlayerTakeDamage(
                    player = player,
                    issuer = null,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
            listener3.onPlayerTakeDamage(
                    player = player,
                    issuer = null,
                    amount = 13.37f,
                    weaponModel = WeaponModel.AK47,
                    bodyPart = BodyPart.GROIN
            )
        }
    }

}