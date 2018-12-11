package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerScreenResolutionFactoryTest {

    @Test
    fun shouldCreatePlayerScreenResolutionForPlayer() {
        val player = mockk<Player>()
        val screenResolutionDialogProvider = mockk<ScreenResolutionDialogProvider>()
        val playerScreenResolutionFactory = PlayerScreenResolutionFactory(screenResolutionDialogProvider)

        val screenResolution = playerScreenResolutionFactory.create(player)

        assertThat(screenResolution.player)
                .isEqualTo(player)
    }

}