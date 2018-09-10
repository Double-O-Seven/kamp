package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class MutableVehicleParametersTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun toMutableVehicleParametersShouldReturnSameInstance(
            engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
            lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
            alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
            doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
            bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
            boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
            objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
    ) {
        val expectedMutableVehicleParameters = mutableVehicleParametersOf(
                engine = engine,
                lights = lights,
                alarm = alarm,
                doorLock = doorLock,
                bonnet = bonnet,
                boot = boot,
                objective = objective
        )

        val mutableVehicleParameters: VehicleParameters = expectedMutableVehicleParameters.toMutableVehicleParameters()

        assertThat(mutableVehicleParameters)
                .isSameAs(expectedMutableVehicleParameters)
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleParametersArgumentsProvider::class)
    fun toVehicleParametersShouldReturnImmutableInstance(
            engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
            lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
            alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
            doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
            bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
            boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
            objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
    ) {
        val mutableVehicleParameters = mutableVehicleParametersOf(
                engine = engine,
                lights = lights,
                alarm = alarm,
                doorLock = doorLock,
                bonnet = bonnet,
                boot = boot,
                objective = objective
        )

        val vehicleParameters: VehicleParameters = mutableVehicleParameters.toVehicleParameters()

        assertThat(vehicleParameters)
                .isNotInstanceOf(MutableVehicleParameters::class.java)
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
                            engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.RUNNING,
                            lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.ON,
                            alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.IS_OR_WAS_SOUNDING,
                            doorLock = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.LOCKED,
                            bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.OPEN,
                            boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.OPEN,
                            objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.ON
                    ),
                    VehicleParametersArguments(
                            engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.OFF,
                            lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.OFF,
                            alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.OFF,
                            doorLock = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNLOCKED,
                            bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.CLOSED,
                            boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.CLOSED,
                            objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.OFF
                    ),
                    VehicleParametersArguments(
                            engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                            lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                            alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                            doorLock = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                            bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                            boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                            objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                    )
            )
        }

    }

    private class VehicleParametersArguments(
            val engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
            val lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
            val alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
            val doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
            val bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
            val boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
            val objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
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