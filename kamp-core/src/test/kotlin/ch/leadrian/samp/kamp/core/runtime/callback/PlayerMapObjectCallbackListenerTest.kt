package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


internal class PlayerMapObjectCallbackListenerTest {

    private lateinit var playerMapObjectCallbackListener: PlayerMapObjectCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        playerMapObjectCallbackListener = PlayerMapObjectCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        playerMapObjectCallbackListener.initialize()

        verify { callbackListenerManager.register(playerMapObjectCallbackListener) }
    }

    @Test
    fun shouldCallOnMovedOnPlayerMapObject() {
        val playerMapObject = mockk<PlayerMapObject> {
            every { onMoved() } just Runs
        }

        playerMapObjectCallbackListener.onPlayerObjectMoved(playerMapObject)

        verify(exactly = 1) { playerMapObject.onMoved() }
    }

    @ParameterizedTest
    @EnumSource(ObjectEditResponse::class)
    fun shouldCallOnEdit(response: ObjectEditResponse) {
        val playerMapObject = mockk<PlayerMapObject> {
            every { onEdit(any(), any(), any()) } just Runs
        }
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)

        playerMapObjectCallbackListener.onPlayerEditPlayerMapObject(
                playerMapObject = playerMapObject,
                response = response,
                offset = offset,
                rotation = rotation
        )

        verify(exactly = 1) {
            playerMapObject.onEdit(
                    response = response,
                    offset = offset,
                    rotation = rotation
            )
        }
    }

}