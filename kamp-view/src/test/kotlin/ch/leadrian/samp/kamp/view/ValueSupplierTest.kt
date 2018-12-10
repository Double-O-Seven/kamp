package ch.leadrian.samp.kamp.view

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ValueSupplierTest {

    @Test
    fun shouldReturnInitialValue() {
        val valueSupplier = ValueSupplier("Hi there")

        val value = valueSupplier.getValue(Any(), mockk())

        assertThat(value)
                .isEqualTo("Hi there")
    }

    @Test
    fun shouldSetValue() {
        val valueSupplier = ValueSupplier("Hi there")

        valueSupplier.setValue(Any(), mockk(), "Hello")

        assertThat(valueSupplier.getValue(Any(), mockk()))
                .isEqualTo("Hello")
    }

    @Test
    fun shouldSetSuppliedValue() {
        val valueSupplier = ValueSupplier("Hi there")

        valueSupplier.value { "Hello" }

        assertThat(valueSupplier.getValue(Any(), mockk()))
                .isEqualTo("Hello")
    }

}