package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class PlayerKeysTest {

    @Nested
    inner class IsPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE, PlayerKey.JUMP)

            val pressed = playerKeys.isPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnTrue() {
            val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE, PlayerKey.JUMP)

            val pressed = playerKeys.isPressed(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE)

            val pressed = playerKeys.isPressed(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

    @Nested
    inner class IsPressedExactlyTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = PlayerKey.ACTION.value or PlayerKey.FIRE.value or PlayerKey.JUMP.value
            val playerKeys = playerKeysOf(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0
            )

            val pressed = playerKeys.isPressedExactly(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnFalse() {
            val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE, PlayerKey.JUMP)

            val pressed = playerKeys.isPressedExactly(PlayerKey.ACTION, PlayerKey.JUMP)

            assertThat(pressed)
                    .isFalse()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE)

            val pressed = playerKeys.isPressedExactly(PlayerKey.ACTION, PlayerKey.JUMP, PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

    @Test
    fun toPlayerKeysShouldReturnThis() {
        val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE)

        val otherPlayerKeys = playerKeys.toPlayerKeys()

        assertThat(otherPlayerKeys)
                .isSameAs(playerKeys)
    }

    @Test
    fun toMutablePlayerKeysShouldReturnInstanceWithSameKeysPressed() {
        val playerKeys = playerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE, upDown = 123, leftRight = 456)

        val mutablePlayerKeys = playerKeys.toMutablePlayerKeys()

        assertAll(
                { assertThat(mutablePlayerKeys.keys).isEqualTo(playerKeys.keys) },
                { assertThat(mutablePlayerKeys.upDown).isEqualTo(playerKeys.upDown) },
                { assertThat(mutablePlayerKeys.leftRight).isEqualTo(playerKeys.leftRight) }
        )
    }

}