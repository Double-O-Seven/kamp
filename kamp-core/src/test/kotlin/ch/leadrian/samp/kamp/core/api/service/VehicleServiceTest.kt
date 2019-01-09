package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleModelInfoType
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.VehicleFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VehicleServiceTest {

    private lateinit var vehicleService: VehicleService

    private val vehicleFactory = mockk<VehicleFactory>()
    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        vehicleService = VehicleService(vehicleFactory, vehicleRegistry, nativeFunctionExecutor)
    }

    @Nested
    inner class CreateVehicleTests {

        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { vehicleFactory.create(any(), any(), any(), any(), any(), any()) } returns vehicle
        }

        @Test
        fun shouldCreateVehicleWithPosition() {
            val createdVehicle = vehicleService.createVehicle(
                    model = VehicleModel.FBIRANCHER,
                    colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                    position = positionOf(1f, 2f, 3f, 4f),
                    respawnDelay = 60,
                    addSiren = true
            )

            assertThat(createdVehicle)
                    .isEqualTo(vehicle)
            verify {
                vehicleFactory.create(
                        model = VehicleModel.FBIRANCHER,
                        colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                        coordinates = positionOf(1f, 2f, 3f, 4f),
                        rotation = 4f,
                        respawnDelay = 60,
                        addSiren = true
                )
            }
        }

        @Test
        fun shouldCreateVehicleWithCoordinatesAndAngle() {
            val createdVehicle = vehicleService.createVehicle(
                    model = VehicleModel.FBIRANCHER,
                    colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = 4f,
                    respawnDelay = 60,
                    addSiren = true
            )

            assertThat(createdVehicle)
                    .isEqualTo(vehicle)
            verify {
                vehicleFactory.create(
                        model = VehicleModel.FBIRANCHER,
                        colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = 4f,
                        respawnDelay = 60,
                        addSiren = true
                )
            }
        }

        @Test
        fun shouldCreateVehicleWithAngledLocation() {
            every { vehicle.virtualWorldId = any() } just Runs
            every { vehicle.interiorId = any() } just Runs

            val createdVehicle = vehicleService.createVehicle(
                    model = VehicleModel.FBIRANCHER,
                    colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                    angledLocation = angledLocationOf(
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            interiorId = 187,
                            worldId = 69,
                            angle = 4f
                    ),
                    respawnDelay = 60,
                    addSiren = true
            )

            assertThat(createdVehicle)
                    .isEqualTo(vehicle)
            verify {
                vehicleFactory.create(
                        model = VehicleModel.FBIRANCHER,
                        colors = vehicleColorsOf(VehicleColor[3], VehicleColor[6]),
                        coordinates = angledLocationOf(
                                x = 1f,
                                y = 2f,
                                z = 3f,
                                interiorId = 187,
                                worldId = 69,
                                angle = 4f
                        ),
                        rotation = 4f,
                        respawnDelay = 60,
                        addSiren = true
                )
                vehicle.interiorId = 187
                vehicle.virtualWorldId = 69
            }
        }

    }

    @Test
    fun shouldUseManualVehicleLightsAndEngine() {
        every { nativeFunctionExecutor.manualVehicleEngineAndLights() } returns true

        vehicleService.useManualVehicleLightsAndEngine()

        verify { nativeFunctionExecutor.manualVehicleEngineAndLights() }
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoVehicleForVehicleIdItShouldReturnFalse() {
            val vehicleId = VehicleId.valueOf(69)
            every { vehicleRegistry[vehicleId] } returns null

            val isValid = vehicleService.isValidVehicle(vehicleId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenVehicleForVehicleIdExistsItShouldReturnTrue() {
            val vehicleId = VehicleId.valueOf(69)
            val vehicle = mockk<Vehicle>()
            every { vehicleRegistry[vehicleId] } returns vehicle

            val isValid = vehicleService.isValidVehicle(vehicleId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetVehicleTests {

        @Test
        fun givenVehicleIdIsValidItShouldReturnVehicle() {
            val vehicleId = VehicleId.valueOf(1337)
            val expectedVehicle = mockk<Vehicle>()
            every { vehicleRegistry[vehicleId] } returns expectedVehicle

            val vehicle = vehicleService.getVehicle(vehicleId)

            assertThat(vehicle)
                    .isEqualTo(expectedVehicle)
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowException() {
            val vehicleId = VehicleId.valueOf(1337)
            every { vehicleRegistry[vehicleId] } returns null

            val caughtThrowable = catchThrowable {
                vehicleService.getVehicle(vehicleId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No vehicle with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllVehicles() {
        val vehicle1 = mockk<Vehicle>()
        val vehicle2 = mockk<Vehicle>()
        every { vehicleRegistry.getAll() } returns listOf(vehicle1, vehicle2)

        val vehicles = vehicleService.getAllVehicles()

        assertThat(vehicles)
                .containsExactly(vehicle1, vehicle2)
    }

    @Test
    fun shouldReturnPoolSize() {
        every { nativeFunctionExecutor.getVehiclePoolSize() } returns 500

        val poolSize = vehicleService.getPoolSize()

        assertThat(poolSize)
                .isEqualTo(500)
    }

    @Test
    fun shouldEnableTirePopping() {
        every { nativeFunctionExecutor.enableTirePopping(any()) } returns true

        vehicleService.enableTirePopping()

        verify { nativeFunctionExecutor.enableTirePopping(true) }
    }

    @Test
    fun shouldDisableTirePopping() {
        every { nativeFunctionExecutor.enableTirePopping(any()) } returns true

        vehicleService.disableTirePopping()

        verify { nativeFunctionExecutor.enableTirePopping(false) }
    }

    @Test
    fun shouldEnableFriendlyFire() {
        every { nativeFunctionExecutor.enableVehicleFriendlyFire() } returns true

        vehicleService.enableFriendlyFire()

        verify { nativeFunctionExecutor.enableVehicleFriendlyFire() }
    }

    @Nested
    inner class GetModelInfoTests {

        @Test
        fun shouldReturnModelInfo() {
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.FBIRANCHER.value,
                                VehicleModelInfoType.PETROLCAP.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 1f
                arg<ReferenceFloat>(3).value = 2f
                arg<ReferenceFloat>(4).value = 3f
                true
            }

            val modelInfo = vehicleService.getModelInfo(VehicleModel.FBIRANCHER, VehicleModelInfoType.PETROLCAP)

            assertThat(modelInfo)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun givenSameModelAndDifferentTypeItShouldReturnDifferentResults() {
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.FBIRANCHER.value,
                                VehicleModelInfoType.PETROLCAP.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 1f
                arg<ReferenceFloat>(3).value = 2f
                arg<ReferenceFloat>(4).value = 3f
                true
            }
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.FBIRANCHER.value,
                                VehicleModelInfoType.FRONTSEAT.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 4f
                arg<ReferenceFloat>(3).value = 5f
                arg<ReferenceFloat>(4).value = 6f
                true
            }
            vehicleService.getModelInfo(VehicleModel.FBIRANCHER, VehicleModelInfoType.PETROLCAP)

            val modelInfo = vehicleService.getModelInfo(VehicleModel.FBIRANCHER, VehicleModelInfoType.FRONTSEAT)

            assertThat(modelInfo)
                    .isEqualTo(vector3DOf(x = 4f, y = 5f, z = 6f))
        }

        @Test
        fun givenDifferentModelAndSameTypeItShouldReturnDifferentResults() {
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.FBIRANCHER.value,
                                VehicleModelInfoType.PETROLCAP.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 1f
                arg<ReferenceFloat>(3).value = 2f
                arg<ReferenceFloat>(4).value = 3f
                true
            }
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.BULLET.value,
                                VehicleModelInfoType.PETROLCAP.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 4f
                arg<ReferenceFloat>(3).value = 5f
                arg<ReferenceFloat>(4).value = 6f
                true
            }
            vehicleService.getModelInfo(VehicleModel.FBIRANCHER, VehicleModelInfoType.PETROLCAP)

            val modelInfo = vehicleService.getModelInfo(VehicleModel.BULLET, VehicleModelInfoType.PETROLCAP)

            assertThat(modelInfo)
                    .isEqualTo(vector3DOf(x = 4f, y = 5f, z = 6f))
        }

        @Test
        fun givenDifferentModelAndDifferentTypeItShouldReturnDifferentResults() {
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.FBIRANCHER.value,
                                VehicleModelInfoType.PETROLCAP.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 1f
                arg<ReferenceFloat>(3).value = 2f
                arg<ReferenceFloat>(4).value = 3f
                true
            }
            every {
                nativeFunctionExecutor
                        .getVehicleModelInfo(
                                VehicleModel.BULLET.value,
                                VehicleModelInfoType.FRONTSEAT.value,
                                any(),
                                any(),
                                any()
                        )
            } answers {
                thirdArg<ReferenceFloat>().value = 4f
                arg<ReferenceFloat>(3).value = 5f
                arg<ReferenceFloat>(4).value = 6f
                true
            }
            vehicleService.getModelInfo(VehicleModel.FBIRANCHER, VehicleModelInfoType.PETROLCAP)

            val modelInfo = vehicleService.getModelInfo(VehicleModel.BULLET, VehicleModelInfoType.FRONTSEAT)

            assertThat(modelInfo)
                    .isEqualTo(vector3DOf(x = 4f, y = 5f, z = 6f))
        }
    }

}