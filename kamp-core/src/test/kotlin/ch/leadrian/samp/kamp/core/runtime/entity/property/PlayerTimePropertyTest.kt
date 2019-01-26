package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.timeOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerTimePropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerTimeProperty: PlayerTimeProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerTimeProperty = PlayerTimeProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetTime() {
        every { nativeFunctionExecutor.getPlayerTime(playerId.value, any(), any()) } answers {
            secondArg<ReferenceInt>().value = 13
            thirdArg<ReferenceInt>().value = 37
            true
        }

        val time = playerTimeProperty.getValue(player, property)

        Assertions.assertThat(time)
                .isEqualTo(timeOf(hour = 13, minute = 37))
    }

    @Test
    fun shouldSetTime() {
        every { nativeFunctionExecutor.setPlayerTime(any(), any(), any()) } returns true

        playerTimeProperty.setValue(player, property, timeOf(hour = 13, minute = 37))

        verify { nativeFunctionExecutor.setPlayerTime(playerid = playerId.value, hour = 13, minute = 37) }
    }
}