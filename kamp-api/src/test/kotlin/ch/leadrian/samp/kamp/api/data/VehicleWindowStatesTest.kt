package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleWindowState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleWindowStatesTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStateArgumentsProvider::class)
    fun toVehicleWindowStatesShouldReturnSameInstance(
            driver: VehicleWindowState,
            passenger: VehicleWindowState,
            backLeft: VehicleWindowState,
            backRight: VehicleWindowState
    ) {
        val expectedVehicleWindowStates = vehicleWindowStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val vehicleWindowStates: VehicleWindowStates = expectedVehicleWindowStates.toVehicleWindowStates()

        assertThat(vehicleWindowStates)
                .isNotInstanceOf(MutableVehicleWindowStates::class.java)
                .isSameAs(expectedVehicleWindowStates)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStateArgumentsProvider::class)
    fun toMutableVehicleWindowStatesShouldReturnImmutableInstance(
            driver: VehicleWindowState,
            passenger: VehicleWindowState,
            backLeft: VehicleWindowState,
            backRight: VehicleWindowState
    ) {
        val vehicleWindowStates = vehicleWindowStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val mutableVehicleWindowStates: MutableVehicleWindowStates = vehicleWindowStates.toMutableVehicleWindowStates()

        assertThat(mutableVehicleWindowStates)
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
                            driver = VehicleWindowState.OPEN,
                            passenger = VehicleWindowState.CLOSED,
                            backRight = VehicleWindowState.UNSET,
                            backLeft = VehicleWindowState.OPEN
                    ),
                    VehicleWindowStateArguments(
                            driver = VehicleWindowState.CLOSED,
                            passenger = VehicleWindowState.UNSET,
                            backRight = VehicleWindowState.OPEN,
                            backLeft = VehicleWindowState.UNSET
                    )
            )
        }

    }

    private class VehicleWindowStateArguments(
            val driver: VehicleWindowState,
            val passenger: VehicleWindowState,
            val backLeft: VehicleWindowState,
            val backRight: VehicleWindowState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}