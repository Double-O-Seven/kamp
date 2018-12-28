package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class VehicleFactoryTest {

    private val vehicleId = 123
    private lateinit var vehicleFactory: VehicleFactory

    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { vehicleRegistry.register(any()) } just Runs
        every { nativeFunctionExecutor.createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns vehicleId
        vehicleFactory = VehicleFactory(
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
    }

    @Test
    fun shouldCreateVehicle() {
        vehicleFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[3], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        verify {
            nativeFunctionExecutor.createVehicle(
                    vehicletype = ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    rotation = 4f,
                    color1 = 3,
                    color2 = 6,
                    respawn_delay = 60,
                    addsiren = true
            )
        }
    }

    @Test
    fun shouldReturnVehicleImpl() {
        val vehicle = vehicleFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[3], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        assertThat(vehicle.id)
                .isEqualTo(VehicleId.valueOf(vehicleId))
    }

    @Test
    fun shouldRegisterVehicle() {
        val vehicle = vehicleFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[3], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        verify { vehicleRegistry.register(vehicle) }
    }

    @Test
    fun destroyShouldUnregisterVehicle() {
        every { vehicleRegistry.unregister(any()) } just Runs
        every { nativeFunctionExecutor.destroyVehicle(any()) } returns true
        val vehicle = vehicleFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[3], color2 = ch.leadrian.samp.kamp.core.api.constants.VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        vehicle.destroy()

        verify { vehicleRegistry.unregister(vehicle) }
    }
}