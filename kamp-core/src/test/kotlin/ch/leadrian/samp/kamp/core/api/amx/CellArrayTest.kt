package ch.leadrian.samp.kamp.core.api.amx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CellArrayTest {

    @Test
    fun shouldGetSizeOfImmutableCellArray() {
        val cellArray = ImmutableCellArray(1, 2, 3)

        val size = cellArray.size

        assertThat(size)
                .isEqualTo(3)
    }

    @Test
    fun shouldGetSizeOfMutableCellArray() {
        val cellArray = MutableCellArray(1, 2, 3)

        val size = cellArray.size

        assertThat(size)
                .isEqualTo(3)
    }

    @Test
    fun shouldGetSizeOfUninitializedMutableCellArray() {
        val cellArray = MutableCellArray(4)

        val size = cellArray.size

        assertThat(size)
                .isEqualTo(4)
    }

    @ParameterizedTest
    @CsvSource("0, 69", "1, 187", "2, 1337")
    fun shouldGetElementOfImmutableCellArray(index: Int, expectedValue: Int) {
        val cellArray = ImmutableCellArray(69, 187, 1337)

        val value = cellArray[index]

        assertThat(value)
                .isEqualTo(expectedValue)
    }

    @ParameterizedTest
    @CsvSource("0, 69", "1, 187", "2, 1337")
    fun shouldGetElementOfMutableCellArray(index: Int, expectedValue: Int) {
        val cellArray = MutableCellArray(69, 187, 1337)

        val value = cellArray[index]

        assertThat(value)
                .isEqualTo(expectedValue)
    }

    @Test
    fun shouldUpdateMutableCellArray() {
        val cellArray = MutableCellArray(69, 187, 1337)

        cellArray[1] = 1234

        assertThat(cellArray[1])
                .isEqualTo(1234)
    }

}