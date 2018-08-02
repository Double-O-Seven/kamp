package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class VehicleParametersTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun toVehicleParametersShouldReturnSameInstance(
            engine: VehicleEngineState,
            lights: VehicleLightsState,
            alarm: VehicleAlarmState,
            doorLock: VehicleDoorLockState,
            bonnet: VehicleBonnetState,
            boot: VehicleBootState,
            objective: VehicleObjectiveState
    ) {
        val expectedVehicleParameters = vehicleParametersOf(
                engine = engine,
                lights = lights,
                alarm = alarm,
                doorLock = doorLock,
                bonnet = bonnet,
                boot = boot,
                objective = objective
        )

        val vehicleParameters: VehicleParameters = expectedVehicleParameters.toVehicleParameters()

        assertThat(vehicleParameters)
                .isNotInstanceOf(MutableVehicleParameters::class.java)
                .isSameAs(expectedVehicleParameters)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun toMutableVehicleParametersShouldReturnImmutableInstance(
            engine: VehicleEngineState,
            lights: VehicleLightsState,
            alarm: VehicleAlarmState,
            doorLock: VehicleDoorLockState,
            bonnet: VehicleBonnetState,
            boot: VehicleBootState,
            objective: VehicleObjectiveState
    ) {
        val vehicleParameters = vehicleParametersOf(
                engine = engine,
                lights = lights,
                alarm = alarm,
                doorLock = doorLock,
                bonnet = bonnet,
                boot = boot,
                objective = objective
        )

        val mutableVehicleParameters: MutableVehicleParameters = vehicleParameters.toMutableVehicleParameters()

        assertThat(mutableVehicleParameters)
                .satisfies {
                    assertThat(it.engine)
                            .isEqualTo(engine)
                    assertThat(it.lights)
                            .isEqualTo(lights)
                    assertThat(it.alarm)
                            .isEqualTo(alarm)
                    assertThat(it.doorLock)
                            .isEqualTo(doorLock)
                    assertThat(it.bonnet)
                            .isEqualTo(bonnet)
                    assertThat(it.boot)
                            .isEqualTo(boot)
                    assertThat(it.objective)
                            .isEqualTo(objective)
                }
    }

    private class VehicleParametersArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    VehicleParametersArguments(
                            engine = VehicleEngineState.RUNNING,
                            lights = VehicleLightsState.ON,
                            alarm = VehicleAlarmState.IS_OR_WAS_SOUNDING,
                            doorLock = VehicleDoorLockState.LOCKED,
                            bonnet = VehicleBonnetState.OPEN,
                            boot = VehicleBootState.OPEN,
                            objective = VehicleObjectiveState.ON
                    ),
                    VehicleParametersArguments(
                            engine = VehicleEngineState.OFF,
                            lights = VehicleLightsState.OFF,
                            alarm = VehicleAlarmState.OFF,
                            doorLock = VehicleDoorLockState.UNLOCKED,
                            bonnet = VehicleBonnetState.CLOSED,
                            boot = VehicleBootState.CLOSED,
                            objective = VehicleObjectiveState.OFF
                    ),
                    VehicleParametersArguments(
                            engine = VehicleEngineState.UNSET,
                            lights = VehicleLightsState.UNSET,
                            alarm = VehicleAlarmState.UNSET,
                            doorLock = VehicleDoorLockState.UNSET,
                            bonnet = VehicleBonnetState.UNSET,
                            boot = VehicleBootState.UNSET,
                            objective = VehicleObjectiveState.UNSET
                    )
            )
        }

    }

    private class VehicleParametersArguments(
            val engine: VehicleEngineState,
            val lights: VehicleLightsState,
            val alarm: VehicleAlarmState,
            val doorLock: VehicleDoorLockState,
            val bonnet: VehicleBonnetState,
            val boot: VehicleBootState,
            val objective: VehicleObjectiveState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(
                engine,
                lights,
                alarm,
                doorLock,
                bonnet,
                boot,
                objective
        )

    }

}