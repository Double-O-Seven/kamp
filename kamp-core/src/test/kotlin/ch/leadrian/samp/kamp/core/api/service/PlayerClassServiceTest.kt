package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.spawnInfoOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerClassFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerClassServiceTest {

    private lateinit var playerClassService: PlayerClassService

    private val playerClassFactory = mockk<PlayerClassFactory>()
    private val playerClassRegistry = mockk<PlayerClassRegistry>()

    @BeforeEach
    fun setUp() {
        playerClassService = PlayerClassService(playerClassFactory, playerClassRegistry)
    }

    @Nested
    inner class AddPlayerClassTests {

        private val playerClass = mockk<PlayerClass>()

        @BeforeEach
        fun setUp() {
            every { playerClassFactory.create(any()) } returns playerClass
        }

        @Test
        fun shouldAddPlayerClassWithSpawnInfo() {
            val spawnInfo = spawnInfoOf(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weaponDataOf(WeaponModel.AK47, 50),
                    weapon2 = weaponDataOf(WeaponModel.M4, 100),
                    weapon3 = weaponDataOf(WeaponModel.TEC9, 180)
            )
            every { playerClassFactory.create(spawnInfo) } returns playerClass

            val createdPlayerClass = playerClassService.addPlayerClass(spawnInfo)

            assertThat(createdPlayerClass)
                    .isEqualTo(playerClass)
        }

        @Test
        fun shouldAddPlayerClassWithCoordinatesAndAngle() {
            val weapon1 = weaponDataOf(WeaponModel.AK47, 50)
            val weapon2 = weaponDataOf(WeaponModel.M4, 100)
            val weapon3 = weaponDataOf(WeaponModel.TEC9, 180)
            val spawnInfo = spawnInfoOf(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )
            every { playerClassFactory.create(spawnInfo) } returns playerClass

            val createdPlayerClass = playerClassService.addPlayerClass(
                    skinModel = SkinModel.ARMY,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    angle = 4f,
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )

            assertThat(createdPlayerClass)
                    .isEqualTo(playerClass)
        }

        @Test
        fun shouldAddPlayerClassWithPosition() {
            val weapon1 = weaponDataOf(WeaponModel.AK47, 50)
            val weapon2 = weaponDataOf(WeaponModel.M4, 100)
            val weapon3 = weaponDataOf(WeaponModel.TEC9, 180)
            val spawnInfo = spawnInfoOf(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )
            every { playerClassFactory.create(spawnInfo) } returns playerClass

            val createdPlayerClass = playerClassService.addPlayerClass(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )

            assertThat(createdPlayerClass)
                    .isEqualTo(playerClass)
        }

        @Test
        fun shouldAddPlayerClassWithCoordinatesAndAngleAndTeamId() {
            val teamId = TeamId.valueOf(69)
            val weapon1 = weaponDataOf(WeaponModel.AK47, 50)
            val weapon2 = weaponDataOf(WeaponModel.M4, 100)
            val weapon3 = weaponDataOf(WeaponModel.TEC9, 180)
            val spawnInfo = spawnInfoOf(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )
            every { playerClassFactory.create(spawnInfo) } returns playerClass

            val createdPlayerClass = playerClassService.addPlayerClass(
                    skinModel = SkinModel.ARMY,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    angle = 4f,
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )

            assertThat(createdPlayerClass)
                    .isEqualTo(playerClass)
        }

        @Test
        fun shouldAddPlayerClassWithPositionAndTeamId() {
            val teamId = TeamId.valueOf(69)
            val weapon1 = weaponDataOf(WeaponModel.AK47, 50)
            val weapon2 = weaponDataOf(WeaponModel.M4, 100)
            val weapon3 = weaponDataOf(WeaponModel.TEC9, 180)
            val spawnInfo = spawnInfoOf(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )
            every { playerClassFactory.create(spawnInfo) } returns playerClass

            val createdPlayerClass = playerClassService.addPlayerClass(
                    skinModel = SkinModel.ARMY,
                    position = positionOf(1f, 2f, 3f, 4f),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )

            assertThat(createdPlayerClass)
                    .isEqualTo(playerClass)
        }

    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoPlayerClassForPlayerClassIdItShouldReturnFalse() {
            val playerClassId = PlayerClassId.valueOf(69)
            every { playerClassRegistry[playerClassId] } returns null

            val isValid = playerClassService.isValidPlayerClass(playerClassId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenPlayerClassForPlayerClassIdExistsItShouldReturnTrue() {
            val playerClassId = PlayerClassId.valueOf(69)
            val playerClass = mockk<PlayerClass>()
            every { playerClassRegistry[playerClassId] } returns playerClass

            val isValid = playerClassService.isValidPlayerClass(playerClassId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetPlayerClassTests {

        @Test
        fun givenPlayerClassIdIsValidItShouldReturnPlayerClass() {
            val playerClassId = PlayerClassId.valueOf(1337)
            val expectedPlayerClass = mockk<PlayerClass>()
            every { playerClassRegistry[playerClassId] } returns expectedPlayerClass

            val playerClass = playerClassService.getPlayerClass(playerClassId)

            assertThat(playerClass)
                    .isEqualTo(expectedPlayerClass)
        }

        @Test
        fun givenInvalidPlayerClassIdItShouldThrowException() {
            val playerClassId = PlayerClassId.valueOf(1337)
            every { playerClassRegistry[playerClassId] } returns null

            val caughtThrowable = catchThrowable { playerClassService.getPlayerClass(playerClassId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No player class with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPlayerClasss() {
        val playerClass1 = mockk<PlayerClass>()
        val playerClass2 = mockk<PlayerClass>()
        every { playerClassRegistry.getAll() } returns listOf(playerClass1, playerClass2)

        val playerClasses = playerClassService.getAllPlayerClasses()

        assertThat(playerClasses)
                .containsExactly(playerClass1, playerClass2)
    }

}