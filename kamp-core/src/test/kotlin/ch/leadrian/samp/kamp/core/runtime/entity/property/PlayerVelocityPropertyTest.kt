package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerVelocityPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerVelocityProperty: PlayerVelocityProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerVelocityProperty = PlayerVelocityProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetVelocity() {
        every { nativeFunctionExecutor.getPlayerVelocity(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val velocity = playerVelocityProperty.getValue(player, property)

        assertThat(velocity)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetVelocity() {
        every { nativeFunctionExecutor.setPlayerVelocity(any(), any(), any(), any()) } returns true

        playerVelocityProperty.setValue(player, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setPlayerVelocity(playerid = playerId.value, x = 1f, y = 2f, z = 3f) }
    }
}