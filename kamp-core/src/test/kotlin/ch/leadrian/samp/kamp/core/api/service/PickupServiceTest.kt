package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PickupFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PickupServiceTest {

    private lateinit var pickupService: PickupService

    private val pickupFactory = mockk<PickupFactory>()
    private val pickupRegistry = mockk<PickupRegistry>()

    @BeforeEach
    fun setUp() {
        pickupService = PickupService(pickupFactory, pickupRegistry)
    }

    @Test
    fun shouldCreatePickup() {
        val expectedPickup = mockk<Pickup>()
        every {
            pickupFactory.create(
                    modelId = 1337,
                    type = 69,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    virtualWorldId = 187
            )
        } returns expectedPickup

        val pickup = pickupService.createPickup(
                modelId = 1337,
                type = 69,
                coordinates = vector3DOf(1f, 2f, 3f),
                virtualWorldId = 187
        )

        assertThat(pickup)
                .isEqualTo(expectedPickup)
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoPickupForPickupIdItShouldReturnFalse() {
            val pickupId = PickupId.valueOf(69)
            every { pickupRegistry[pickupId] } returns null

            val isValid = pickupService.isValidPickup(pickupId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenPickupForPickupIdExistsItShouldReturnTrue() {
            val pickupId = PickupId.valueOf(69)
            val pickup = mockk<Pickup>()
            every { pickupRegistry[pickupId] } returns pickup

            val isValid = pickupService.isValidPickup(pickupId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetPickupTests {

        @Test
        fun givenPickupIdIsValidItShouldReturnPickup() {
            val pickupId = PickupId.valueOf(1337)
            val expectedPickup = mockk<Pickup>()
            every { pickupRegistry[pickupId] } returns expectedPickup

            val pickup = pickupService.getPickup(pickupId)

            assertThat(pickup)
                    .isEqualTo(expectedPickup)
        }

        @Test
        fun givenInvalidPickupIdItShouldThrowException() {
            val pickupId = PickupId.valueOf(1337)
            every { pickupRegistry[pickupId] } returns null

            val caughtThrowable = catchThrowable { pickupService.getPickup(pickupId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No pickup with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPickups() {
        val pickup1 = mockk<Pickup>()
        val pickup2 = mockk<Pickup>()
        every { pickupRegistry.getAll() } returns listOf(pickup1, pickup2)

        val pickups = pickupService.getAllPickups()

        assertThat(pickups)
                .containsExactly(pickup1, pickup2)
    }


}