package ch.leadrian.samp.kamp.api.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerClassIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 319, 320, 999])
    fun shouldReturnPlayerId(value: Int) {
        val playerClassId = PlayerClassId.valueOf(value)

        assertThat(playerClassId.value)
                .isEqualTo(value)
    }
}