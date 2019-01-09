package ch.leadrian.samp.kamp.core.api.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DialogIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        255,
        256,
        Int.MAX_VALUE
    ])
    fun shouldReturnDialogId(value: Int) {
        val dialogId = DialogId.valueOf(value)

        assertThat(dialogId.value)
                .isEqualTo(value)
    }

}