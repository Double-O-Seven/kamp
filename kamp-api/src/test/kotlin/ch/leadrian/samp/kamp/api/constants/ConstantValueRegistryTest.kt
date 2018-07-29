package ch.leadrian.samp.kamp.api.constants

import org.assertj.core.api.Assertions.assertThat
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

    enum class TestConstantValue(override val value: Int) : ConstantValue<Int> {
        ABC(123),
        DEF(456),
        GHI(789)
    }

}