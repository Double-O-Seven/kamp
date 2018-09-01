package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.constants.VehicleColor
import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.InterceptableVehicle
import ch.leadrian.samp.kamp.runtime.entity.VehicleImpl
import ch.leadrian.samp.kamp.runtime.entity.interceptor.InterceptorPriority
import ch.leadrian.samp.kamp.runtime.entity.interceptor.VehicleInterceptor
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleFactoryTest {

    @Test
    fun shouldCreateVehicle() {
        val vehicleId = 123
        val vehicleRegistry = mockk<VehicleRegistry>()
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns vehicleId
        }
        val vehicleFactory = VehicleFactory(
                interceptors = emptySet(),
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        vehicleFactory.create(
                model = VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        verify {
            nativeFunctionExecutor.createVehicle(
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
        }
    }

    @Test
    fun givenNoInterceptorItShouldReturnVehicleImpl() {
        val vehicleId = 123
        val vehicleRegistry = mockk<VehicleRegistry>()
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns vehicleId
        }
        val vehicleFactory = VehicleFactory(
                interceptors = emptySet(),
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val vehicle = vehicleFactory.create(
                model = VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        assertThat(vehicle)
                .isInstanceOfSatisfying(VehicleImpl::class.java) {
                    assertThat(it.id)
                            .isEqualTo(VehicleId.valueOf(vehicleId))
                }
    }

    @Test
    fun givenInterceptorsItShouldReturnInterceptableVehicle() {
        val vehicleId = 123
        val vehicleRegistry = mockk<VehicleRegistry>()
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { createVehicle(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns vehicleId
        }
        val interceptors = setOf(
                DefaultPriorityInterceptor("default"),
                HighPriorityInterceptor("high"),
                LowPriorityInterceptor("low")
        )
        val vehicleFactory = VehicleFactory(
                interceptors = interceptors,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val vehicle = vehicleFactory.create(
                model = VehicleModel.ALPHA,
                colors = vehicleColorsOf(color1 = VehicleColor[3], color2 = VehicleColor[6]),
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f,
                addSiren = true,
                respawnDelay = 60
        )

        assertThat(vehicle)
                .isInstanceOfSatisfying(VehicleFactoryTest.VehicleWrapper::class.java) {
                    assertThat(it.interceptorName)
                            .isEqualTo("low")
                    assertThat(it.vehicle)
                            .isInstanceOfSatisfying(VehicleFactoryTest.VehicleWrapper::class.java) {
                                assertThat(it.interceptorName)
                                        .isEqualTo("default")
                                assertThat(it.vehicle)
                                        .isInstanceOfSatisfying(VehicleFactoryTest.VehicleWrapper::class.java) {
                                            assertThat(it.interceptorName)
                                                    .isEqualTo("high")
                                            assertThat(it.vehicle)
                                                    .isInstanceOfSatisfying(VehicleImpl::class.java) {
                                                        assertThat(it.id)
                                                                .isEqualTo(VehicleId.valueOf(vehicleId))
                                                    }
                                        }
                            }
                }
    }

    @InterceptorPriority(-5)
    private class LowPriorityInterceptor(val name: String) : VehicleInterceptor {

        override fun intercept(interceptableVehicle: InterceptableVehicle): InterceptableVehicle {
            return VehicleWrapper(name, interceptableVehicle)
        }

    }

    @InterceptorPriority(10)
    private class HighPriorityInterceptor(val name: String) : VehicleInterceptor {

        override fun intercept(interceptableVehicle: InterceptableVehicle): InterceptableVehicle {
            return VehicleWrapper(name, interceptableVehicle)
        }

    }

    private class DefaultPriorityInterceptor(val name: String) : VehicleInterceptor {

        override fun intercept(interceptableVehicle: InterceptableVehicle): InterceptableVehicle {
            return VehicleWrapper(name, interceptableVehicle)
        }

    }

    private class VehicleWrapper(val interceptorName: String, val vehicle: InterceptableVehicle) : InterceptableVehicle by vehicle
}