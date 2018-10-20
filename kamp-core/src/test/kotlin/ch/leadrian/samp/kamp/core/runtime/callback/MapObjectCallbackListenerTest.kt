package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class MapObjectCallbackListenerTest {

    private lateinit var mapObjectCallbackListener: MapObjectCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        mapObjectCallbackListener = MapObjectCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        mapObjectCallbackListener.initialize()

        verify { callbackListenerManager.register(mapObjectCallbackListener) }
    }

    @Test
    fun shouldCallOnMovedOnMapObject() {
        val mapObject = mockk<MapObject> {
            every { onMoved() } just Runs
        }

        mapObjectCallbackListener.onObjectMoved(mapObject)

        verify(exactly = 1) { mapObject.onMoved() }
    }

    @ParameterizedTest
    @EnumSource(ObjectEditResponse::class)
    fun shouldCallOnEdit(response: ObjectEditResponse) {
        val mapObject = mockk<MapObject> {
            every { onEdit(any(), any(), any(), any()) } just Runs
        }
        val player = mockk<Player>()
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)

        mapObjectCallbackListener.onPlayerEditMapObject(
                player = player,
                mapObject = mapObject,
                response = response,
                offset = offset,
                rotation = rotation
        )

        verify(exactly = 1) {
            mapObject.onEdit(
                    player = player,
                    response = response,
                    offset = offset,
                    rotation = rotation
            )
        }
    }

    @Test
    fun shouldCallOnSelectOnMapObject() {
        val player = mockk<Player>()
        val mapObject = mockk<MapObject> {
            every { onSelect(player, 1337, vector3DOf(1f, 2f, 3f)) } just Runs
        }

        mapObjectCallbackListener.onPlayerSelectMapObject(player, mapObject, 1337, vector3DOf(1f, 2f, 3f))

        verify(exactly = 1) { mapObject.onSelect(player, 1337, vector3DOf(1f, 2f, 3f)) }
    }

}