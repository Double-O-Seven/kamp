package ch.leadrian.samp.kamp.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleDoorDamageStatusValueTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorDamageStatusValueArgumentsProvider::class)
    fun shouldReturnExpectedValues(
            inputValue: Int,
            isOpened: Boolean,
            isDamaged: Boolean,
            isRemoved: Boolean
    ) {
        val vehicleDoorDamageStatusValue = VehicleDoorDamageStatusValue(inputValue)

        assertThat(vehicleDoorDamageStatusValue)
                .satisfies {
                    assertThat(it.isOpened)
                            .isEqualTo(isOpened)
                    assertThat(it.isDamaged)
                            .isEqualTo(isDamaged)
                    assertThat(it.isRemoved)
                            .isEqualTo(isRemoved)
                }
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorDamageStatusValueArgumentsProvider::class)
    fun shouldSetValues(
            expectedValue: Int,
            isOpened: Boolean,
            isDamaged: Boolean,
            isRemoved: Boolean
    ) {
        val vehicleDoorDamageStatusValue = VehicleDoorDamageStatusValue(0)

        vehicleDoorDamageStatusValue.apply {
            this.isOpened = isOpened
            this.isDamaged = isDamaged
            this.isRemoved = isRemoved
        }

        assertThat(vehicleDoorDamageStatusValue.data)
                .isEqualTo(expectedValue)
    }

    private class VehicleDoorDamageStatusValueArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = Stream.of(
                VehicleDoorDamageStatusValueArguments(value = 0b000),
                VehicleDoorDamageStatusValueArguments(value = 0b001, isOpened = true),
                VehicleDoorDamageStatusValueArguments(value = 0b010, isDamaged = true),
                VehicleDoorDamageStatusValueArguments(value = 0b011, isOpened = true, isDamaged = true),
                VehicleDoorDamageStatusValueArguments(value = 0b100, isRemoved = true),
                VehicleDoorDamageStatusValueArguments(value = 0b101, isRemoved = true, isOpened = true),
                VehicleDoorDamageStatusValueArguments(value = 0b110, isRemoved = true, isDamaged = true),
                VehicleDoorDamageStatusValueArguments(value = 0b111, isRemoved = true, isOpened = true, isDamaged = true)
        )
    }

    private class VehicleDoorDamageStatusValueArguments(
            val value: Int,
            val isOpened: Boolean = false,
            val isDamaged: Boolean = false,
            val isRemoved: Boolean = false
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(value, isOpened, isDamaged, isRemoved)

    }

}