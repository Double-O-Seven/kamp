package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import ch.leadrian.samp.kamp.core.runtime.amx.nullTerminated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class OutputStringTest {

    @Test
    fun shouldInitializeWithByteArray() {
        val outputString = OutputString(byteArrayOf(1, 2, 69))

        assertAll(
                { assertThat(outputString.bytes).containsExactly(1, 2, 69) },
                { assertThat(outputString.size).isEqualTo(3) }
        )
    }

    @Test
    fun shouldInitializeWithString() {
        val outputString = OutputString("Test")

        assertAll(
                { assertThat(outputString.bytes).isEqualTo("Test".toByteArray(StringEncoding.getCharset()).copyOf(5)) },
                { assertThat(outputString.size).isEqualTo(5) },
                { assertThat(outputString.value).isEqualTo("Test") }
        )
    }

    @Test
    fun shouldInitializeWithEmptyArray() {
        val outputString = OutputString(size = 4)

        assertAll(
                { assertThat(outputString.bytes).hasSize(4).containsOnly(0) },
                { assertThat(outputString.size).isEqualTo(4) }
        )
    }

    @Test
    fun shouldSetValueToBytesOfStringShorterThanCapacity() {
        val outputString = OutputString(byteArrayOf(1, 2, 3, 4, 5, 6))

        outputString.value = "Test"

        assertThat(outputString.bytes)
                .isEqualTo("Test".toByteArray(StringEncoding.getCharset()).copyOf(6))
    }

    @Test
    fun shouldSetValueToBytesOfStringLongerThanCapacity() {
        val outputString = OutputString(byteArrayOf(1, 2, 3))

        outputString.value = "Foobar"

        assertThat(outputString.bytes)
                .isEqualTo("Foobar".toByteArray(StringEncoding.getCharset()).copyOf(2).nullTerminated())
    }

}