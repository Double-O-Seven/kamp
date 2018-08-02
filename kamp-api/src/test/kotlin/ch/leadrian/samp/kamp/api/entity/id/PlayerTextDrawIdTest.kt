package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerTextDrawIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        SAMPConstants.MAX_PLAYER_TEXT_DRAWS - 1,
        SAMPConstants.MAX_PLAYER_TEXT_DRAWS,
        Int.MAX_VALUE,
        SAMPConstants.INVALID_TEXT_DRAW
    ])
    fun shouldReturnPlayerTextDrawId(value: Int) {
        val playerTextDrawId = PlayerTextDrawId.valueOf(value)

        assertThat(playerTextDrawId.value)
                .isEqualTo(value)
    }

}