package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleTrailerImplTest {

    private lateinit var vehicleTrailer: VehicleTrailerImpl

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val vehicleRegistry = mockk<VehicleRegistry>()
    private val vehicleId = VehicleId.valueOf(69)
    private val vehicle = mockk<Vehicle> {
        every { id } returns vehicleId
    }
    private val trailerVehicleId = VehicleId.valueOf(69)
    private val trailer = mockk<VehicleImpl> {
        every { id } returns trailerVehicleId
    }

    @BeforeEach
    fun setUp() {
        vehicleTrailer = VehicleTrailerImpl(
                vehicle = vehicle,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
    }

    @Test
    fun shouldAttachTrailerToVehicle() {
        every { nativeFunctionExecutor.attachTrailerToVehicle(any(), any()) } returns true

        vehicleTrailer.attach(trailer)

        verify { nativeFunctionExecutor.attachTrailerToVehicle(trailerid = trailerVehicleId.value, vehicleid = vehicleId.value) }
    }

    @Test
    fun shouldDetachTrailerFromVehicle() {
        every { nativeFunctionExecutor.detachTrailerFromVehicle(any()) } returns true

        vehicleTrailer.detach()

        verify { nativeFunctionExecutor.detachTrailerFromVehicle(vehicleid = vehicleId.value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnWhetherTrailerIsAttachedToVehicle(expectedResult: Boolean) {
        every { nativeFunctionExecutor.isTrailerAttachedToVehicle(vehicleId.value) } returns expectedResult

        val result = vehicleTrailer.isAttached

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class TrailerTests {

        @Test
        fun shouldReturnAttachedTrailer() {
            every { nativeFunctionExecutor.getVehicleTrailer(vehicleId.value) } returns trailerVehicleId.value
            every { vehicleRegistry[trailerVehicleId.value] } returns trailer

            val result = vehicleTrailer.trailer

            assertThat(result)
                    .isEqualTo(trailer)
        }

        @Test
        fun givenGetVehicleTrailerReturnsInvalidVehicleIdItShouldReturnNull() {
            every { nativeFunctionExecutor.getVehicleTrailer(vehicleId.value) } returns SAMPConstants.INVALID_VEHICLE_ID
            every { vehicleRegistry[SAMPConstants.INVALID_VEHICLE_ID] } returns null

            val result = vehicleTrailer.trailer

            assertThat(result)
                    .isNull()
        }
    }
}