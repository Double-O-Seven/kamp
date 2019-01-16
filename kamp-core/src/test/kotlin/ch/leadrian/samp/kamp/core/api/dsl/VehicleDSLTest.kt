package ch.leadrian.samp.kamp.core.api.dsl

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleDSLTest {

    @Nested
    inner class VehicleSeatTests {

        @Test
        fun givenPlayerIsInVehicleSeatContainsShouldReturnTrue() {
            val player: Player = mockk {
                every { vehicleSeat } returns 1
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isInVehicleSeat = player in vehicle seat 1

            assertThat(isInVehicleSeat)
                    .isTrue()
        }

        @Test
        fun givenPlayerIsNotInVehicleSeatContainsShouldReturnTrue() {
            val player: Player = mockk {
                every { vehicleSeat } returns 2
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isInVehicleSeat = player in vehicle seat 1

            assertThat(isInVehicleSeat)
                    .isFalse()
        }

        @Test
        fun givenPlayerIsNotInVehicleContainsShouldReturnFalse() {
            val player: Player = mockk {
                every { vehicleSeat } returns 1
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns false
            }

            val isInVehicleSeat = player in vehicle seat 1

            assertThat(isInVehicleSeat)
                    .isFalse()
        }
    }

    @Nested
    inner class IsDriverOfTests {

        @Test
        fun givenPlayerIsDriveOfVehicleItShouldReturnTrue() {
            val player: Player = mockk {
                every { vehicleSeat } returns VehicleSeat.DRIVER
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isDriver = player isDriverOf vehicle

            assertThat(isDriver)
                    .isTrue()
        }

        @Test
        fun givenPlayerIsNotInVehicleItShouldReturnFalse() {
            val player: Player = mockk {
                every { vehicleSeat } returns VehicleSeat.DRIVER
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns false
            }

            val isDriver = player isDriverOf vehicle

            assertThat(isDriver)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [VehicleSeat.FRONT_PASSENGER, VehicleSeat.REAR_PASSENGER_1, VehicleSeat.REAR_PASSENGER_2])
        fun givenPlayerIsPassengerItShouldReturnFalse(seatId: Int) {
            val player: Player = mockk {
                every { vehicleSeat } returns seatId
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isDriver = player isDriverOf vehicle

            assertThat(isDriver)
                    .isFalse()
        }

    }

    @Nested
    inner class IsPassengerOfTests {

        @Test
        fun givenPlayerIsDriveOfVehicleItShouldReturnFalse() {
            val player: Player = mockk {
                every { vehicleSeat } returns VehicleSeat.DRIVER
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isDriver = player isPassengerOf vehicle

            assertThat(isDriver)
                    .isFalse()
        }

        @Test
        fun givenPlayerIsNotInVehicleItShouldReturnFalse() {
            val player: Player = mockk {
                every { vehicleSeat } returns VehicleSeat.FRONT_PASSENGER
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns false
            }

            val isDriver = player isPassengerOf vehicle

            assertThat(isDriver)
                    .isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [VehicleSeat.FRONT_PASSENGER, VehicleSeat.REAR_PASSENGER_1, VehicleSeat.REAR_PASSENGER_2])
        fun givenPlayerIsPassengerItShouldReturnTrue(seatId: Int) {
            val player: Player = mockk {
                every { vehicleSeat } returns seatId
            }
            val vehicle: Vehicle = mockk {
                every { contains(player) } returns true
            }

            val isDriver = player isPassengerOf vehicle

            assertThat(isDriver)
                    .isTrue()
        }

    }

}