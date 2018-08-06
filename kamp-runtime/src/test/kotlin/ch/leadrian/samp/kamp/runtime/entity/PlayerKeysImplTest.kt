package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.PlayerKey
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerKeysImplTest {

    @Nested
    inner class IsKeyPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

    @Nested
    inner class IsOnlyKeyPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isFalse()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

}