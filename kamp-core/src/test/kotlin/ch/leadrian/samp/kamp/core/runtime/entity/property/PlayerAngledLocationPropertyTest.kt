package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerAngledLocationPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerAngledLocationProperty: PlayerAngledLocationProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerAngledLocationProperty = PlayerAngledLocationProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetAngledLocation() {
        every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { player.angle } returns 4f
        every { player.interiorId } returns 5
        every { player.virtualWorldId } returns 6

        val angledLocation = playerAngledLocationProperty.getValue(player, property)

        assertThat(angledLocation)
                .isEqualTo(angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 5, worldId = 6))
    }

    @Test
    fun shouldSetAngledLocation() {
        player.apply {
            every { coordinates = any() } just Runs
            every { angle = any() } just Runs
            every { interiorId = any() } just Runs
            every { virtualWorldId = any() } just Runs
        }

        playerAngledLocationProperty.setValue(
                player,
                property,
                angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 5, worldId = 6)
        )

        verify {
            player.coordinates = angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 5, worldId = 6)
            player.angle = 4f
            player.interiorId = 5
            player.virtualWorldId = 6
        }
    }
}