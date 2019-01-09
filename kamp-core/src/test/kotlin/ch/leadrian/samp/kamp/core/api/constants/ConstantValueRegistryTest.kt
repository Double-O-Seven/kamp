package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class ConstantValueRegistryTest {

    @Nested
    inner class GetTests {

        @ParameterizedTest
        @ArgumentsSource(TestConstantValueArgumentsProvider::class)
        fun shouldReturnConstantByValue(expectedConstant: TestConstantValue) {
            val constantValueRegistry = ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry(*TestConstantValue.values())

            val constant = constantValueRegistry[expectedConstant.value]

            assertThat(constant)
                    .isEqualTo(expectedConstant)
        }

        @Test
        fun givenInvalidValueAndDefaultValueItShouldReturnDefaultValue() {
            val constantValueRegistry = ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry(
                    TestConstantValue.FirstValue,
                    TestConstantValue.SecondValue,
                    TestConstantValue.ThirdValue,
                    defaultValue = TestConstantValue.DefaultValue
            )

            val constant = constantValueRegistry[TestConstantValue.DefaultValue.value]

            assertThat(constant)
                    .isEqualTo(TestConstantValue.DefaultValue)
        }

        @Test
        fun givenInvalidValueItShouldThrowAnException() {
            val constantValueRegistry = ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry(
                    TestConstantValue.FirstValue,
                    TestConstantValue.SecondValue,
                    TestConstantValue.ThirdValue
            )

            val caughtThrowable = catchThrowable { constantValueRegistry[TestConstantValue.DefaultValue.value] }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class ExistsTests {

        @ParameterizedTest
        @ArgumentsSource(TestConstantValueArgumentsProvider::class)
        fun givenValueExistsItShouldReturnTrue(expectedConstant: TestConstantValue) {
            val constantValueRegistry = ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry(*TestConstantValue.values())

            val exists = constantValueRegistry.exists(expectedConstant.value)

            assertThat(exists)
                    .isTrue()
        }

        @Test
        fun givenValueDoesNotExistItShouldReturnFalse() {
            val constantValueRegistry = ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry(*TestConstantValue.values())

            val exists = constantValueRegistry.exists(-999)

            assertThat(exists)
                    .isFalse()
        }
    }

    sealed class TestConstantValue(override val value: Int) :
            ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {

        companion object {

            fun values(): Array<TestConstantValue> = arrayOf(FirstValue, SecondValue, ThirdValue, DefaultValue)
        }

        object FirstValue : TestConstantValue(1)

        object SecondValue : TestConstantValue(2)

        object ThirdValue : TestConstantValue(3)

        object DefaultValue : TestConstantValue(4)
    }

    private class TestConstantValueArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(*TestConstantValue.values()).map { Arguments.of(it) }

    }

}