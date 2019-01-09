package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class MutableVehicleWindowStatesTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStateArgumentsProvider::class)
    fun toMutableVehicleWindowStatesShouldReturnSameInstance(
            driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
    ) {
        val expectedMutableVehicleWindowStates = mutableVehicleWindowStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val mutableVehicleWindowStates: VehicleWindowStates = expectedMutableVehicleWindowStates.toMutableVehicleWindowStates()

        assertThat(mutableVehicleWindowStates)
                .isSameAs(expectedMutableVehicleWindowStates)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStateArgumentsProvider::class)
    fun toVehicleWindowStatesShouldReturnImmutableInstance(
            driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
    ) {
        val mutableVehicleWindowStates = mutableVehicleWindowStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val vehicleWindowStates: VehicleWindowStates = mutableVehicleWindowStates.toVehicleWindowStates()

        assertThat(vehicleWindowStates)
                .isNotInstanceOf(MutableVehicleWindowStates::class.java)
                .satisfies {
                    assertThat(it.driver)
                            .isEqualTo(driver)
                    assertThat(it.passenger)
                            .isEqualTo(passenger)
                    assertThat(it.backLeft)
                            .isEqualTo(backLeft)
                    assertThat(it.backRight)
                            .isEqualTo(backRight)
                }
    }

    private class VehicleWindowStateArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    VehicleWindowStateArguments(
                            driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN,
                            passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.CLOSED,
                            backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                            backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN
                    ),
                    VehicleWindowStateArguments(
                            driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.CLOSED,
                            passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                            backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN,
                            backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET
                    )
            )
        }

    }

    private class VehicleWindowStateArguments(
            val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}