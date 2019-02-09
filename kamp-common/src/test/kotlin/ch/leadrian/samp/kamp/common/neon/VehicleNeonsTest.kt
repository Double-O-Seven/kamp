package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class VehicleNeonsTest {

    private lateinit var vehicleNeons: VehicleNeons
    private val vehicle: Vehicle = mockk()
    private val vehicleNeonMapObjectProvider: VehicleNeonMapObjectProvider = mockk()

    @BeforeEach
    fun setUp() {
        every { vehicle.addOnVehicleSpawnListener(any()) } just Runs
        vehicleNeons = VehicleNeons(vehicle, vehicleNeonMapObjectProvider)
    }

    @Test
    fun shouldRegisterAsOnVehicleSpawnListener() {
        verify { vehicle.addOnVehicleSpawnListener(vehicleNeons) }
    }

    @Nested
    inner class AttachTests {

        @Test
        fun shouldAttachNeons() {
            every { vehicle.model } returns VehicleModel.TURISMO
            val neonColor = NeonColor.PINK
            val neonMapObject1 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            val neonMapObject2 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            every {
                vehicleNeonMapObjectProvider.createNeon(neonColor)
            } returnsMany listOf(neonMapObject1, neonMapObject2)

            vehicleNeons.attach(neonColor)

            verify {
                neonMapObject1.attachTo(
                        vehicle = vehicle,
                        offset = vector3DOf(-0.984999f, -0.194999f, -0.519999f),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
                neonMapObject2.attachTo(
                        vehicle = vehicle,
                        offset = mutableVector3DOf(0.984999f, -0.194999f, -0.519999f),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
            }
        }

        @Test
        fun givenNeonsAreAlreadyAttachedItShouldDestroyOldNeonsAndAttachNewOnes() {
            every { vehicle.model } returns VehicleModel.TURISMO
            val neonMapObject1 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
                every { destroy() } just Runs
            }
            val neonMapObject2 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
                every { destroy() } just Runs
            }
            val neonMapObject3 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            val neonMapObject4 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            every {
                vehicleNeonMapObjectProvider.createNeon(NeonColor.PINK)
            } returnsMany listOf(neonMapObject1, neonMapObject2)
            every {
                vehicleNeonMapObjectProvider.createNeon(NeonColor.RED)
            } returnsMany listOf(neonMapObject3, neonMapObject4)
            vehicleNeons.attach(NeonColor.PINK)

            vehicleNeons.attach(NeonColor.RED)

            verify {
                neonMapObject1.destroy()
                neonMapObject2.destroy()
                neonMapObject3.attachTo(
                        vehicle = vehicle,
                        offset = vector3DOf(-0.984999f, -0.194999f, -0.519999f),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
                neonMapObject4.attachTo(
                        vehicle = vehicle,
                        offset = mutableVector3DOf(0.984999f, -0.194999f, -0.519999f),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
            }
        }

    }

    @Nested
    inner class RemoveTests {

        @Test
        fun givenNeonsHaveBeenAttachedItShouldDestroyThemAndReturnTrue() {
            every { vehicle.model } returns VehicleModel.TURISMO
            val neonMapObject1 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
                every { destroy() } just Runs
            }
            val neonMapObject2 = mockk<MapObjectBase> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
                every { destroy() } just Runs
            }
            every {
                vehicleNeonMapObjectProvider.createNeon(any())
            } returnsMany listOf(neonMapObject1, neonMapObject2)
            vehicleNeons.attach(NeonColor.PINK)

            val removed = vehicleNeons.remove()

            assertAll(
                    {
                        verify {
                            neonMapObject1.destroy()
                            neonMapObject2.destroy()
                        }
                    },
                    { assertThat(removed).isTrue() }
            )
        }

        @Test
        fun givenNoNeonsHaveBeenAttachedItShouldReturnFalse() {
            val removed = vehicleNeons.remove()

            assertThat(removed)
                    .isFalse()
        }

    }

    @Test
    fun givenNeonsHaveBeenAttachedDestroyShouldDestroyThem() {
        every { vehicle.model } returns VehicleModel.TURISMO
        val neonMapObject1 = mockk<MapObjectBase> {
            every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            every { destroy() } just Runs
        }
        val neonMapObject2 = mockk<MapObjectBase> {
            every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            every { destroy() } just Runs
        }
        every {
            vehicleNeonMapObjectProvider.createNeon(any())
        } returnsMany listOf(neonMapObject1, neonMapObject2)
        vehicleNeons.attach(NeonColor.PINK)

        vehicleNeons.destroy()

        verify {
            neonMapObject1.destroy()
            neonMapObject2.destroy()
        }
    }

    @Test
    fun givenNeonsHaveBeenAttachedOnVehicleSpawnShouldAttachThemAgain() {
        every { vehicle.model } returns VehicleModel.TURISMO
        val neonColor = NeonColor.PINK
        val neonMapObject1 = mockk<MapObjectBase> {
            every { attachTo(any<Vehicle>(), any(), any()) } just Runs
        }
        val neonMapObject2 = mockk<MapObjectBase> {
            every { attachTo(any<Vehicle>(), any(), any()) } just Runs
        }
        every {
            vehicleNeonMapObjectProvider.createNeon(neonColor)
        } returnsMany listOf(neonMapObject1, neonMapObject2)
        vehicleNeons.attach(neonColor)
        clearMocks(neonMapObject1, neonMapObject2)
        every { neonMapObject1.attachTo(any<Vehicle>(), any(), any()) } just Runs
        every { neonMapObject2.attachTo(any<Vehicle>(), any(), any()) } just Runs

        vehicleNeons.onVehicleSpawn(vehicle)

        verify {
            neonMapObject1.attachTo(
                    vehicle = vehicle,
                    offset = vector3DOf(-0.984999f, -0.194999f, -0.519999f),
                    rotation = vector3DOf(0f, 0f, 0f)
            )
            neonMapObject2.attachTo(
                    vehicle = vehicle,
                    offset = mutableVector3DOf(0.984999f, -0.194999f, -0.519999f),
                    rotation = vector3DOf(0f, 0f, 0f)
            )
        }
    }

}