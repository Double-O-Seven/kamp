package ch.leadrian.samp.kamp.core.runtime.entity

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerKeysImplTest {

    @Nested
    inner class IsKeyPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnTrue() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

    @Nested
    inner class IsOnlyKeyPressedTests {

        @Test
        fun exactKeysPressedShouldReturnTrue() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE)

            assertThat(pressed)
                    .isTrue()
        }

        @Test
        fun givenSomeOfPressedKeysShouldBePressedItShouldReturnFalse() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP)

            assertThat(pressed)
                    .isFalse()
        }

        @Test
        fun givenNotAllKeysArePressedItShouldReturnFalse() {
            val keys = ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION.value or ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE.value
            val playerKeys = PlayerKeysImpl(
                    keys = keys,
                    upDown = 0,
                    leftRight = 0,
                    player = mockk()
            )

            val pressed = playerKeys.isOnlyKeyPressed(ch.leadrian.samp.kamp.core.api.constants.PlayerKey.ACTION, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.JUMP, ch.leadrian.samp.kamp.core.api.constants.PlayerKey.FIRE)

            assertThat(pressed)
                    .isFalse()
        }
    }

}