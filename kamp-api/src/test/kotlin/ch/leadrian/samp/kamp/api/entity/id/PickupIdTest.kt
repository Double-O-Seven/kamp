package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PickupIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        SAMPConstants.MAX_PICKUPS - 1,
        SAMPConstants.MAX_PICKUPS,
        Int.MAX_VALUE
    ]
    )
    fun shouldReturnPickupId(value: Int) {
        val pickupId = PickupId.valueOf(value)

        assertThat(pickupId.value)
                .isEqualTo(value)
    }

}