package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
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

internal class PlayerAnglePropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerAngleProperty: PlayerAngleProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerAngleProperty = PlayerAngleProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetAngle() {
        every { nativeFunctionExecutor.getPlayerFacingAngle(playerId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val angle = playerAngleProperty.getValue(player, property)

        assertThat(angle)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetAngle() {
        every { nativeFunctionExecutor.setPlayerFacingAngle(any(), any()) } returns true

        playerAngleProperty.setValue(player, property, 4f)

        verify { nativeFunctionExecutor.setPlayerFacingAngle(playerid = playerId.value, angle = 4f) }
    }

}