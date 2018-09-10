package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
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
            driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
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
            driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
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
                            driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN,
                            passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.CLOSED,
                            backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                            backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN
                    ),
                    VehicleDoorStateArguments(
                            driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.CLOSED,
                            passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                            backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN,
                            backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET
                    )
            )
        }

    }

    private class VehicleDoorStateArguments(
            val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}