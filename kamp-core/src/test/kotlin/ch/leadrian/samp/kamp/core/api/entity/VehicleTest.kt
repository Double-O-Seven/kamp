package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.data.mutableVehicleColorsOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEnterVehicleReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerExitVehicleReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleDeathReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleSpawnReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleStreamInReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

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
        private val onVehicleSpawnReceiver = mockk<OnVehicleSpawnReceiverDelegate>()
        private val onVehicleDeathReceiver = mockk<OnVehicleDeathReceiverDelegate>()
        private val onPlayerEnterVehicleReceiver = mockk<OnPlayerEnterVehicleReceiverDelegate>()
        private val onPlayerExitVehicleReceiver = mockk<OnPlayerExitVehicleReceiverDelegate>()
        private val onVehicleStreamInReceiver = mockk<OnVehicleStreamInReceiverDelegate>()
        private val onVehicleStreamOutReceiver = mockk<OnVehicleStreamOutReceiverDelegate>()

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
                    vehicleRegistry = vehicleRegistry,
                    onVehicleSpawnReceiver = onVehicleSpawnReceiver,
                    onVehicleDeathReceiver = onVehicleDeathReceiver,
                    onPlayerEnterVehicleReceiver = onPlayerEnterVehicleReceiver,
                    onPlayerExitVehicleReceiver = onPlayerExitVehicleReceiver,
                    onVehicleStreamInReceiver = onVehicleStreamInReceiver,
                    onVehicleStreamOutReceiver = onVehicleStreamOutReceiver
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
                    nativeFunctionExecutor.changeVehiclePaintjob(
                            vehicleid = vehicleId.value,
                            paintjobid = paintjob!!
                    )
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
                    nativeFunctionExecutor.setVehicleNumberPlate(
                            vehicleid = vehicleId.value,
                            numberplate = "ABC123"
                    )
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

        @Test
        fun shouldSetAngularVelocity() {
            every { nativeFunctionExecutor.setVehicleAngularVelocity(any(), any(), any(), any()) } returns true

            vehicle.setAngularVelocity(vector3DOf(x = 1f, y = 2f, z = 3f))

            verify {
                nativeFunctionExecutor.setVehicleAngularVelocity(
                        vehicleid = vehicleId.value,
                        X = 1f,
                        Y = 2f,
                        Z = 3f
                )
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
        inner class OnSpawnTests {

            @BeforeEach
            fun setUp() {
                every { onVehicleSpawnReceiver.onVehicleSpawn(any()) } just Runs
            }

            @Test
            fun shouldCallOnVehicleSpawnReceiverDelegate() {
                vehicle.onSpawn()

                verify { onVehicleSpawnReceiver.onVehicleSpawn(vehicle) }
            }

            @Test
            fun shouldResetColorsToInitialColors() {
                every { nativeFunctionExecutor.changeVehicleColor(any(), any(), any()) } returns true
                vehicle.colors = vehicleColorsOf(69, 187)

                vehicle.onSpawn()

                assertThat(vehicle.colors)
                        .isEqualTo(vehicleColorsOf(3, 6))
            }

        }

        @Test
        fun shouldCallOnVehicleDeathReceiverDelegate() {
            val killer = mockk<Player>()
            every { onVehicleDeathReceiver.onVehicleDeath(any(), any()) } just Runs

            vehicle.onDeath(killer)

            verify { onVehicleDeathReceiver.onVehicleDeath(vehicle, killer) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCallOnPlayerEnterVehicleReceiverDelegate(isPassenger: Boolean) {
            val player = mockk<Player>()
            every { onPlayerEnterVehicleReceiver.onPlayerEnterVehicle(any(), any(), any()) } just Runs

            vehicle.onEnter(player, isPassenger)

            verify { onPlayerEnterVehicleReceiver.onPlayerEnterVehicle(player, vehicle, isPassenger) }
        }

        @Test
        fun shouldCallOnPlayerExitVehicleReceiverDelegate() {
            val player = mockk<Player>()
            every { onPlayerExitVehicleReceiver.onPlayerExitVehicle(any(), any()) } just Runs

            vehicle.onExit(player)

            verify { onPlayerExitVehicleReceiver.onPlayerExitVehicle(player, vehicle) }
        }

        @Test
        fun shouldCallOnVehicleStreamInReceiverDelegate() {
            val player = mockk<Player>()
            every { onVehicleStreamInReceiver.onVehicleStreamIn(any(), any()) } just Runs

            vehicle.onStreamIn(player)

            verify { onVehicleStreamInReceiver.onVehicleStreamIn(vehicle, player) }
        }

        @Test
        fun shouldCallOnVehicleStreamOutReceiverDelegate() {
            val player = mockk<Player>()
            every { onVehicleStreamOutReceiver.onVehicleStreamOut(any(), any()) } just Runs

            vehicle.onStreamOut(player)

            verify { onVehicleStreamOutReceiver.onVehicleStreamOut(vehicle, player) }
        }
    }
}