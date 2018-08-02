package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleDoorState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleDoorStatesTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorStateArgumentsProvider::class)
    fun toVehicleDoorStatesShouldReturnSameInstance(
            driver: VehicleDoorState,
            passenger: VehicleDoorState,
            backLeft: VehicleDoorState,
            backRight: VehicleDoorState
    ) {
        val expectedVehicleDoorStates = vehicleDoorStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val vehicleDoorStates: VehicleDoorStates = expectedVehicleDoorStates.toVehicleDoorStates()

        assertThat(vehicleDoorStates)
                .isNotInstanceOf(MutableVehicleDoorStates::class.java)
                .isSameAs(expectedVehicleDoorStates)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorStateArgumentsProvider::class)
    fun toMutableVehicleDoorStatesShouldReturnMutableInstance(
            driver: VehicleDoorState,
            passenger: VehicleDoorState,
            backLeft: VehicleDoorState,
            backRight: VehicleDoorState
    ) {
        val vehicleDoorStates = vehicleDoorStatesOf(
                driver = driver,
                passenger = passenger,
                backRight = backRight,
                backLeft = backLeft
        )

        val mutableVehicleDoorStates: MutableVehicleDoorStates = vehicleDoorStates.toMutableVehicleDoorStates()

        assertThat(mutableVehicleDoorStates)
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

    private class VehicleDoorStateArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    VehicleDoorStateArguments(
                            driver = VehicleDoorState.OPEN,
                            passenger = VehicleDoorState.CLOSED,
                            backRight = VehicleDoorState.UNSET,
                            backLeft = VehicleDoorState.OPEN
                    ),
                    VehicleDoorStateArguments(
                            driver = VehicleDoorState.CLOSED,
                            passenger = VehicleDoorState.UNSET,
                            backRight = VehicleDoorState.OPEN,
                            backLeft = VehicleDoorState.UNSET
                    )
            )
        }

    }

    private class VehicleDoorStateArguments(
            val driver: VehicleDoorState,
            val passenger: VehicleDoorState,
            val backLeft: VehicleDoorState,
            val backRight: VehicleDoorState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}