package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleLightsDamageStatusTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleLightsDamageStatusArgumentsProvider::class)
    fun shouldReturnExpectedValues(
            inputValue: Int,
            frontLeftLights: Boolean,
            frontRightLights: Boolean,
            backLights: Boolean
    ) {
        val vehicleLightsDamageStatus = VehicleLightsDamageStatus(inputValue)

        assertThat(vehicleLightsDamageStatus)
                .satisfies {
                    assertThat(it.isFrontLeftLightDamaged)
                            .isEqualTo(frontLeftLights)
                    assertThat(it.isFrontRightLightDamaged)
                            .isEqualTo(frontRightLights)
                    assertThat(it.areBackLightsDamaged)
                            .isEqualTo(backLights)
                }
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleLightsDamageStatusArgumentsProvider::class)
    fun shouldSetValues(
            expectedValue: Int,
            frontLeftLights: Boolean,
            frontRightLights: Boolean,
            backLights: Boolean
    ) {
        val vehicleLightsDamageStatus = VehicleLightsDamageStatus(0)

        vehicleLightsDamageStatus.apply {
            this.isFrontLeftLightDamaged = frontLeftLights
            this.isFrontRightLightDamaged = frontRightLights
            this.areBackLightsDamaged = backLights
        }

        assertThat(vehicleLightsDamageStatus.value)
                .isEqualTo(expectedValue)
    }

    private class VehicleLightsDamageStatusArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(p0: ExtensionContext?): Stream<out Arguments> = Stream.of(
                VehicleLightsDamageStatusArguments(value = 0b00000000),
                VehicleLightsDamageStatusArguments(value = 0b00000001, frontLeftLights = true),
                VehicleLightsDamageStatusArguments(value = 0b00000100, frontRightLights = true),
                VehicleLightsDamageStatusArguments(value = 0b00000101, frontLeftLights = true, frontRightLights = true),
                VehicleLightsDamageStatusArguments(value = 0b01000000, backLights = true),
                VehicleLightsDamageStatusArguments(value = 0b01000001, backLights = true, frontLeftLights = true),
                VehicleLightsDamageStatusArguments(value = 0b01000100, backLights = true, frontRightLights = true),
                VehicleLightsDamageStatusArguments(value = 0b01000101, backLights = true, frontLeftLights = true, frontRightLights = true)
        )

    }

    private class VehicleLightsDamageStatusArguments(
            val value: Int,
            val frontLeftLights: Boolean = false,
            val frontRightLights: Boolean = false,
            val backLights: Boolean = false
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(value, frontLeftLights, frontRightLights, backLights)

    }

}