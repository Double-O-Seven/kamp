package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vehicleDoorStatesOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.reflect.KProperty

internal class VehicleDoorStatesPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleDoorStatesProperty: VehicleDoorStatesProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleDoorStatesProperty = VehicleDoorStatesProperty(nativeFunctionExecutor)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorStatesArgumentsProvider::class)
    fun shouldGetDoorStates(
            driver: VehicleDoorState,
            passenger: VehicleDoorState,
            backLeft: VehicleDoorState,
            backRight: VehicleDoorState
    ) {
        every {
            nativeFunctionExecutor.getVehicleParamsCarDoors(vehicleId.value, any(), any(), any(), any())
        } answers {
            secondArg<ReferenceInt>().value = driver.value
            thirdArg<ReferenceInt>().value = passenger.value
            arg<ReferenceInt>(3).value = backLeft.value
            arg<ReferenceInt>(4).value = backRight.value
            true
        }

        val doorStates = vehicleDoorStatesProperty.getValue(vehicle, property)

        assertThat(doorStates)
                .isEqualTo(
                        vehicleDoorStatesOf(
                                driver = driver,
                                passenger = passenger,
                                backLeft = backLeft,
                                backRight = backRight
                        )
                )
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleDoorStatesArgumentsProvider::class)
    fun shouldSetDoorStates(
            driver: VehicleDoorState,
            passenger: VehicleDoorState,
            backLeft: VehicleDoorState,
            backRight: VehicleDoorState
    ) {
        every {
            nativeFunctionExecutor.setVehicleParamsCarDoors(any(), any(), any(), any(), any())
        } returns true

        vehicleDoorStatesProperty.setValue(
                vehicle,
                property,
                vehicleDoorStatesOf(
                        driver = driver,
                        passenger = passenger,
                        backLeft = backLeft,
                        backRight = backRight
                )
        )

        verify {
            nativeFunctionExecutor.setVehicleParamsCarDoors(
                    vehicleid = vehicleId.value,
                    driver = driver.value,
                    passenger = passenger.value,
                    backleft = backLeft.value,
                    backright = backRight.value
            )
        }
    }

    private class VehicleDoorStatesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleDoorStatesArguments> =
                Stream.of(
                        VehicleDoorStatesArguments(
                                driver = VehicleDoorState.OPEN,
                                passenger = VehicleDoorState.UNSET,
                                backLeft = VehicleDoorState.UNSET,
                                backRight = VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = VehicleDoorState.UNSET,
                                passenger = VehicleDoorState.OPEN,
                                backLeft = VehicleDoorState.UNSET,
                                backRight = VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = VehicleDoorState.UNSET,
                                passenger = VehicleDoorState.UNSET,
                                backLeft = VehicleDoorState.OPEN,
                                backRight = VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = VehicleDoorState.UNSET,
                                passenger = VehicleDoorState.UNSET,
                                backLeft = VehicleDoorState.UNSET,
                                backRight = VehicleDoorState.OPEN
                        )
                )

    }

    private class VehicleDoorStatesArguments(
            val driver: VehicleDoorState,
            val passenger: VehicleDoorState,
            val backLeft: VehicleDoorState,
            val backRight: VehicleDoorState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}