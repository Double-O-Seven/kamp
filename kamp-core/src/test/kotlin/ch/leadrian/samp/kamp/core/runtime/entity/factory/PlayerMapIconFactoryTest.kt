package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerMapIconFactoryTest {

    @Test
    fun shouldCreateMapIcon() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any())
            } returns true
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(69)
        }
        val playerMapIconFactory = PlayerMapIconFactory(nativeFunctionExecutor)

        val playerMapIcon = playerMapIconFactory.create(
                player = player,
                playerMapIconId = PlayerMapIconId.valueOf(50),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                color = Colors.RED,
                type = MapIconType.BALLAS,
                style = MapIconStyle.GLOBAL
        )

        assertThat(playerMapIcon)
                .isInstanceOfSatisfying(PlayerMapIcon::class.java) {
                    assertThat(it.color)
                            .isEqualTo(Colors.RED)
                    assertThat(it.id)
                            .isEqualTo(PlayerMapIconId.valueOf(50))
                    assertThat(it.player)
                            .isSameAs(player)
                    assertThat(it.coordinates)
                            .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
                    assertThat(it.type)
                            .isEqualTo(MapIconType.BALLAS)
                    assertThat(it.style)
                            .isEqualTo(MapIconStyle.GLOBAL)
                }

    }

}