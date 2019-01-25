package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
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

internal class PlayerLocationDelegateTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerLocationDelegate: PlayerLocationDelegate

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerLocationDelegate = PlayerLocationDelegate(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetLocation() {
        every { nativeFunctionExecutor.getPlayerPos(playerId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { player.interiorId } returns 5
        every { player.virtualWorldId } returns 6

        val location = playerLocationDelegate.getValue(player, property)

        assertThat(location)
                .isEqualTo(locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6))
    }

    @Test
    fun shouldSetLocation() {
        player.apply {
            every { coordinates = any() } just Runs
            every { interiorId = any() } just Runs
            every { virtualWorldId = any() } just Runs
        }

        playerLocationDelegate.setValue(
                player,
                property,
                locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6)
        )

        verify {
            player.coordinates = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 5, worldId = 6)
            player.interiorId = 5
            player.virtualWorldId = 6
        }
    }
}