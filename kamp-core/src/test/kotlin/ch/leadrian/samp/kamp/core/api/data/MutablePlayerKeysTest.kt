package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class MutablePlayerKeysTest {

    @Test
    fun shouldPressKeys() {
        val mutablePlayerKeys = mutablePlayerKeysOf()

        mutablePlayerKeys.press(PlayerKey.ACTION, PlayerKey.FIRE)

        assertThat(mutablePlayerKeys.isPressed(PlayerKey.ACTION, PlayerKey.FIRE))
                .isTrue()
    }

    @Test
    fun shouldReleaseKeys() {
        val mutablePlayerKeys = mutablePlayerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE)

        mutablePlayerKeys.release(PlayerKey.FIRE)

        assertThat(mutablePlayerKeys.isPressedExactly(PlayerKey.ACTION))
                .isTrue()
    }

    @Test
    fun shouldPressExactly() {
        val mutablePlayerKeys = mutablePlayerKeysOf(PlayerKey.CROUCH, PlayerKey.YES)

        mutablePlayerKeys.pressExactly(PlayerKey.ACTION, PlayerKey.FIRE)

        assertThat(mutablePlayerKeys.isPressedExactly(PlayerKey.ACTION, PlayerKey.FIRE))
                .isTrue()
    }

    @Test
    fun toMutablePlayerKeysShouldReturnThis() {
        val mutablePlayerKeys = mutablePlayerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE)

        val otherMutablePlayerKeys = mutablePlayerKeys.toMutablePlayerKeys()

        assertThat(otherMutablePlayerKeys)
                .isSameAs(mutablePlayerKeys)
    }

    @Test
    fun toMutablePlayerKeysShouldReturnInstanceWithSameKeysPressed() {
        val mutablePlayerKeys = mutablePlayerKeysOf(PlayerKey.ACTION, PlayerKey.FIRE, upDown = 123, leftRight = 456)

        val otherMutablePlayerKeys = mutablePlayerKeys.toPlayerKeys()

        assertAll(
                { assertThat(otherMutablePlayerKeys.keys).isEqualTo(mutablePlayerKeys.keys) },
                { assertThat(otherMutablePlayerKeys.upDown).isEqualTo(mutablePlayerKeys.upDown) },
                { assertThat(otherMutablePlayerKeys.leftRight).isEqualTo(mutablePlayerKeys.leftRight) }
        )
    }

}