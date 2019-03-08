package ch.leadrian.samp.kamp.core.api.amx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldGetValueFromImmutableBooleanCell(expectedValue: Boolean) {
        val cell = ImmutableBooleanCell(expectedValue)

        val value = cell.value

        assertThat(value)
                .isEqualTo(expectedValue)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldGetValueFromMutableBooleanCell(expectedValue: Boolean) {
        val cell = MutableBooleanCell(expectedValue)

        val value = cell.value

        assertThat(value)
                .isEqualTo(expectedValue)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldUpdateMutableBooleanCell(expectedValue: Boolean) {
        val cell = MutableBooleanCell(!expectedValue)

        cell.value = expectedValue

        assertThat(cell.value)
                .isEqualTo(expectedValue)
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