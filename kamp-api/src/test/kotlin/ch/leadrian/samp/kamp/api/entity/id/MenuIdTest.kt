package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MenuIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        SAMPConstants.MAX_MENUS - 1,
        SAMPConstants.MAX_MENUS,
        Int.MAX_VALUE,
        SAMPConstants.INVALID_MENU
    ]
    )
    fun shouldReturnMenuId(value: Int) {
        val menuId = MenuId.valueOf(value)

        assertThat(menuId.value)
                .isEqualTo(value)
    }

}