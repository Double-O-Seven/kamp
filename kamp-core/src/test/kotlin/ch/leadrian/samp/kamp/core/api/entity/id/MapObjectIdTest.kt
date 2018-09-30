package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MapObjectIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_OBJECTS - 1, SAMPConstants.MAX_OBJECTS, Int.MAX_VALUE, SAMPConstants.INVALID_OBJECT_ID])
    fun shouldReturnMapObjectId(value: Int) {
        val mapObjectId = MapObjectId.valueOf(value)

        assertThat(mapObjectId.value)
                .isEqualTo(value)
    }
}