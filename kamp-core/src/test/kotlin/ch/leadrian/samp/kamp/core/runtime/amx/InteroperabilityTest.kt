package ch.leadrian.samp.kamp.core.runtime.amx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class InteroperabilityTest {

    @Test
    fun shouldNullTerminateByteArray() {
        val nullTerminated = byteArrayOf(1, 2, 3).nullTerminated()

        assertThat(nullTerminated)
                .containsExactly(1, 2, 3, 0)
    }

    @ParameterizedTest
    @ArgumentsSource(RemoveTrailingZeroesArgumentsProvider::class)
    fun shouldReturnTrainingZeroes(inputArray: ByteArray, expectedResult: ByteArray) {
        val result = inputArray.removeTrailingZeroes()

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    private class RemoveTrailingZeroesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(byteArrayOf(1, 2, 3, 0, 0), byteArrayOf(1, 2, 3)),
                        Arguments.of(byteArrayOf(1, 2, 3), byteArrayOf(1, 2, 3)),
                        Arguments.of(byteArrayOf(0, 2, 3), byteArrayOf()),
                        Arguments.of(byteArrayOf(), byteArrayOf())
                )

    }

}