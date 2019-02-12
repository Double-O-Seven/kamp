package ch.leadrian.samp.kamp.core.api.amx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CellTest {

    @Test
    fun shouldGetValueFromImmutableIntCell() {
        val cell = ImmutableIntCell(1337)

        val value = cell.value

        assertThat(value)
                .isEqualTo(1337)
    }

    @Test
    fun shouldGetValueFromMutableIntCell() {
        val cell = MutableIntCell(1337)

        val value = cell.value

        assertThat(value)
                .isEqualTo(1337)
    }

    @Test
    fun shouldUpdateMutableIntCell() {
        val cell = MutableIntCell(1337)

        cell.value = 69

        assertThat(cell.value)
                .isEqualTo(69)
    }

    @Test
    fun shouldGetValueFromImmutableFloatCell() {
        val cell = ImmutableFloatCell(1337f)

        val value = cell.value

        assertThat(value)
                .isEqualTo(1337f)
    }

    @Test
    fun shouldGetValueFromMutableFloatCell() {
        val cell = MutableFloatCell(1337f)

        val value = cell.value

        assertThat(value)
                .isEqualTo(1337f)
    }

    @Test
    fun shouldUpdateMutableFloatCell() {
        val cell = MutableFloatCell(1337f)

        cell.value = 69f

        assertThat(cell.value)
                .isEqualTo(69f)
    }

}