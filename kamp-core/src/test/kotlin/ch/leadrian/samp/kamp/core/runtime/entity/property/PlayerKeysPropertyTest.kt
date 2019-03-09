package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.playerKeysOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerKeysPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerKeysProperty: PlayerKeysProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerKeysProperty = PlayerKeysProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetPlayerKeys() {
        every { nativeFunctionExecutor.getPlayerKeys(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceInt>().value = 50
            thirdArg<ReferenceInt>().value = 75
            arg<ReferenceInt>(3).value = 100
            true
        }

        val playerKeys = playerKeysProperty.getValue(player, property)

        assertThat(playerKeys)
                .isEqualTo(playerKeysOf(keys = 50, upDown = 75, leftRight = 100))
    }

}