package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.*
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
            } returns ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState.ON.value

            val sirenState = vehicle.sirenState

            assertThat(sirenState)
                    .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState.ON)
        }

        @Nested
        inner class ParametersTests {

            @ParameterizedTest
            @ArgumentsSource(VehicleParametersArgumentsProvider::class)
            fun shouldGetParameters(
                    engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
                    lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
                    alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
                    doorLocked: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
                    bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
                    boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
                    objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
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
                    engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
                    lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
                    alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
                    doorLocked: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
                    bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
                    boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
                    objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
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
                        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
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
                        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
                        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
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
                        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
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
                        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
                        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
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
                            .isEqualTo(vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[3], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[6]))
                }

                @Test
                fun shouldSetColors() {
                    every { nativeFunctionExecutor.changeVehicleColor(any(), any(), any()) } returns true

                    vehicle.colors = mutableVehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[50], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[70])

                    verify {
                        nativeFunctionExecutor.changeVehicleColor(vehicleid = vehicleId.value, color1 = 50, color2 = 70)
                    }
                    assertThat(vehicle.colors)
                            .isEqualTo(vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[50], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[70]))
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

            @Test
            fun shouldExecuteOnSpawnHandlers() {
                val onSpawn = mockk<Vehicle.() -> Unit>(relaxed = true)
                vehicle.onSpawn(onSpawn)

                vehicle.onSpawn()

                verify { onSpawn.invoke(vehicle) }
            }

            @Test
            fun shouldExecuteOnDeathHandlers() {
                val onDeath = mockk<Vehicle.(Player?) -> Unit>(relaxed = true)
                vehicle.onDeath(onDeath)

                vehicle.onDeath(player)

                verify { onDeath.invoke(vehicle, player) }
            }

            @Test
            fun shouldExecuteOnEnterHandlers() {
                val onEnter = mockk<Vehicle.(Player, Boolean) -> Unit>(relaxed = true)
                vehicle.onEnter(onEnter)

                vehicle.onEnter(player, true)

                verify { onEnter.invoke(vehicle, player, true) }
            }

            @Test
            fun shouldExecuteOnExitHandlers() {
                val onExit = mockk<Vehicle.(Player) -> Unit>(relaxed = true)
                vehicle.onExit(onExit)

                vehicle.onExit(player)

                verify { onExit.invoke(vehicle, player) }
            }
        }
    }

    private class VehicleParametersArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleParametersArguments> =
                Stream.of(
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.RUNNING,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.ON,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.IS_OR_WAS_SOUNDING,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.LOCKED,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.OPEN,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.OPEN,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.UNSET
                        ),
                        VehicleParametersArguments(
                                engine = ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState.UNSET,
                                lights = ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState.UNSET,
                                alarm = ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState.UNSET,
                                door = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState.UNSET,
                                bonnet = ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState.UNSET,
                                boot = ch.leadrian.samp.kamp.core.api.constants.VehicleBootState.UNSET,
                                objective = ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState.ON
                        )
                )

    }

    private class VehicleParametersArguments(
            val engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
            val lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
            val alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
            val door: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
            val bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
            val boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
            val objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(engine, lights, alarm, door, bonnet, boot, objective)

    }

    private class VehicleDoorStatesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleDoorStatesArguments> =
                Stream.of(
                        VehicleDoorStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET
                        ),
                        VehicleDoorStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState.OPEN
                        )
                )

    }

    private class VehicleDoorStatesArguments(
            val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
            val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }

    private class VehicleWindowStatesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<VehicleWindowStatesArguments> =
                Stream.of(
                        VehicleWindowStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET
                        ),
                        VehicleWindowStatesArguments(
                                driver = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                passenger = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backLeft = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.UNSET,
                                backRight = ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState.OPEN
                        )
                )

    }

    private class VehicleWindowStatesArguments(
            val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
            val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(driver, passenger, backLeft, backRight)

    }
}