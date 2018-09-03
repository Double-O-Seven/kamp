package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.GangZone
import ch.leadrian.samp.kamp.api.entity.id.GangZoneId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GangZoneRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_GANG_ZONES - 1])
    fun shouldRegisterAndGetGangZone(gangZoneId: Int) {
        val gangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val gangZoneRegistry = GangZoneRegistry()

        gangZoneRegistry.register(gangZone)

        val registeredGangZone = gangZoneRegistry[gangZoneId]
        assertThat(registeredGangZone)
                .isSameAs(gangZone)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_GANG_ZONES, SAMPConstants.MAX_GANG_ZONES + 1])
    fun givenUnknownGangZoneIdGetGangZoneShouldReturn(gangZoneId: Int) {
        val gangZoneRegistry = GangZoneRegistry()

        val registeredGangZone = gangZoneRegistry[gangZoneId]
        assertThat(registeredGangZone)
                .isNull()
    }

    @Test
    fun givenAnotherGangZoneWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val gangZoneId = 50
        val alreadyRegisteredGangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val newGangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val gangZoneRegistry = GangZoneRegistry()
        gangZoneRegistry.register(alreadyRegisteredGangZone)

        val caughtThrowable = catchThrowable { gangZoneRegistry.register(newGangZone) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredGangZone = gangZoneRegistry[gangZoneId]
        assertThat(registeredGangZone)
                .isSameAs(alreadyRegisteredGangZone)
    }

    @Test
    fun shouldUnregisterRegisteredGangZone() {
        val gangZoneId = 50
        val gangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val gangZoneRegistry = GangZoneRegistry()
        gangZoneRegistry.register(gangZone)

        gangZoneRegistry.unregister(gangZone)

        val registeredGangZone = gangZoneRegistry[gangZoneId]
        assertThat(registeredGangZone)
                .isNull()
    }


    @Test
    fun givenGangZoneIsNotRegisteredItShouldThrowAnException() {
        val gangZoneId = GangZoneId.valueOf(50)
        val gangZone = mockk<GangZone> {
            every { id } returns gangZoneId
        }
        val gangZoneRegistry = GangZoneRegistry()

        val caughtThrowable = catchThrowable { gangZoneRegistry.unregister(gangZone) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherGangZoneWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val gangZoneId = 50
        val alreadyRegisteredGangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val newGangZone = mockk<GangZone> {
            every { id } returns GangZoneId.valueOf(gangZoneId)
        }
        val gangZoneRegistry = GangZoneRegistry()
        gangZoneRegistry.register(alreadyRegisteredGangZone)

        val caughtThrowable = catchThrowable { gangZoneRegistry.unregister(newGangZone) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredGangZone = gangZoneRegistry[gangZoneId]
        assertThat(registeredGangZone)
                .isSameAs(alreadyRegisteredGangZone)
    }

    @Test
    fun shouldReturnAllRegisteredGangZones() {
        val gangZoneId1 = GangZoneId.valueOf(10)
        val gangZone1 = mockk<GangZone> {
            every { id } returns gangZoneId1
        }
        val gangZoneId2 = GangZoneId.valueOf(15)
        val gangZone2 = mockk<GangZone> {
            every { id } returns gangZoneId2
        }
        val gangZoneId3 = GangZoneId.valueOf(30)
        val gangZone3 = mockk<GangZone> {
            every { id } returns gangZoneId3
        }
        val gangZoneRegistry = GangZoneRegistry()
        gangZoneRegistry.register(gangZone1)
        gangZoneRegistry.register(gangZone2)
        gangZoneRegistry.register(gangZone3)

        val allGangZones = gangZoneRegistry.getAll()

        assertThat(allGangZones)
                .containsExactly(gangZone1, gangZone2, gangZone3)
    }

}