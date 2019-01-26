package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
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

internal class PlayerPositionPropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerPositionProperty: PlayerPositionProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerPositionProperty = PlayerPositionProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetPosition() {
        every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { player.angle } returns 4f

        val position = playerPositionProperty.getValue(player, property)

        assertThat(position)
                .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
    }

    @Test
    fun shouldSetPosition() {
        player.apply {
            every { coordinates = any() } just Runs
            every { angle = any() } just Runs
        }

        playerPositionProperty.setValue(
                player,
                property,
                positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
        )

        verify {
            player.coordinates = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
            player.angle = 4f
        }
    }
}