package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GangZoneIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        SAMPConstants.MAX_GANG_ZONES - 1,
        SAMPConstants.MAX_GANG_ZONES,
        Int.MAX_VALUE,
        SAMPConstants.INVALID_GANG_ZONE
    ])
    fun shouldReturnGangZoneId(value: Int) {
        val gangZoneId = GangZoneId.valueOf(value)

        assertThat(gangZoneId.value)
                .isEqualTo(value)
    }

}