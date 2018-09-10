package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TeamIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 255, 256, Int.MAX_VALUE, SAMPConstants.NO_TEAM])
    fun shouldReturnTeamId(value: Int) {
        val teamId = TeamId.valueOf(value)

        assertThat(teamId.value)
                .isEqualTo(value)
    }
}