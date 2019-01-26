package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vehicleWindowStatesOf
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

internal class VehicleWindowStatesPropertyTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleWindowStatesProperty: VehicleWindowStatesProperty

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleWindowStatesProperty = VehicleWindowStatesProperty(nativeFunctionExecutor)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStatesArgumentsProvider::class)
    fun shouldGetWindowStates(
            driver: VehicleWindowState,
            passenger: VehicleWindowState,
            backLeft: VehicleWindowState,
            backRight: VehicleWindowState
    ) {
        every {
            nativeFunctionExecutor.getVehicleParamsCarWindows(vehicleId.value, any(), any(), any(), any())
        } answers {
            secondArg<ReferenceInt>().value = driver.value
            thirdArg<ReferenceInt>().value = passenger.value
            arg<ReferenceInt>(3).value = backLeft.value
            arg<ReferenceInt>(4).value = backRight.value
            true
        }

        val windowStates = vehicleWindowStatesProperty.getValue(vehicle, property)

        assertThat(windowStates)
                .isEqualTo(
                        vehicleWindowStatesOf(
                                driver = driver,
                                passenger = passenger,
                                backLeft = backLeft,
                                backRight = backRight
                        )
                )
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleWindowStatesArgumentsProvider::class)
    fun shouldSetWindowStates(
            driver: VehicleWindowState,
            passenger: VehicleWindowState,
            backLeft: VehicleWindowState,
            backRight: VehicleWindowState
    ) {
        every {
            nativeFunctionExecutor.setVehicleParamsCarWindows(any(), any(), any(), any(), any())
        } returns true

        vehicleWindowStatesProperty.setValue(
                vehicle,
                property,
                vehicleWindowStatesOf(
                        driver = driver,
                        passenger = passenger,
                        backLeft = backLeft,
                        backRight = backRight
                )
        )

        verify {
            nativeFunctionExecutor.setVehicleParamsCarWindows(
                    vehicleid = vehicleId.value,
                    driver = driver.value,
                    passenger = passenger.value,
                    backleft = backLeft.value,
                    backright = backRight.value
            )
        }
    }

    private class VehicleWindowStatesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleWindowStatesArguments> =
                Stream.of(
                        VehicleWindowStatesArguments(
                                driver = VehicleWindowState.OPEN,
                                passenger = VehicleWindowState.UNSET,
                                backLeft = VehicleWindowState.UNSET,
                                backRight = VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = VehicleWindowState.UNSET,
                                passenger = VehicleWindowState.OPEN,
                                backLeft = VehicleWindowState.UNSET,
                                backRight = VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = VehicleWindowState.UNSET,
                                passenger = VehicleWindowState.UNSET,
                                backLeft = VehicleWindowState.OPEN,
                                backRight = VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = VehicleWindowState.UNSET,
                                passenger = VehicleWindowState.UNSET,
                                backLeft = VehicleWindowState.UNSET,
                                backRight = VehicleWindowState.OPEN
                        )
                )

    }

    private class VehicleWindowStatesArguments(
            val driver: VehicleWindowState,
            val passenger: VehicleWindowState,
            val backLeft: VehicleWindowState,
            val backRight: VehicleWindowState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

}