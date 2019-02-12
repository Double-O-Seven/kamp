package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
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
                { assertThat(outputString.bytes).isEqualTo("Test".toByteArray(StringEncoding.getCharset())) },
                { assertThat(outputString.size).isEqualTo(4) }
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

}