package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.constants.VehicleColor
import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VehicleImplTest {

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

            val vehicle = VehicleImpl(
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
                VehicleImpl(
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
        private lateinit var vehicle: VehicleImpl

        private val vehicleRegistry = mockk<VehicleRegistry>()
        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns vehicleId.value
            vehicle = VehicleImpl(
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
    }
}