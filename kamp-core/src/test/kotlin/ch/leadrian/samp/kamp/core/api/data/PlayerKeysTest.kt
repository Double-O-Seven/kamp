package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerKeysTest {

    @Nested
    inner class IsKeyPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
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
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isFalse()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value
            val playerKeys = PlayerKeys(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isOnlyKeyPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

}