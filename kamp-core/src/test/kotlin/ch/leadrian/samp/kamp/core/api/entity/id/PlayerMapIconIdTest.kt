package ch.leadrian.samp.kamp.core.api.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerMapIconIdTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 50, 99])
    fun shouldReturnPlayerMapIconId(value: Int) {
        val playerMapIconId = PlayerMapIconId.valueOf(value)

        assertThat(playerMapIconId.value)
                .isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 100, 200])
    fun givenInvalidPlayerMapIconIdItShouldThrowAnException(value: Int) {
        val caughtThrowable = catchThrowable { PlayerMapIconId.valueOf(value) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
    }
}