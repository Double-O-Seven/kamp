package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerMapObjectIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_OBJECTS - 1, SAMPConstants.MAX_OBJECTS, Int.MAX_VALUE, SAMPConstants.INVALID_OBJECT_ID])
    fun shouldReturnPlayerMapObjectId(value: Int) {
        val playerMapObjectId = PlayerMapObjectId.valueOf(value)

        Assertions.assertThat(playerMapObjectId.value)
                .isEqualTo(value)
    }
}