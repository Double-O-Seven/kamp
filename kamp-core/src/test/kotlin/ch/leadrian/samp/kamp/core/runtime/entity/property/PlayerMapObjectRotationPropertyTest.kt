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

internal class PlayerMapObjectRotationPropertyTest {

    private val playerMapObjectId: PlayerMapObjectId = PlayerMapObjectId.valueOf(50)
    private val playerMapObject: PlayerMapObject = mockk()
    private val playerId: PlayerId = PlayerId.valueOf(69)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerMapObjectRotationProperty: PlayerMapObjectRotationProperty

    @BeforeEach
    fun setUp() {
        every { playerMapObject.id } returns playerMapObjectId
        every { playerMapObject.player } returns player
        every { player.id } returns playerId
        playerMapObjectRotationProperty = PlayerMapObjectRotationProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetRotation() {
        every {
            nativeFunctionExecutor.getPlayerObjectRot(
                    playerid = playerId.value,
                    objectid = playerMapObjectId.value,
                    rotX = any(),
                    rotY = any(),
                    rotZ = any()
            )
        } answers {
            thirdArg<ReferenceFloat>().value = 1f
            arg<ReferenceFloat>(3).value = 2f
            arg<ReferenceFloat>(4).value = 3f
            true
        }

        val rotation = playerMapObjectRotationProperty.getValue(playerMapObject, property)

        assertThat(rotation)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetRotation() {
        every { nativeFunctionExecutor.setPlayerObjectRot(any(), any(), any(), any(), any()) } returns true

        playerMapObjectRotationProperty.setValue(playerMapObject, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify {
            nativeFunctionExecutor.setPlayerObjectRot(
                    playerid = playerId.value,
                    objectid = playerMapObjectId.value,
                    rotX = 1f,
                    rotY = 2f,
                    rotZ = 3f
            )
        }
    }
}