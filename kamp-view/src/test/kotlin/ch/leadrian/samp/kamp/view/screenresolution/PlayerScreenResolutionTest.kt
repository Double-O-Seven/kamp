package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerScreenResolutionTest {

    private lateinit var playerScreenResolution: PlayerScreenResolution

    private val player = mockk<Player>()
    private val screenResolutionDialogProvider = mockk<ScreenResolutionDialogProvider>()

    @BeforeEach
    fun setUp() {
        playerScreenResolution = PlayerScreenResolution(player, screenResolutionDialogProvider)
    }

    @Test
    fun shouldInitializeWith1920x1080() {
        assertThat(playerScreenResolution)
                .satisfies {
                    assertThat(it.width)
                            .isEqualTo(1920)
                    assertThat(it.height)
                            .isEqualTo(1080)
                }
    }

    @Test
    fun shouldSetScreenResolution() {
        playerScreenResolution.set(640 x 480)

        assertThat(playerScreenResolution)
                .satisfies {
                    assertThat(it.width)
                            .isEqualTo(640)
                    assertThat(it.height)
                            .isEqualTo(480)
                }
    }

    @Test
    fun shouldShowDialog() {
        val dialog = mockk<Dialog> {
            every { show(any()) } just Runs
        }
        every { screenResolutionDialogProvider.get() } returns dialog

        playerScreenResolution.showSelectionDialog()

        verify { dialog.show(player) }
    }

    @Test
    fun shouldReturnPlayerScreenResolutionAsExtension() {
        val extensions = mockk<EntityExtensionContainer<Player>> {
            every { get(PlayerScreenResolution::class) } returns playerScreenResolution
        }
        val player = mockk<Player> {
            every { this@mockk.extensions } returns extensions
        }

        val extension = player.screenResolution

        assertThat(extension)
                .isEqualTo(playerScreenResolution)
    }

}