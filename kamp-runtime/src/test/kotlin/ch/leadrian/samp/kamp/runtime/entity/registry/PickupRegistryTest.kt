package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PickupId
import ch.leadrian.samp.kamp.runtime.entity.PickupImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PickupRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_PICKUPS - 1])
    fun shouldRegisterAndGetPickup(pickupId: Int) {
        val pickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val pickupRegistry = PickupRegistry()

        pickupRegistry.register(pickup)

        val registeredPickup = pickupRegistry[pickupId]
        assertThat(registeredPickup)
                .isSameAs(pickup)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_PICKUPS, SAMPConstants.MAX_PICKUPS + 1])
    fun givenUnknownPickupIdGetPickupShouldReturn(pickupId: Int) {
        val pickupRegistry = PickupRegistry()

        val registeredPickup = pickupRegistry[pickupId]
        assertThat(registeredPickup)
                .isNull()
    }

    @Test
    fun givenAnotherPickupWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val pickupId = 50
        val alreadyRegisteredPickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val newPickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val pickupRegistry = PickupRegistry()
        pickupRegistry.register(alreadyRegisteredPickup)

        val caughtThrowable = catchThrowable { pickupRegistry.register(newPickup) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPickup = pickupRegistry[pickupId]
        assertThat(registeredPickup)
                .isSameAs(alreadyRegisteredPickup)
    }

    @Test
    fun shouldUnregisterRegisteredPickup() {
        val pickupId = 50
        val pickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val pickupRegistry = PickupRegistry()
        pickupRegistry.register(pickup)

        pickupRegistry.unregister(pickup)

        val registeredPickup = pickupRegistry[pickupId]
        assertThat(registeredPickup)
                .isNull()
    }


    @Test
    fun givenPickupIsNotRegisteredItShouldThrowAnException() {
        val pickupId = PickupId.valueOf(50)
        val pickup = mockk<PickupImpl> {
            every { id } returns pickupId
        }
        val pickupRegistry = PickupRegistry()

        val caughtThrowable = catchThrowable { pickupRegistry.unregister(pickup) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherPickupWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val pickupId = 50
        val alreadyRegisteredPickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val newPickup = mockk<PickupImpl> {
            every { id } returns PickupId.valueOf(pickupId)
        }
        val pickupRegistry = PickupRegistry()
        pickupRegistry.register(alreadyRegisteredPickup)

        val caughtThrowable = catchThrowable { pickupRegistry.unregister(newPickup) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPickup = pickupRegistry[pickupId]
        assertThat(registeredPickup)
                .isSameAs(alreadyRegisteredPickup)
    }

    @Test
    fun shouldReturnAllRegisteredPickups() {
        val pickupId1 = PickupId.valueOf(10)
        val pickup1 = mockk<PickupImpl> {
            every { id } returns pickupId1
        }
        val pickupId2 = PickupId.valueOf(15)
        val pickup2 = mockk<PickupImpl> {
            every { id } returns pickupId2
        }
        val pickupId3 = PickupId.valueOf(30)
        val pickup3 = mockk<PickupImpl> {
            every { id } returns pickupId3
        }
        val pickupRegistry = PickupRegistry()
        pickupRegistry.register(pickup1)
        pickupRegistry.register(pickup2)
        pickupRegistry.register(pickup3)

        val allPickups = pickupRegistry.getAll()

        assertThat(allPickups)
                .containsExactly(pickup1, pickup2, pickup3)
    }

}