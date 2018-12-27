package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import ch.leadrian.samp.kamp.core.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleLightsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehiclePanelDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleTiresDamageStatus
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVehicleColorsOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.data.vehicleDoorStatesOf
import ch.leadrian.samp.kamp.core.api.data.vehicleParametersOf
import ch.leadrian.samp.kamp.core.api.data.vehicleWindowStatesOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

internal class VehicleTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructVehicle() {
            val vehicleId = VehicleId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createVehicle(
                            vehicletype = VehicleModel.ALPHA.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rotation = 4f,
                            color1 = 3,
                            color2 = 6,
                            respawn_delay = 60,
                            addsiren = true
                    )
                } returns vehicleId.value
            }

            val vehicle = Vehicle(
                    model = VehicleModel.ALPHA,
                    colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = 4f,
                    addSiren = true,
                    respawnDelay = 60,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    vehicleRegistry = mockk()
            )

            assertThat(vehicle.id)
                    .isEqualTo(vehicleId)
        }

        @Test
        fun shouldConstructVehicleWithFallbackValues() {
            val vehicleId = VehicleId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createVehicle(
                            vehicletype = VehicleModel.ALPHA.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rotation = 4f,
                            color1 = 3,
                            color2 = 6,
                            respawn_delay = -1,
                            addsiren = true
                    )
                } returns vehicleId.value
            }

            val vehicle = Vehicle(
                    model = VehicleModel.ALPHA,
                    colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = 4f,
                    addSiren = true,
                    respawnDelay = null,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    vehicleRegistry = mockk()
            )

            assertThat(vehicle.id)
                    .isEqualTo(vehicleId)
        }

        @Test
        fun givenCreateVehicleReturnsInvalidVehicleIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createVehicle(
                            vehicletype = VehicleModel.ALPHA.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rotation = 4f,
                            color1 = 3,
                            color2 = 6,
                            respawn_delay = 60,
                            addsiren = true
                    )
                } returns SAMPConstants.INVALID_VEHICLE_ID
            }

            val caughtThrowable = catchThrowable {
                Vehicle(
                        model = VehicleModel.ALPHA,
                        colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = 4f,
                        addSiren = true,
                        respawnDelay = 60,
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        vehicleRegistry = mockk()
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val vehicleId = VehicleId.valueOf(69)
        private lateinit var vehicle: Vehicle
        private val playerId = PlayerId.valueOf(50)
        private val player = mockk<Player>()

        private val vehicleRegistry = mockk<VehicleRegistry>()
        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every { player.id } returns playerId

            every {
                nativeFunctionExecutor.createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns vehicleId.value
            vehicle = Vehicle(
                    model = VehicleModel.ALPHA,
                    colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = 4f,
                    addSiren = true,
                    respawnDelay = 60,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    vehicleRegistry = vehicleRegistry
            )
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun isStreamedInShouldReturnExpectedReturn(expectedResult: Boolean) {
            every {
                nativeFunctionExecutor.isVehicleStreamedIn(vehicleid = vehicleId.value, forplayerid = playerId.value)
            } returns expectedResult

            val result = vehicle.isStreamedIn(player)

            assertThat(result)
                    .isEqualTo(expectedResult)
        }

        @Nested
        inner class CoordinatesTests {

            @Test
            fun shouldGetCoordinates() {
                every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }

                val coordinates = vehicle.coordinates

                assertThat(coordinates)
                        .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
            }

            @Test
            fun shouldSetCoordinates() {
                every { nativeFunctionExecutor.setVehiclePos(any(), any(), any(), any()) } returns true

                vehicle.coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)

                verify { nativeFunctionExecutor.setVehiclePos(vehicleid = vehicleId.value, x = 1f, y = 2f, z = 3f) }
            }
        }

        @Nested
        inner class PositionTests {

            @Test
            fun shouldGetPosition() {
                every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }
                every { nativeFunctionExecutor.getVehicleZAngle(vehicleId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 4f
                    true
                }

                val position = vehicle.position

                assertThat(position)
                        .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
            }

            @Test
            fun shouldSetPosition() {
                every { nativeFunctionExecutor.setVehiclePos(any(), any(), any(), any()) } returns true
                every { nativeFunctionExecutor.setVehicleZAngle(any(), any()) } returns true

                vehicle.position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)

                verify {
                    nativeFunctionExecutor.setVehiclePos(vehicleid = vehicleId.value, x = 1f, y = 2f, z = 3f)
                    nativeFunctionExecutor.setVehicleZAngle(vehicleid = vehicleId.value, z_angle = 4f)
                }
            }
        }

        @Nested
        inner class LocationTests {

            @Test
            fun shouldGetLocation() {
                every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }
                every { nativeFunctionExecutor.linkVehicleToInterior(any(), any()) } returns true
                vehicle.interiorId = 69
                every { nativeFunctionExecutor.getVehicleVirtualWorld(vehicleId.value) } returns 1337

                val location = vehicle.location

                assertThat(location)
                        .isEqualTo(locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1337))
            }

            @Test
            fun shouldSetLocation() {
                every { nativeFunctionExecutor.setVehiclePos(any(), any(), any(), any()) } returns true
                every { nativeFunctionExecutor.linkVehicleToInterior(any(), any()) } returns true
                every { nativeFunctionExecutor.setVehicleVirtualWorld(any(), any()) } returns true

                vehicle.location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1337)

                verify {
                    nativeFunctionExecutor.setVehiclePos(vehicleid = vehicleId.value, x = 1f, y = 2f, z = 3f)
                    nativeFunctionExecutor.linkVehicleToInterior(vehicleid = vehicleId.value, interiorid = 69)
                    nativeFunctionExecutor.setVehicleVirtualWorld(vehicleid = vehicleId.value, worldid = 1337)
                }
            }
        }

        @Nested
        inner class AngledLocationTests {

            @Test
            fun shouldGetAngledLocation() {
                every { nativeFunctionExecutor.getVehiclePos(vehicleId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }
                every { nativeFunctionExecutor.getVehicleZAngle(vehicleId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 4f
                    true
                }
                every { nativeFunctionExecutor.linkVehicleToInterior(any(), any()) } returns true
                vehicle.interiorId = 69
                every { nativeFunctionExecutor.getVehicleVirtualWorld(vehicleId.value) } returns 1337

                val angledLocation = vehicle.angledLocation

                assertThat(angledLocation)
                        .isEqualTo(angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 69, worldId = 1337))
            }

            @Test
            fun shouldSetAngledLocation() {
                every { nativeFunctionExecutor.setVehiclePos(any(), any(), any(), any()) } returns true
                every { nativeFunctionExecutor.setVehicleZAngle(any(), any()) } returns true
                every { nativeFunctionExecutor.linkVehicleToInterior(any(), any()) } returns true
                every { nativeFunctionExecutor.setVehicleVirtualWorld(any(), any()) } returns true

                vehicle.angledLocation = angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, interiorId = 69, worldId = 1337)

                verify {
                    nativeFunctionExecutor.setVehiclePos(vehicleid = vehicleId.value, x = 1f, y = 2f, z = 3f)
                    nativeFunctionExecutor.setVehicleZAngle(vehicleid = vehicleId.value, z_angle = 4f)
                    nativeFunctionExecutor.linkVehicleToInterior(vehicleid = vehicleId.value, interiorid = 69)
                    nativeFunctionExecutor.setVehicleVirtualWorld(vehicleid = vehicleId.value, worldid = 1337)
                }
            }
        }

        @Nested
        inner class AngleTests {

            @Test
            fun shouldGetAngle() {
                every { nativeFunctionExecutor.getVehicleZAngle(vehicleId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 4f
                    true
                }

                val angle = vehicle.angle

                assertThat(angle)
                        .isEqualTo(4f)
            }

            @Test
            fun shouldSetAngle() {
                every { nativeFunctionExecutor.setVehicleZAngle(any(), any()) } returns true

                vehicle.angle = 4f

                verify { nativeFunctionExecutor.setVehicleZAngle(vehicleid = vehicleId.value, z_angle = 4f) }
            }
        }

        @Nested
        inner class InteriorIdTests {

            @Test
            fun interiorIdShouldBeInitializedToZero() {
                val interiorId = vehicle.interiorId

                assertThat(interiorId)
                        .isZero()
            }

            @Test
            fun shouldSetInteriorId() {
                every { nativeFunctionExecutor.linkVehicleToInterior(any(), any()) } returns true

                vehicle.interiorId = 69

                verify { nativeFunctionExecutor.linkVehicleToInterior(vehicleid = vehicleId.value, interiorid = 69) }
                assertThat(vehicle.interiorId)
                        .isEqualTo(69)
            }
        }

        @Nested
        inner class VirtualWorldIdTests {

            @Test
            fun shouldGetVirtualWorldId() {
                every { nativeFunctionExecutor.getVehicleVirtualWorld(vehicleId.value) } returns 1337

                val virtualWorldId = vehicle.virtualWorldId

                assertThat(virtualWorldId)
                        .isEqualTo(1337)
            }

            @Test
            fun shouldSetVirtualWorldId() {
                every { nativeFunctionExecutor.setVehicleVirtualWorld(any(), any()) } returns true

                vehicle.virtualWorldId = 1337

                verify { nativeFunctionExecutor.setVehicleVirtualWorld(vehicleid = vehicleId.value, worldid = 1337) }
            }
        }

        @Test
        fun shouldRespawn() {
            every { nativeFunctionExecutor.setVehicleToRespawn(any()) } returns true

            vehicle.respawn()

            verify { nativeFunctionExecutor.setVehicleToRespawn(vehicleId.value) }
        }

        @ParameterizedTest
        @CsvSource("true, 1, false, 0", "false, 0, true, 1")
        fun shouldSetVehicleParametersForPlayer(
                objective: Boolean,
                setObjectiveValue: Int,
                locked: Boolean,
                setLockedValue: Int
        ) {
            every { nativeFunctionExecutor.setVehicleParamsForPlayer(any(), any(), any(), any()) } returns true

            vehicle.setParametersForPlayer(forPlayer = player, objective = objective, locked = locked)

            verify {
                nativeFunctionExecutor.setVehicleParamsForPlayer(
                        vehicleid = vehicleId.value,
                        playerid = playerId.value,
                        objective = setObjectiveValue,
                        doorslocked = setLockedValue
                )
            }
        }

        @Test
        fun shouldGetSirenState() {
            every {
                nativeFunctionExecutor.getVehicleParamsSirenState(vehicleId.value)
            } returns VehicleSirenState.ON.value

            val sirenState = vehicle.sirenState

            assertThat(sirenState)
                    .isEqualTo(VehicleSirenState.ON)
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun containsShouldReturnTrueIfAndOnlyIfPlayerIsInVehicle(isInVehicle: Boolean) {
            every { player.isInVehicle(vehicle) } returns isInVehicle

            val containsPlayer = player in vehicle

            assertThat(containsPlayer)
                    .isEqualTo(isInVehicle)
        }

        @Nested
        inner class ParametersTests {

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
                    nativeFunctionExecutor.getVehicleParamsEx(vehicleId.value, any(), any(), any(), any(), any(), any(), any())
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

                val parameters = vehicle.parameters

                assertThat(parameters)
                        .isEqualTo(vehicleParametersOf(
                                engine = engine,
                                lights = lights,
                                alarm = alarm,
                                doorLock = doorLocked,
                                bonnet = bonnet,
                                boot = boot,
                                objective = objective
                        ))
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
                    nativeFunctionExecutor.setVehicleParamsEx(vehicleId.value, any(), any(), any(), any(), any(), any(), any())
                } returns true

                vehicle.parameters = vehicleParametersOf(
                        engine = engine,
                        lights = lights,
                        alarm = alarm,
                        doorLock = doorLocked,
                        bonnet = bonnet,
                        boot = boot,
                        objective = objective
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

            @Nested
            inner class DoorStatesTests {

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

                    val doorStates = vehicle.doorStates

                    assertThat(doorStates)
                            .isEqualTo(vehicleDoorStatesOf(
                                    driver = driver,
                                    passenger = passenger,
                                    backLeft = backLeft,
                                    backRight = backRight
                            ))
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

                    vehicle.doorStates = vehicleDoorStatesOf(
                            driver = driver,
                            passenger = passenger,
                            backLeft = backLeft,
                            backRight = backRight
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
            }

            @Nested
            inner class WindowStatesTests {

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

                    val windowStates = vehicle.windowStates

                    assertThat(windowStates)
                            .isEqualTo(vehicleWindowStatesOf(
                                    driver = driver,
                                    passenger = passenger,
                                    backLeft = backLeft,
                                    backRight = backRight
                            ))
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

                    vehicle.windowStates = vehicleWindowStatesOf(
                            driver = driver,
                            passenger = passenger,
                            backLeft = backLeft,
                            backRight = backRight
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
            }

            @Test
            fun shouldInitializeVehicleComponents() {
                val vehicleComponents = vehicle.components

                assertThat(vehicleComponents)
                        .isInstanceOfSatisfying(VehicleComponents::class.java) {
                            assertThat(it.vehicle)
                                    .isEqualTo(vehicle)
                        }
            }

            @Nested
            inner class ColorsTests {

                @Test
                fun shouldInitializeColors() {
                    val colors = vehicle.colors

                    assertThat(colors)
                            .isEqualTo(vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]))
                }

                @Test
                fun shouldSetColors() {
                    every { nativeFunctionExecutor.changeVehicleColor(any(), any(), any()) } returns true

                    vehicle.colors = mutableVehicleColorsOf(color1 = VehicleColor[50], color2 = VehicleColor[70])

                    verify {
                        nativeFunctionExecutor.changeVehicleColor(vehicleid = vehicleId.value, color1 = 50, color2 = 70)
                    }
                    assertThat(vehicle.colors)
                            .isEqualTo(vehicleColorsOf(color1 = VehicleColor[50], color2 = VehicleColor[70]))
                }
            }

            @Nested
            inner class PaintjobTests {

                @Test
                fun shouldInitializePaintjobWithNull() {
                    val paintjob = vehicle.paintjob

                    assertThat(paintjob)
                            .isNull()
                }

                @Test
                fun shouldSetPaintjob() {
                    every {
                        nativeFunctionExecutor.changeVehiclePaintjob(any(), any())
                    } returns true
                    // Need to extract it because there seems to be some smartcast issue here
                    val paintjob: Int? = 1

                    vehicle.paintjob = paintjob

                    verify {
                        nativeFunctionExecutor.changeVehiclePaintjob(vehicleid = vehicleId.value, paintjobid = paintjob!!)
                    }
                    assertThat(vehicle.paintjob)
                            .isEqualTo(paintjob)
                }

                @Test
                fun givenPaintjobIsSetToNullItShouldChangePaintjobToMinusOne() {
                    every {
                        nativeFunctionExecutor.changeVehiclePaintjob(any(), any())
                    } returns true
                    // Need to extract it because there seems to be some smartcast issue here
                    val paintjob: Int? = null

                    vehicle.paintjob = paintjob

                    verify {
                        nativeFunctionExecutor.changeVehiclePaintjob(vehicleid = vehicleId.value, paintjobid = -1)
                    }
                    assertThat(vehicle.paintjob)
                            .isNull()
                }
            }

            @Nested
            inner class HealthTests {

                @Test
                fun shouldGetHealth() {
                    every { nativeFunctionExecutor.getVehicleHealth(vehicleId.value, any()) } answers {
                        secondArg<ReferenceFloat>().value = 50f
                        true
                    }

                    val health = vehicle.health

                    assertThat(health)
                            .isEqualTo(50f)
                }

                @Test
                fun shouldSetHealth() {
                    every { nativeFunctionExecutor.setVehicleHealth(any(), any()) } returns true

                    vehicle.health = 50f

                    verify { nativeFunctionExecutor.setVehicleHealth(vehicleid = vehicleId.value, health = 50f) }
                }
            }

            @Test
            fun shouldInitializeTrailer() {
                val trailer = vehicle.trailer

                assertThat(trailer)
                        .isInstanceOfSatisfying(VehicleTrailer::class.java) {
                            assertThat(it.vehicle)
                                    .isEqualTo(vehicle)
                        }
            }

            @Nested
            inner class NumberPlateTests {

                @Test
                fun shouldInitializeNumberPlateWithNull() {
                    val numberPlate = vehicle.numberPlate

                    assertThat(numberPlate)
                            .isNull()
                }

                @Test
                fun shouldSetVehicleNumberPlate() {
                    every { nativeFunctionExecutor.setVehicleNumberPlate(any(), any()) } returns true

                    vehicle.numberPlate = "ABC123"

                    verify {
                        nativeFunctionExecutor.setVehicleNumberPlate(vehicleid = vehicleId.value, numberplate = "ABC123")
                    }
                    assertThat(vehicle.numberPlate)
                            .isEqualTo("ABC123")
                }

                @Test
                fun shouldNotSetEmptyNumberPlate() {
                    every { nativeFunctionExecutor.setVehicleNumberPlate(any(), any()) } returns true
                    vehicle.numberPlate = "ABC123"

                    vehicle.numberPlate = ""


                    verify(exactly = 0) {
                        nativeFunctionExecutor.setVehicleNumberPlate(vehicleid = vehicleId.value, numberplate = "")
                    }
                    assertThat(vehicle.numberPlate)
                            .isEqualTo("ABC123")
                }

                @Test
                fun shouldNotSetNullNumberPlate() {
                    every { nativeFunctionExecutor.setVehicleNumberPlate(any(), any()) } returns true
                    vehicle.numberPlate = "ABC123"

                    val nullNumberPlate: String? = null
                    vehicle.numberPlate = nullNumberPlate


                    verify(exactly = 0) {
                        nativeFunctionExecutor.setVehicleNumberPlate(vehicleid = vehicleId.value, numberplate = "")
                    }
                    assertThat(vehicle.numberPlate)
                            .isEqualTo("ABC123")
                }
            }

            @Test
            fun shouldRepairVehicle() {
                every { nativeFunctionExecutor.repairVehicle(any()) } returns true

                vehicle.repair()

                verify { nativeFunctionExecutor.repairVehicle(vehicleId.value) }
            }

            @Nested
            inner class VelocityTests {

                @Test
                fun shouldGetVelocity() {
                    every { nativeFunctionExecutor.getVehicleVelocity(vehicleId.value, any(), any(), any()) } answers {
                        secondArg<ReferenceFloat>().value = 1f
                        thirdArg<ReferenceFloat>().value = 2f
                        arg<ReferenceFloat>(3).value = 3f
                        true
                    }

                    val velocity = vehicle.velocity

                    assertThat(velocity)
                            .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
                }

                @Test
                fun shouldSetVelocity() {
                    every { nativeFunctionExecutor.setVehicleVelocity(any(), any(), any(), any()) } returns true

                    vehicle.velocity = vector3DOf(x = 1f, y = 2f, z = 3f)

                    verify { nativeFunctionExecutor.setVehicleVelocity(vehicleid = vehicleId.value, X = 1f, Y = 2f, Z = 3f) }
                }
            }

            @Test
            fun shouldSetAngularVelocity() {
                every { nativeFunctionExecutor.setVehicleAngularVelocity(any(), any(), any(), any()) } returns true

                vehicle.setAngularVelocity(vector3DOf(x = 1f, y = 2f, z = 3f))

                verify { nativeFunctionExecutor.setVehicleAngularVelocity(vehicleid = vehicleId.value, X = 1f, Y = 2f, Z = 3f) }
            }

            @Nested
            inner class DamageStatusTests {

                @Test
                fun shouldGetDamageStatus() {
                    every {
                        nativeFunctionExecutor.getVehicleDamageStatus(vehicleId.value, any(), any(), any(), any())
                    } answers {
                        secondArg<ReferenceInt>().value = 10
                        thirdArg<ReferenceInt>().value = 20
                        arg<ReferenceInt>(3).value = 30
                        arg<ReferenceInt>(4).value = 40
                        true
                    }

                    val damageStatus = vehicle.damageStatus

                    assertThat(damageStatus)
                            .isEqualTo(VehicleDamageStatus(
                                    panels = VehiclePanelDamageStatus(10),
                                    doors = VehicleDoorsDamageStatus(20),
                                    lights = VehicleLightsDamageStatus(30),
                                    tires = VehicleTiresDamageStatus(40)
                            ))
                }

                @Test
                fun shouldSetDamageStatus() {
                    every {
                        nativeFunctionExecutor.updateVehicleDamageStatus(any(), any(), any(), any(), any())
                    } returns true

                    vehicle.damageStatus = VehicleDamageStatus(
                            panels = VehiclePanelDamageStatus(10),
                            doors = VehicleDoorsDamageStatus(20),
                            lights = VehicleLightsDamageStatus(30),
                            tires = VehicleTiresDamageStatus(40)
                    )

                    verify {
                        nativeFunctionExecutor.updateVehicleDamageStatus(
                                vehicleid = vehicleId.value,
                                panels = 10,
                                doors = 20,
                                lights = 30,
                                tires = 40
                        )
                    }
                }
            }

            @Nested
            inner class DestroyTests {

                @BeforeEach
                fun setUp() {
                    every { nativeFunctionExecutor.destroyVehicle(any()) } returns true
                }

                @Test
                fun isDestroyedShouldInitiallyBeFalse() {
                    val isDestroyed = vehicle.isDestroyed

                    assertThat(isDestroyed)
                            .isFalse()
                }

                @Test
                fun shouldDestroyVehicle() {
                    val onDestroy = mockk<Vehicle.() -> Unit>(relaxed = true)
                    vehicle.onDestroy(onDestroy)

                    vehicle.destroy()

                    verifyOrder {
                        onDestroy.invoke(vehicle)
                        nativeFunctionExecutor.destroyVehicle(vehicleId.value)
                    }
                    assertThat(vehicle.isDestroyed)
                            .isTrue()
                }

                @Test
                fun shouldNotExecuteDestroyTwice() {
                    val onDestroy = mockk<Vehicle.() -> Unit>(relaxed = true)
                    vehicle.onDestroy(onDestroy)

                    vehicle.destroy()
                    vehicle.destroy()

                    verify(exactly = 1) {
                        nativeFunctionExecutor.destroyVehicle(vehicleId.value)
                        onDestroy.invoke(vehicle)
                    }
                }

                @Test
                fun givenItDestroyedIdShouldThrowException() {
                    vehicle.destroy()

                    val caughtThrowable = catchThrowable { vehicle.id }

                    assertThat(caughtThrowable)
                            .isInstanceOf(AlreadyDestroyedException::class.java)
                }
            }

            @Nested
            inner class OnVehicleSpawnListenersTests {

                @Test
                fun shouldCallAddedListener() {
                    val listener = mockk<OnVehicleSpawnListener>(relaxed = true)
                    vehicle.addOnVehicleSpawnListener(listener)

                    vehicle.onSpawn()

                    verify { listener.onVehicleSpawn(vehicle) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val listener = mockk<OnVehicleSpawnListener>(relaxed = true)
                    vehicle.addOnVehicleSpawnListener(listener)
                    vehicle.removeOnVehicleSpawnListener(listener)

                    vehicle.onSpawn()

                    verify(exactly = 0) { listener.onVehicleSpawn(any()) }
                }

                @Test
                fun shouldCallInlinedListener() {
                    val onSpawn = mockk<Vehicle.() -> Unit>(relaxed = true)
                    vehicle.onSpawn(onSpawn)

                    vehicle.onSpawn()

                    verify { onSpawn.invoke(vehicle) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val onSpawn = mockk<Vehicle.() -> Unit>(relaxed = true)
                    val listener = vehicle.onSpawn(onSpawn)
                    vehicle.removeOnVehicleSpawnListener(listener)

                    vehicle.onSpawn()

                    verify(exactly = 0) { onSpawn.invoke(any()) }
                }

            }

            @Nested
            inner class OnVehicleDeathListenersTests {

                @Test
                fun shouldCallAddedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleDeathListener>(relaxed = true)
                    vehicle.addOnVehicleDeathListener(listener)

                    vehicle.onDeath(player)

                    verify { listener.onVehicleDeath(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleDeathListener>(relaxed = true)
                    vehicle.addOnVehicleDeathListener(listener)
                    vehicle.removeOnVehicleDeathListener(listener)

                    vehicle.onDeath(player)

                    verify(exactly = 0) { listener.onVehicleDeath(any(), any()) }
                }

                @Test
                fun shouldCallInlinedListener() {
                    val player = mockk<Player>()
                    val onDeath = mockk<Vehicle.(Player?) -> Unit>(relaxed = true)
                    vehicle.onDeath(onDeath)

                    vehicle.onDeath(player)

                    verify { onDeath.invoke(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val player = mockk<Player>()
                    val onDeath = mockk<Vehicle.(Player?) -> Unit>(relaxed = true)
                    val listener = vehicle.onDeath(onDeath)
                    vehicle.removeOnVehicleDeathListener(listener)

                    vehicle.onDeath(player)

                    verify(exactly = 0) { onDeath.invoke(any(), any()) }
                }

            }

            @Nested
            inner class OnPlayerEnterVehicleListenersTests {

                @ParameterizedTest
                @ValueSource(strings = ["true", "false"])
                fun shouldCallAddedListener(isPassenger: Boolean) {
                    val player = mockk<Player>()
                    val listener = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
                    vehicle.addOnPlayerEnterVehicleListener(listener)

                    vehicle.onEnter(player, isPassenger)

                    verify { listener.onPlayerEnterVehicle(player, vehicle, isPassenger) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
                    vehicle.addOnPlayerEnterVehicleListener(listener)
                    vehicle.removeOnPlayerEnterVehicleListener(listener)

                    vehicle.onEnter(player, true)

                    verify(exactly = 0) { listener.onPlayerEnterVehicle(any(), any(), any()) }
                }

                @ParameterizedTest
                @ValueSource(strings = ["true", "false"])
                fun shouldCallInlinedListener(isPassenger: Boolean) {
                    val player = mockk<Player>()
                    val onEnter = mockk<Vehicle.(Player, Boolean) -> Unit>(relaxed = true)
                    vehicle.onEnter(onEnter)

                    vehicle.onEnter(player, isPassenger)

                    verify { onEnter.invoke(vehicle, player, isPassenger) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val player = mockk<Player>()
                    val onEnter = mockk<Vehicle.(Player, Boolean) -> Unit>(relaxed = true)
                    val listener = vehicle.onEnter(onEnter)
                    vehicle.removeOnPlayerEnterVehicleListener(listener)

                    vehicle.onEnter(player, true)

                    verify(exactly = 0) { onEnter.invoke(any(), any(), any()) }
                }

            }

            @Nested
            inner class OnPlayerExitVehicleListenersTests {

                @Test
                fun shouldCallAddedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnPlayerExitVehicleListener>(relaxed = true)
                    vehicle.addOnPlayerExitVehicleListener(listener)

                    vehicle.onExit(player)

                    verify { listener.onPlayerExitVehicle(player, vehicle) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnPlayerExitVehicleListener>(relaxed = true)
                    vehicle.addOnPlayerExitVehicleListener(listener)
                    vehicle.removeOnPlayerExitVehicleListener(listener)

                    vehicle.onExit(player)

                    verify(exactly = 0) { listener.onPlayerExitVehicle(any(), any()) }
                }

                @Test
                fun shouldCallInlinedListener() {
                    val player = mockk<Player>()
                    val onExit = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    vehicle.onExit(onExit)

                    vehicle.onExit(player)

                    verify { onExit.invoke(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val player = mockk<Player>()
                    val onExit = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    val listener = vehicle.onExit(onExit)
                    vehicle.removeOnPlayerExitVehicleListener(listener)

                    vehicle.onExit(player)

                    verify(exactly = 0) { onExit.invoke(any(), any()) }
                }

            }

            @Nested
            inner class OnVehicleStreamInListenersTests {

                @Test
                fun shouldCallAddedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleStreamInListener>(relaxed = true)
                    vehicle.addOnVehicleStreamInListener(listener)

                    vehicle.onStreamIn(player)

                    verify { listener.onVehicleStreamIn(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleStreamInListener>(relaxed = true)
                    vehicle.addOnVehicleStreamInListener(listener)
                    vehicle.removeOnVehicleStreamInListener(listener)

                    vehicle.onStreamIn(player)

                    verify(exactly = 0) { listener.onVehicleStreamIn(any(), any()) }
                }

                @Test
                fun shouldCallInlinedListener() {
                    val player = mockk<Player>()
                    val onStreamIn = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    vehicle.onStreamIn(onStreamIn)

                    vehicle.onStreamIn(player)

                    verify { onStreamIn.invoke(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val player = mockk<Player>()
                    val onStreamIn = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    val listener = vehicle.onStreamIn(onStreamIn)
                    vehicle.removeOnVehicleStreamInListener(listener)

                    vehicle.onStreamIn(player)

                    verify(exactly = 0) { onStreamIn.invoke(any(), any()) }
                }

            }

            @Nested
            inner class OnVehicleStreamOutListenersTests {

                @Test
                fun shouldCallAddedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleStreamOutListener>(relaxed = true)
                    vehicle.addOnVehicleStreamOutListener(listener)

                    vehicle.onStreamOut(player)

                    verify { listener.onVehicleStreamOut(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedListener() {
                    val player = mockk<Player>()
                    val listener = mockk<OnVehicleStreamOutListener>(relaxed = true)
                    vehicle.addOnVehicleStreamOutListener(listener)
                    vehicle.removeOnVehicleStreamOutListener(listener)

                    vehicle.onStreamOut(player)

                    verify(exactly = 0) { listener.onVehicleStreamOut(any(), any()) }
                }

                @Test
                fun shouldCallInlinedListener() {
                    val player = mockk<Player>()
                    val onStreamOut = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    vehicle.onStreamOut(onStreamOut)

                    vehicle.onStreamOut(player)

                    verify { onStreamOut.invoke(vehicle, player) }
                }

                @Test
                fun shouldNotCallRemovedInlinedListener() {
                    val player = mockk<Player>()
                    val onStreamOut = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                    val listener = vehicle.onStreamOut(onStreamOut)
                    vehicle.removeOnVehicleStreamOutListener(listener)

                    vehicle.onStreamOut(player)

                    verify(exactly = 0) { onStreamOut.invoke(any(), any()) }
                }

            }
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