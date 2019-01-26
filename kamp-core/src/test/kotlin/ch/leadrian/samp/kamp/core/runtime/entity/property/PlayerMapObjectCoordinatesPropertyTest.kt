package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerMapObjectCoordinatesPropertyTest {

    private val playerMapObjectId: PlayerMapObjectId = PlayerMapObjectId.valueOf(50)
    private val playerMapObject: PlayerMapObject = mockk()
    private val playerId: PlayerId = PlayerId.valueOf(69)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerMapObjectCoordinatesProperty: PlayerMapObjectCoordinatesProperty

    @BeforeEach
    fun setUp() {
        every { playerMapObject.id } returns playerMapObjectId
        every { playerMapObject.player } returns player
        every { player.id } returns playerId
        playerMapObjectCoordinatesProperty = PlayerMapObjectCoordinatesProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetCoordinates() {
        every {
            nativeFunctionExecutor.getPlayerObjectPos(
                    playerid = playerId.value,
                    objectid = playerMapObjectId.value,
                    x = any(),
                    y = any(),
                    z = any()
            )
        } answers {
            thirdArg<ReferenceFloat>().value = 1f
            arg<ReferenceFloat>(3).value = 2f
            arg<ReferenceFloat>(4).value = 3f
            true
        }

        val coordinates = playerMapObjectCoordinatesProperty.getValue(playerMapObject, property)

        assertThat(coordinates)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetCoordinates() {
        every { nativeFunctionExecutor.setPlayerObjectPos(any(), any(), any(), any(), any()) } returns true

        playerMapObjectCoordinatesProperty.setValue(playerMapObject, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify {
            nativeFunctionExecutor.setPlayerObjectPos(
                    playerid = playerId.value,
                    objectid = playerMapObjectId.value,
                    x = 1f,
                    y = 2f,
                    z = 3f
            )
        }
    }
}