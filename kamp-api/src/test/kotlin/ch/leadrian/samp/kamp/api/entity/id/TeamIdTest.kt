package ch.leadrian.samp.kamp.api.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TeamIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 255, 256, Int.MAX_VALUE])
    fun shouldReturnTeamId(value: Int) {
        val teamId = TeamId.valueOf(value)

        assertThat(teamId.value)
                .isEqualTo(value)
    }
}