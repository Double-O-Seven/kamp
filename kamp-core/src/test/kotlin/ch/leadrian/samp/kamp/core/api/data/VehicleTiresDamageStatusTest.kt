package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleTiresDamageStatusTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleTiresDamageStatusArgumentsProvider::class)
    fun shouldReturnExpectedValues(
            inputValue: Int,
            frontLeftTire: Boolean,
            backLeftTire: Boolean,
            frontRightTire: Boolean,
            backRightTire: Boolean
    ) {
        val vehicleTiresDamageStatus = VehicleTiresDamageStatus(inputValue)

        assertThat(vehicleTiresDamageStatus)
                .satisfies {
                    assertThat(it.isFrontLeftTireDamaged)
                            .isEqualTo(frontLeftTire)
                    assertThat(it.isBackLeftTireDamaged)
                            .isEqualTo(backLeftTire)
                    assertThat(it.isFrontRightTireDamaged)
                            .isEqualTo(frontRightTire)
                    assertThat(it.isBackRightTireDamaged)
                            .isEqualTo(backRightTire)
                }
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleTiresDamageStatusArgumentsProvider::class)
    fun shouldSetValues(
            expectedValue: Int,
            frontLeftTire: Boolean,
            backLeftTire: Boolean,
            frontRightTire: Boolean,
            backRightTire: Boolean
    ) {
        val vehicleTiresDamageStatus = VehicleTiresDamageStatus(0)

        vehicleTiresDamageStatus.apply {
            this.isFrontLeftTireDamaged = frontLeftTire
            this.isBackLeftTireDamaged = backLeftTire
            this.isFrontRightTireDamaged = frontRightTire
            this.isBackRightTireDamaged = backRightTire
        }

        assertThat(vehicleTiresDamageStatus.value)
                .isEqualTo(expectedValue)
    }

    private class VehicleTiresDamageStatusArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<VehicleTiresDamageStatusArguments> =
                Stream.of(
                        VehicleTiresDamageStatusArguments(value = 0b0000),
                        VehicleTiresDamageStatusArguments(value = 0b0001, backRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0010, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0011, backRightTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0100, backLeftTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0101, backRightTire = true, backLeftTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0110, backLeftTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b0111, backRightTire = true, backLeftTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1000, frontLeftTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1001, frontLeftTire = true, backRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1010, frontLeftTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1011, frontLeftTire = true, backRightTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1100, frontLeftTire = true, backLeftTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1101, frontLeftTire = true, backRightTire = true, backLeftTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1110, frontLeftTire = true, backLeftTire = true, frontRightTire = true),
                        VehicleTiresDamageStatusArguments(value = 0b1111, frontLeftTire = true, backRightTire = true, backLeftTire = true, frontRightTire = true)
                )

    }

    private class VehicleTiresDamageStatusArguments(
            val value: Int,
            val frontLeftTire: Boolean = false,
            val backLeftTire: Boolean = false,
            val frontRightTire: Boolean = false,
            val backRightTire: Boolean = false
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(value, frontLeftTire, backLeftTire, frontRightTire, backRightTire)

    }

}