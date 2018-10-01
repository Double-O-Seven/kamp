package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PickupCallbackListenerTest {

    private lateinit var pickupCallbackListener: PickupCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        pickupCallbackListener = PickupCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        pickupCallbackListener.initialize()

        verify { callbackListenerManager.register(pickupCallbackListener) }
    }

    @Test
    fun shouldCallOnPickUp() {
        val player = mockk<Player>()
        val pickup = mockk<Pickup> {
            every { onPickUp(any<Player>()) } just Runs
        }

        pickupCallbackListener.onPlayerPickUpPickup(player, pickup)

        verify { pickup.onPickUp(player) }
    }
}