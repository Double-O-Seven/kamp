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

internal class PlayerArmourPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerArmourProperty: PlayerArmourProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerArmourProperty = PlayerArmourProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetArmour() {
        every { nativeFunctionExecutor.getPlayerArmour(playerId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val armour = playerArmourProperty.getValue(player, property)

        assertThat(armour)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetArmour() {
        every { nativeFunctionExecutor.setPlayerArmour(any(), any()) } returns true

        playerArmourProperty.setValue(player, property, 4f)

        verify { nativeFunctionExecutor.setPlayerArmour(playerid = playerId.value, armour = 4f) }
    }

}