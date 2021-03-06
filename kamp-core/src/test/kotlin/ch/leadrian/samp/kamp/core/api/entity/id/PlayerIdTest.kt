package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_PLAYERS - 1, SAMPConstants.MAX_PLAYERS, Int.MAX_VALUE, SAMPConstants.INVALID_PLAYER_ID])
    fun shouldReturnPlayerId(value: Int) {
        val playerId = PlayerId.valueOf(value)

        assertThat(playerId.value)
                .isEqualTo(value)
    }
}