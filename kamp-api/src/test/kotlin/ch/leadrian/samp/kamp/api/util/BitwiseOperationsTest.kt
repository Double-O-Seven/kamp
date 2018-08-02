package ch.leadrian.samp.kamp.api.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class BitwiseOperationsTest {

    @ParameterizedTest
    @ArgumentsSource(GetByteArgumentsProvider::class)
    fun shouldGetByte(value: Int, position: Int, expectedResultValue: Int) {
        val resultValue = value.getByte(position)

        assertThat(resultValue)
                .isEqualTo(expectedResultValue)
    }

    @ParameterizedTest
    @ArgumentsSource(SetByteArgumentsProvider::class)
    fun shouldSetByte(value: Int, position: Int, byteValue: Int, expectedResultValue: Int) {
        val resultValue = value.setByte(position, byteValue)

        assertThat(resultValue)
                .isEqualTo(expectedResultValue)
    }

    @ParameterizedTest
    @ArgumentsSource(GetBitArgumentsProvider::class)
    fun shouldGetBit(value: Int, position: Int, expectedResultValue: Int) {
        val resultValue = value.getBit(position)

        assertThat(resultValue)
                .isEqualTo(expectedResultValue)
    }

    @ParameterizedTest
    @ArgumentsSource(SetBitArgumentsProvider::class)
    fun shouldSetBit(value: Int, position: Int, bitValue: Int, expectedResultValue: Int) {
        val resultValue = value.setBit(position, bitValue)

        assertThat(resultValue)
                .isEqualTo(expectedResultValue)
    }

    private class GetByteArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(0x12345678, 0, 0x8),
                        Arguments.of(0x12345678, 1, 0x7),
                        Arguments.of(0x12345678, 2, 0x6),
                        Arguments.of(0x12345678, 3, 0x5),
                        Arguments.of(0x12345678, 4, 0x4),
                        Arguments.of(0x12345678, 5, 0x3),
                        Arguments.of(0x12345678, 6, 0x2),
                        Arguments.of(0x12345678, 7, 0x1),
                        Arguments.of(0x12345678, 0, 0x8),
                        Arguments.of(0x12345678, 1, 0x7),
                        Arguments.of(0x12345678, 2, 0x6),
                        Arguments.of(0x12345678, 3, 0x5),
                        Arguments.of(0x12345678, 4, 0x4),
                        Arguments.of(0x12345678, 5, 0x3),
                        Arguments.of(0x12345678, 6, 0x2),
                        Arguments.of(0x12345678, 7, 0x1)
                )

    }

    private class SetByteArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(0x4FFFFFFF, 0, 0x3, 0x4FFFFFF3),
                        Arguments.of(0x4FFFFFFF, 1, 0x3, 0x4FFFFF3F),
                        Arguments.of(0x4FFFFFFF, 2, 0x3, 0x4FFFF3FF),
                        Arguments.of(0x4FFFFFFF, 3, 0x3, 0x4FFF3FFF),
                        Arguments.of(0x4FFFFFFF, 4, 0x3, 0x4FF3FFFF),
                        Arguments.of(0x4FFFFFFF, 5, 0x3, 0x4F3FFFFF),
                        Arguments.of(0x4FFFFFFF, 6, 0x3, 0x43FFFFFF),
                        Arguments.of(0x4FFFFFFF, 7, 0x3, 0x3FFFFFFF),
                        Arguments.of(0x4FFFFFFF, 0, 0x73, 0x4FFFFFF3),
                        Arguments.of(0x4FFFFFFF, 1, 0x73, 0x4FFFFF3F),
                        Arguments.of(0x4FFFFFFF, 2, 0x73, 0x4FFFF3FF),
                        Arguments.of(0x4FFFFFFF, 3, 0x73, 0x4FFF3FFF),
                        Arguments.of(0x4FFFFFFF, 4, 0x73, 0x4FF3FFFF),
                        Arguments.of(0x4FFFFFFF, 5, 0x73, 0x4F3FFFFF),
                        Arguments.of(0x4FFFFFFF, 6, 0x73, 0x43FFFFFF),
                        Arguments.of(0x4FFFFFFF, 7, 0x73, 0x3FFFFFFF)
                )

    }

    private class GetBitArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(0b00000001, 0, 1),
                        Arguments.of(0b00000010, 1, 1),
                        Arguments.of(0b00000100, 2, 1),
                        Arguments.of(0b00001000, 3, 1),
                        Arguments.of(0b00010000, 4, 1),
                        Arguments.of(0b00100000, 5, 1),
                        Arguments.of(0b01000000, 6, 1),
                        Arguments.of(0b10000000, 7, 1),
                        Arguments.of(0b0000000100000000, 8, 1),
                        Arguments.of(0b0000001000000000, 9, 1),
                        Arguments.of(0b0000010000000000, 10, 1),
                        Arguments.of(0b0000100000000000, 11, 1),
                        Arguments.of(0b0001000000000000, 12, 1),
                        Arguments.of(0b0010000000000000, 13, 1),
                        Arguments.of(0b0100000000000000, 14, 1),
                        Arguments.of(0b1000000000000000, 15, 1),
                        Arguments.of(0b000000010000000000000000, 16, 1),
                        Arguments.of(0b000000100000000000000000, 17, 1),
                        Arguments.of(0b000001000000000000000000, 18, 1),
                        Arguments.of(0b000010000000000000000000, 19, 1),
                        Arguments.of(0b000100000000000000000000, 20, 1),
                        Arguments.of(0b001000000000000000000000, 21, 1),
                        Arguments.of(0b010000000000000000000000, 22, 1),
                        Arguments.of(0b100000000000000000000000, 23, 1),
                        Arguments.of(0b00000001000000000000000000000000, 24, 1),
                        Arguments.of(0b00000010000000000000000000000000, 25, 1),
                        Arguments.of(0b00000100000000000000000000000000, 26, 1),
                        Arguments.of(0b00001000000000000000000000000000, 27, 1),
                        Arguments.of(0b00010000000000000000000000000000, 28, 1),
                        Arguments.of(0b00100000000000000000000000000000, 29, 1),
                        Arguments.of(0b01000000000000000000000000000000, 30, 1),
                        Arguments.of(0b10000000000000000000000000000000.toInt(), 31, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 0, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 1, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 2, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 3, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 4, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 5, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 6, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 7, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 8, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 9, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 10, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 11, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 12, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 13, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 14, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 15, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 16, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 17, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 18, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 19, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 20, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 21, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 22, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 23, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 24, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 25, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 26, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 27, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 28, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 29, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 30, 1),
                        Arguments.of(0xFFFFFFFF.toInt(), 31, 1),
                        Arguments.of(0b11111111111111111111111111111110.toInt(), 0, 0),
                        Arguments.of(0b11111111111111111111111111111101.toInt(), 1, 0),
                        Arguments.of(0b11111111111111111111111111111011.toInt(), 2, 0),
                        Arguments.of(0b11111111111111111111111111110111.toInt(), 3, 0),
                        Arguments.of(0b11111111111111111111111111101111.toInt(), 4, 0),
                        Arguments.of(0b11111111111111111111111111011111.toInt(), 5, 0),
                        Arguments.of(0b11111111111111111111111110111111.toInt(), 6, 0),
                        Arguments.of(0b11111111111111111111111101111111.toInt(), 7, 0),
                        Arguments.of(0b11111111111111111111111011111111.toInt(), 8, 0),
                        Arguments.of(0b11111111111111111111110111111111.toInt(), 9, 0),
                        Arguments.of(0b11111111111111111111101111111111.toInt(), 10, 0),
                        Arguments.of(0b11111111111111111111011111111111.toInt(), 11, 0),
                        Arguments.of(0b11111111111111111110111111111111.toInt(), 12, 0),
                        Arguments.of(0b11111111111111111101111111111111.toInt(), 13, 0),
                        Arguments.of(0b11111111111111111011111111111111.toInt(), 14, 0),
                        Arguments.of(0b11111111111111110111111111111111.toInt(), 15, 0),
                        Arguments.of(0b11111111111111101111111111111111.toInt(), 16, 0),
                        Arguments.of(0b11111111111111011111111111111111.toInt(), 17, 0),
                        Arguments.of(0b11111111111110111111111111111111.toInt(), 18, 0),
                        Arguments.of(0b11111111111101111111111111111111.toInt(), 19, 0),
                        Arguments.of(0b11111111111011111111111111111111.toInt(), 20, 0),
                        Arguments.of(0b11111111110111111111111111111111.toInt(), 21, 0),
                        Arguments.of(0b11111111101111111111111111111111.toInt(), 22, 0),
                        Arguments.of(0b11111111011111111111111111111111.toInt(), 23, 0),
                        Arguments.of(0b11111110111111111111111111111111.toInt(), 24, 0),
                        Arguments.of(0b11111101111111111111111111111111.toInt(), 25, 0),
                        Arguments.of(0b11111011111111111111111111111111.toInt(), 26, 0),
                        Arguments.of(0b11110111111111111111111111111111.toInt(), 27, 0),
                        Arguments.of(0b11101111111111111111111111111111.toInt(), 28, 0),
                        Arguments.of(0b11011111111111111111111111111111.toInt(), 29, 0),
                        Arguments.of(0b10111111111111111111111111111111.toInt(), 30, 0),
                        Arguments.of(0b01111111111111111111111111111111, 31, 0)
                )

    }

    private class SetBitArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(0, 0, 1, 0b00000000000000000000000000000001),
                        Arguments.of(0, 1, 1, 0b00000000000000000000000000000010),
                        Arguments.of(0, 2, 1, 0b00000000000000000000000000000100),
                        Arguments.of(0, 3, 1, 0b00000000000000000000000000001000),
                        Arguments.of(0, 4, 1, 0b00000000000000000000000000010000),
                        Arguments.of(0, 5, 1, 0b00000000000000000000000000100000),
                        Arguments.of(0, 6, 1, 0b00000000000000000000000001000000),
                        Arguments.of(0, 7, 1, 0b00000000000000000000000010000000),
                        Arguments.of(0, 8, 1, 0b00000000000000000000000100000000),
                        Arguments.of(0, 9, 1, 0b00000000000000000000001000000000),
                        Arguments.of(0, 10, 1, 0b00000000000000000000010000000000),
                        Arguments.of(0, 11, 1, 0b00000000000000000000100000000000),
                        Arguments.of(0, 12, 1, 0b00000000000000000001000000000000),
                        Arguments.of(0, 13, 1, 0b00000000000000000010000000000000),
                        Arguments.of(0, 14, 1, 0b00000000000000000100000000000000),
                        Arguments.of(0, 15, 1, 0b00000000000000001000000000000000),
                        Arguments.of(0, 16, 1, 0b00000000000000010000000000000000),
                        Arguments.of(0, 17, 1, 0b00000000000000100000000000000000),
                        Arguments.of(0, 18, 1, 0b00000000000001000000000000000000),
                        Arguments.of(0, 19, 1, 0b00000000000010000000000000000000),
                        Arguments.of(0, 20, 1, 0b00000000000100000000000000000000),
                        Arguments.of(0, 21, 1, 0b00000000001000000000000000000000),
                        Arguments.of(0, 22, 1, 0b00000000010000000000000000000000),
                        Arguments.of(0, 23, 1, 0b00000000100000000000000000000000),
                        Arguments.of(0, 24, 1, 0b00000001000000000000000000000000),
                        Arguments.of(0, 25, 1, 0b00000010000000000000000000000000),
                        Arguments.of(0, 26, 1, 0b00000100000000000000000000000000),
                        Arguments.of(0, 27, 1, 0b00001000000000000000000000000000),
                        Arguments.of(0, 28, 1, 0b00010000000000000000000000000000),
                        Arguments.of(0, 29, 1, 0b00100000000000000000000000000000),
                        Arguments.of(0, 30, 1, 0b01000000000000000000000000000000),
                        Arguments.of(0, 31, 1, 0b10000000000000000000000000000000.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 0, 0, 0b11111111111111111111111111111110.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 1, 0, 0b11111111111111111111111111111101.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 2, 0, 0b11111111111111111111111111111011.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 3, 0, 0b11111111111111111111111111110111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 4, 0, 0b11111111111111111111111111101111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 5, 0, 0b11111111111111111111111111011111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 6, 0, 0b11111111111111111111111110111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 7, 0, 0b11111111111111111111111101111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 8, 0, 0b11111111111111111111111011111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 9, 0, 0b11111111111111111111110111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 10, 0, 0b11111111111111111111101111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 11, 0, 0b11111111111111111111011111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 12, 0, 0b11111111111111111110111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 13, 0, 0b11111111111111111101111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 14, 0, 0b11111111111111111011111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 15, 0, 0b11111111111111110111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 16, 0, 0b11111111111111101111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 17, 0, 0b11111111111111011111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 18, 0, 0b11111111111110111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 19, 0, 0b11111111111101111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 20, 0, 0b11111111111011111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 21, 0, 0b11111111110111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 22, 0, 0b11111111101111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 23, 0, 0b11111111011111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 24, 0, 0b11111110111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 25, 0, 0b11111101111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 26, 0, 0b11111011111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 27, 0, 0b11110111111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 28, 0, 0b11101111111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 29, 0, 0b11011111111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 30, 0, 0b10111111111111111111111111111111.toInt()),
                        Arguments.of(0xFFFFFFFF.toInt(), 31, 0, 0b01111111111111111111111111111111)
                )
    }

}