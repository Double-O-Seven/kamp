package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerAnimationTest {

    private lateinit var playerAnimation: PlayerAnimation

    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerAnimation = PlayerAnimation(player, nativeFunctionExecutor)
    }

    @ParameterizedTest
    @CsvSource(
            "false, false, false, false, false",
            "true, true, true, true, true",
            "true, false, false, false, false",
            "false, true, false, false, false",
            "false, false, true, false, false",
            "false, false, false, true, false",
            "false, false, false, false, true"
    )
    fun shouldApplyAnimation(loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, forceSync: Boolean) {
        every {
            nativeFunctionExecutor.applyAnimation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns true

        playerAnimation.apply(
                animation = ch.leadrian.samp.kamp.core.api.data.Animation.valueOf(library = "ABC", name = "xyz"),
                fDelta = 1f,
                time = 60,
                loop = loop,
                lockX = lockX,
                lockY = lockY,
                freeze = freeze,
                forceSync = forceSync
        )

        verify {
            nativeFunctionExecutor.applyAnimation(
                    animlib = "ABC",
                    animname = "xyz",
                    playerid = playerId.value,
                    fDelta = 1f,
                    time = 60,
                    loop = loop,
                    lockx = lockX,
                    locky = lockY,
                    freeze = freeze,
                    forcesync = forceSync
            )
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldClearAnimation(forceSync: Boolean) {
        every { nativeFunctionExecutor.clearAnimations(any(), any()) } returns true

        playerAnimation.clear(forceSync)

        verify { nativeFunctionExecutor.clearAnimations(playerId.value, forceSync) }
    }

    @Test
    fun shouldGetAnimationIndex() {
        every { nativeFunctionExecutor.getPlayerAnimationIndex(playerId.value) } returns 69

        val animationIndex = playerAnimation.index

        Assertions.assertThat(animationIndex)
                .isEqualTo(69)
    }

}