package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerTextLabelIdTest {

    @ParameterizedTest
    @ValueSource(
            ints = [
                -1,
                0,
                SAMPConstants.MAX_3DTEXT_PLAYER - 1,
                SAMPConstants.MAX_3DTEXT_PLAYER,
                Int.MAX_VALUE,
                SAMPConstants.INVALID_3DTEXT_ID
            ]
    )
    fun shouldReturnTextLabelId(value: Int) {
        val playerTextLabelId = PlayerTextLabelId.valueOf(value)

        assertThat(playerTextLabelId.value)
                .isEqualTo(value)
    }

}