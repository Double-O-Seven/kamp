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

internal class PlayerHealthPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerHealthProperty: PlayerHealthProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerHealthProperty = PlayerHealthProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetHealth() {
        every { nativeFunctionExecutor.getPlayerHealth(playerId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val health = playerHealthProperty.getValue(player, property)

        assertThat(health)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetHealth() {
        every { nativeFunctionExecutor.setPlayerHealth(any(), any()) } returns true

        playerHealthProperty.setValue(player, property, 4f)

        verify { nativeFunctionExecutor.setPlayerHealth(playerid = playerId.value, health = 4f) }
    }

}