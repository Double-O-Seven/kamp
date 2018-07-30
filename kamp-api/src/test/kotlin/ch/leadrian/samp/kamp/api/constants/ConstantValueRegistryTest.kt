package ch.leadrian.samp.kamp.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class ConstantValueRegistryTest {

    @ParameterizedTest
    @EnumSource(TestConstantValue::class)
    fun shouldReturnConstantByValue(expectedConstant: TestConstantValue) {
        val constantValueRegistry = ConstantValueRegistry(*TestConstantValue.values())

        val constant = constantValueRegistry[expectedConstant.value]

        assertThat(constant)
                .isEqualTo(expectedConstant)
    }

    @Test
    fun givenInvalidValueItShouldThrowAnException() {
        val constantValueRegistry = ConstantValueRegistry(*TestConstantValue.values())

        val caughtThrowable = catchThrowable { constantValueRegistry[-999] }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    enum class TestConstantValue(override val value: Int) : ConstantValue<Int> {
        ABC(123),
        DEF(456),
        GHI(789)
    }

}