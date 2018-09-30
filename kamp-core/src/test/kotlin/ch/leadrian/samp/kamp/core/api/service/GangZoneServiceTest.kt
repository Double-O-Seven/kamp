package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.GangZoneFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.GangZoneRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GangZoneServiceTest {

    private lateinit var gangZoneService: GangZoneService

    private val gangZoneFactory = mockk<GangZoneFactory>()
    private val gangZoneRegistry = mockk<GangZoneRegistry>()

    @BeforeEach
    fun setUp() {
        gangZoneService = GangZoneService(gangZoneFactory, gangZoneRegistry)
    }

    @Test
    fun shouldCreateGangZone() {
        val area = rectangleOf(1f, 2f, 3f, 4f)
        val expectedGangZone = mockk<GangZone>()
        every { gangZoneFactory.create(area) } returns expectedGangZone

        val gangZone = gangZoneService.createGangZone(area)

        assertThat(gangZone)
                .isEqualTo(expectedGangZone)
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoGangZoneForGangZoneIdItShouldReturnFalse() {
            val gangZoneId = GangZoneId.valueOf(69)
            every { gangZoneRegistry[gangZoneId] } returns null

            val isValid = gangZoneService.isValid(gangZoneId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenGangZoneForGangZoneIdExistsItShouldReturnTrue() {
            val gangZoneId = GangZoneId.valueOf(69)
            val gangZone = mockk<GangZone>()
            every { gangZoneRegistry[gangZoneId] } returns gangZone

            val isValid = gangZoneService.isValid(gangZoneId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetGangZoneTests {

        @Test
        fun givenNoGangZoneForGangZoneIdItShouldThrowException() {
            val gangZoneId = GangZoneId.valueOf(69)
            every { gangZoneRegistry[gangZoneId] } returns null

            val caughtThrowable = catchThrowable { gangZoneService.getGangZone(gangZoneId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No gang zone with ID 69")
        }

        @Test
        fun givenGangZoneForGangZoneIdExistsItShouldReturnGangZone() {
            val gangZoneId = GangZoneId.valueOf(69)
            val expectedGangZone = mockk<GangZone>()
            every { gangZoneRegistry[gangZoneId] } returns expectedGangZone

            val gangZone = gangZoneService.getGangZone(gangZoneId)

            assertThat(gangZone)
                    .isEqualTo(expectedGangZone)
        }
    }

    @Test
    fun shouldReturnAllGangZones() {
        val gangZone1 = mockk<GangZone>()
        val gangZone2 = mockk<GangZone>()
        every { gangZoneRegistry.getAll() } returns listOf(gangZone1, gangZone2)

        val gangZones = gangZoneService.getAllGangZones()

        assertThat(gangZones)
                .containsExactly(gangZone1, gangZone2)
    }

}