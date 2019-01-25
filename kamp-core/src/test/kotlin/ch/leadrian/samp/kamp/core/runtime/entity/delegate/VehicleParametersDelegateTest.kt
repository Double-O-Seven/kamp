package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vehicleParametersOf
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

internal class VehicleParametersDelegateTest {

    private val vehicleId: VehicleId = VehicleId.valueOf(50)
    private val vehicle: Vehicle = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var vehicleParametersDelegate: VehicleParametersDelegate

    @BeforeEach
    fun setUp() {
        every { vehicle.id } returns vehicleId
        vehicleParametersDelegate = VehicleParametersDelegate(nativeFunctionExecutor)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun shouldGetParameters(
            engine: VehicleEngineState,
            lights: VehicleLightsState,
            alarm: VehicleAlarmState,
            doorLocked: VehicleDoorLockState,
            bonnet: VehicleBonnetState,
            boot: VehicleBootState,
            objective: VehicleObjectiveState
    ) {
        every {
            nativeFunctionExecutor.getVehicleParamsEx(
                    vehicleId.value,
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } answers {
            secondArg<ReferenceInt>().value = engine.value
            thirdArg<ReferenceInt>().value = lights.value
            arg<ReferenceInt>(3).value = alarm.value
            arg<ReferenceInt>(4).value = doorLocked.value
            arg<ReferenceInt>(5).value = bonnet.value
            arg<ReferenceInt>(6).value = boot.value
            arg<ReferenceInt>(7).value = objective.value
            true
        }

        val parameters = vehicleParametersDelegate.getValue(vehicle, property)

        assertThat(parameters)
                .isEqualTo(
                        vehicleParametersOf(
                                engine = engine,
                                lights = lights,
                                alarm = alarm,
                                doorLock = doorLocked,
                                bonnet = bonnet,
                                boot = boot,
                                objective = objective
                        )
                )
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun shouldSetParameters(
            engine: VehicleEngineState,
            lights: VehicleLightsState,
            alarm: VehicleAlarmState,
            doorLocked: VehicleDoorLockState,
            bonnet: VehicleBonnetState,
            boot: VehicleBootState,
            objective: VehicleObjectiveState
    ) {
        every {
            nativeFunctionExecutor.setVehicleParamsEx(
                    vehicleId.value,
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } returns true

        vehicleParametersDelegate.setValue(
                vehicle,
                property,
                vehicleParametersOf(
                        engine = engine,
                        lights = lights,
                        alarm = alarm,
                        doorLock = doorLocked,
                        bonnet = bonnet,
                        boot = boot,
                        objective = objective
                )
        )

        verify {
            nativeFunctionExecutor.setVehicleParamsEx(
                    vehicleid = vehicleId.value,
                    engine = engine.value,
                    lights = lights.value,
                    alarm = alarm.value,
                    doors = doorLocked.value,
                    bonnet = bonnet.value,
                    boot = boot.value,
                    objective = objective.value
            )
        }
    }

    private class VehicleParametersArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleParametersArguments> =
                Stream.of(
                        VehicleParametersArguments(
                                engine = VehicleEngineState.RUNNING,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.ON,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.IS_OR_WAS_SOUNDING,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.LOCKED,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.OPEN,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.OPEN,
                                objective = VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = VehicleEngineState.UNSET,
                                lights = VehicleLightsState.UNSET,
                                alarm = VehicleAlarmState.UNSET,
                                door = VehicleDoorLockState.UNSET,
                                bonnet = VehicleBonnetState.UNSET,
                                boot = VehicleBootState.UNSET,
                                objective = VehicleObjectiveState.ON
                        )
                )

    }

    private class VehicleParametersArguments(
            val engine: VehicleEngineState,
            val lights: VehicleLightsState,
            val alarm: VehicleAlarmState,
            val door: VehicleDoorLockState,
            val bonnet: VehicleBonnetState,
            val boot: VehicleBootState,
            val objective: VehicleObjectiveState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(engine, lights, alarm, door, bonnet, boot, objective)

    }

}