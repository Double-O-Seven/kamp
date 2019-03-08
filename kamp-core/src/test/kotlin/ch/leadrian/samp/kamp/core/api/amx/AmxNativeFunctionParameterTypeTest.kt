package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class AmxNativeFunctionParameterTypeTest {

    @Nested
    inner class IntTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseI() {
            val specifier = IntType.getSpecifier(1337)

            assertThat(specifier)
                    .isEqualTo("i")
        }

        @Test
        fun givenNonIntValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { IntType.tryToGetSpecifier("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldConvertToInt() {
            val intValue = IntType.convertToInt(1337)

            assertThat(intValue)
                    .isEqualTo(1337)
        }

        @Test
        fun givenNonIntValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { IntType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val primitiveValue = IntType.transformToPrimitive(1234)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1234))
        }

        @Test
        fun givenNonIntValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { IntType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetIntTypeByForIntClass() {
            val parameterType: AmxNativeFunctionParameterType<Int> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(IntType)
        }

    }

    @Nested
    inner class ImmutableIntCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseR() {
            val specifier = ImmutableIntCellType.getSpecifier(ImmutableIntCell())

            assertThat(specifier)
                    .isEqualTo("r")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { ImmutableIntCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val value = ImmutableIntCell(1337)
            val primitiveValue = ImmutableIntCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1337))
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetImmutableIntCellTypeByForImmutableIntCellClass() {
            val parameterType: AmxNativeFunctionParameterType<ImmutableIntCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(ImmutableIntCellType)
        }
    }

    @Nested
    inner class MutableIntCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseR() {
            val specifier = MutableIntCellType.getSpecifier(MutableIntCell())

            assertThat(specifier)
                    .isEqualTo("R")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { MutableIntCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val value = MutableIntCell(1337)
            val primitiveValue = MutableIntCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1337))
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetMutableIntCellTypeByForMutableIntCellClass() {
            val parameterType: AmxNativeFunctionParameterType<MutableIntCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(MutableIntCellType)
        }
    }

    @Nested
    inner class BooleanTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseB() {
            val specifier = BooleanType.getSpecifier(true)

            assertThat(specifier)
                    .isEqualTo("b")
        }

        @Test
        fun givenNonBooleanValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToGetSpecifier("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @ParameterizedTest
        @CsvSource("true, 1", "false, 0")
        fun shouldConvertToInt(booleanValue: Boolean, expectedIntValue: Int) {
            val intValue = BooleanType.convertToInt(booleanValue)

            assertThat(intValue)
                    .isEqualTo(expectedIntValue)
        }

        @Test
        fun givenNonBooleanValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @ParameterizedTest
        @CsvSource("true, 1", "false, 0")
        fun transformToPrimitiveShouldReturnSingleElementIntArray(booleanValue: Boolean, expectedIntValue: Int) {
            val primitiveValue = BooleanType.transformToPrimitive(booleanValue)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(expectedIntValue))
        }

        @Test
        fun givenNonBooleanValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetBooleanTypeByForBooleanClass() {
            val parameterType: AmxNativeFunctionParameterType<Boolean> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(BooleanType)
        }

    }

    @Nested
    inner class ImmutableBooleanCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseR() {
            val specifier = ImmutableBooleanCellType.getSpecifier(ImmutableBooleanCell())

            assertThat(specifier)
                    .isEqualTo("r")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { ImmutableBooleanCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @ParameterizedTest
        @CsvSource("true, 1", "false, 0")
        fun transformToPrimitiveShouldReturnSingleElementIntArray(booleanValue: Boolean, intValue: Int) {
            val value = ImmutableBooleanCell(booleanValue)
            val primitiveValue = ImmutableBooleanCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(intValue))
        }

        @Test
        fun givenNonBooleanValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonBooleanValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetImmutableBooleanCellTypeByForImmutableBooleanCellClass() {
            val parameterType: AmxNativeFunctionParameterType<ImmutableBooleanCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(ImmutableBooleanCellType)
        }
    }

    @Nested
    inner class MutableBooleanCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseR() {
            val specifier = MutableBooleanCellType.getSpecifier(MutableBooleanCell())

            assertThat(specifier)
                    .isEqualTo("R")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { MutableBooleanCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @ParameterizedTest
        @CsvSource("true, 1", "false, 0")
        fun transformToPrimitiveShouldReturnSingleElementIntArray(booleanValue: Boolean, intValue: Int) {
            val value = MutableBooleanCell(booleanValue)
            val primitiveValue = MutableBooleanCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(intValue))
        }

        @Test
        fun givenNonBooleanValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonBooleanValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { BooleanType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetMutableBooleanCellTypeByForMutableBooleanCellClass() {
            val parameterType: AmxNativeFunctionParameterType<MutableBooleanCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(MutableBooleanCellType)
        }
    }

    @Nested
    inner class FloatTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseF() {
            val specifier = FloatType.getSpecifier(1337f)

            assertThat(specifier)
                    .isEqualTo("f")
        }

        @Test
        fun givenNonFloatValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToGetSpecifier("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldConvertToInt() {
            val intValue = FloatType.convertToInt(1337f)

            assertThat(intValue)
                    .isEqualTo(1337f.toRawBits())
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val primitiveValue = FloatType.transformToPrimitive(1234f)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1234f.toRawBits()))
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetFloatTypeByForFloatClass() {
            val parameterType: AmxNativeFunctionParameterType<Float> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(FloatType)
        }

    }

    @Nested
    inner class ImmutableFloatCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseR() {
            val specifier = ImmutableFloatCellType.getSpecifier(ImmutableFloatCell())

            assertThat(specifier)
                    .isEqualTo("r")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { ImmutableFloatCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val value = ImmutableFloatCell(1337f)
            val primitiveValue = ImmutableFloatCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1337f.toRawBits()))
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetImmutableFloatCellTypeByForImmutableFloatCellClass() {
            val parameterType: AmxNativeFunctionParameterType<ImmutableFloatCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(ImmutableFloatCellType)
        }
    }

    @Nested
    inner class MutableFloatCellTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseR() {
            val specifier = MutableFloatCellType.getSpecifier(MutableFloatCell())

            assertThat(specifier)
                    .isEqualTo("R")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { MutableFloatCellType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnSingleElementIntArray() {
            val value = MutableFloatCell(1337f)
            val primitiveValue = MutableFloatCellType.transformToPrimitive(value)

            assertThat(primitiveValue)
                    .isEqualTo(intArrayOf(1337f.toRawBits()))
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetMutableFloatCellTypeByForMutableFloatCellClass() {
            val parameterType: AmxNativeFunctionParameterType<MutableFloatCell> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(MutableFloatCellType)
        }
    }

    @Nested
    inner class StringTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseS() {
            val specifier = StringType.getSpecifier("Test")

            assertThat(specifier)
                    .isEqualTo("s")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { StringType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnBytes() {
            val primitiveValue = StringType.transformToPrimitive("Test")

            assertThat(primitiveValue)
                    .isEqualTo("Test".toByteArray(StringEncoding.getCharset()).copyOf(5))
        }

        @Test
        fun givenNonFloatValueTryToConvertToIntShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToConvertToInt("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun givenNonFloatValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { FloatType.tryToTransformToPrimitive("Hi there") }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetStringTypeByForStringClass() {
            val parameterType: AmxNativeFunctionParameterType<String> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(StringType)
        }
    }

    @Nested
    inner class OutputStringTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseSWithStringSize() {
            val specifier = OutputStringType.getSpecifier(OutputString("Test"))

            assertThat(specifier)
                    .isEqualTo("S[5]")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { OutputStringType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnBytes() {
            val primitiveValue = OutputStringType.transformToPrimitive(OutputString("Test"))

            assertThat(primitiveValue)
                    .isEqualTo("Test".toByteArray(StringEncoding.getCharset()).copyOf(5))
        }

        @Test
        fun givenNonByteArrayValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { OutputStringType.tryToTransformToPrimitive(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetOutputStringTypeByForOutputStringClass() {
            val parameterType: AmxNativeFunctionParameterType<OutputString> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(OutputStringType)
        }
    }

    @Nested
    inner class ImmutableCellArrayTypeTests {

        @Test
        fun shouldHaveFormatSpecifierLowerCaseAWithSize() {
            val specifier = ImmutableCellArrayType.getSpecifier(ImmutableCellArray(intArrayOf(69, 1337, 1234, 999)))

            assertThat(specifier)
                    .isEqualTo("a[4]")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { ImmutableCellArrayType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnIntArray() {
            val primitiveValue = ImmutableCellArrayType.transformToPrimitive(ImmutableCellArray(intArrayOf(1, 2, 3)))

            assertThat(primitiveValue)
                    .isInstanceOfSatisfying(IntArray::class.java) {
                        assertThat(it).containsExactly(1, 2, 3)
                    }
        }

        @Test
        fun givenNonByteArrayValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { ImmutableCellArrayType.tryToTransformToPrimitive(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetImmutableCellArrayTypeByForImmutableCellArrayClass() {
            val parameterType: AmxNativeFunctionParameterType<ImmutableCellArray> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(ImmutableCellArrayType)
        }
    }

    @Nested
    inner class MutableCellArrayTypeTests {

        @Test
        fun shouldHaveFormatSpecifierUpperCaseAWithArraySize() {
            val specifier = MutableCellArrayType.getSpecifier(MutableCellArray(intArrayOf(69, 1337, 1234)))

            assertThat(specifier)
                    .isEqualTo("A[3]")
        }

        @Test
        fun givenNonByteArrayValueTryToGetSpecifierShouldThrowException() {
            val caughtThrowable = catchThrowable { MutableCellArrayType.tryToGetSpecifier(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun transformToPrimitiveShouldReturnIntArray() {
            val primitiveValue = MutableCellArrayType.transformToPrimitive(MutableCellArray(intArrayOf(1, 2, 3)))

            assertThat(primitiveValue)
                    .isInstanceOfSatisfying(IntArray::class.java) {
                        assertThat(it).containsExactly(1, 2, 3)
                    }
        }

        @Test
        fun givenNonByteArrayValueTryToTransformToPrimitiveShouldThrowException() {
            val caughtThrowable = catchThrowable { MutableCellArrayType.tryToTransformToPrimitive(1337f) }

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException::class.java)
        }

        @Test
        fun shouldGetMutableCellArrayTypeByForMutableCellArrayClass() {
            val parameterType: AmxNativeFunctionParameterType<MutableCellArray> = AmxNativeFunctionParameterType.get()

            assertThat(parameterType)
                    .isEqualTo(MutableCellArrayType)
        }
    }

}