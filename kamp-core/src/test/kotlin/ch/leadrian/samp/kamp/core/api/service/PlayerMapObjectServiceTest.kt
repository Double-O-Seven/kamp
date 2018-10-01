package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerMapObjectFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerMapObjectRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerMapObjectServiceTest {

    private lateinit var playerMapObjectService: PlayerMapObjectService

    private val playerMapObjectFactory = mockk<PlayerMapObjectFactory>()
    private val playerMapObjectRegistry = mockk<PlayerMapObjectRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        every { player.playerMapObjectRegistry } returns playerMapObjectRegistry
        every { player.id } returns playerId
        playerMapObjectService = PlayerMapObjectService(playerMapObjectFactory, nativeFunctionExecutor)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isValidShouldReturnExpectedResult(expectedResult: Boolean) {
        val playerMapObjectId = PlayerMapObjectId.valueOf(1337)
        every {
            nativeFunctionExecutor.isValidPlayerObject(playerid = playerId.value, objectid = playerMapObjectId.value)
        } returns expectedResult

        val result = playerMapObjectService.isValidPlayerMapObject(player, playerMapObjectId)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Test
    fun shouldCreatePlayerMapObject() {
        val playerMapObject = mockk<PlayerMapObject>()
        val coordinates = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        every {
            playerMapObjectFactory.create(
                    player = player,
                    modelId = 1337,
                    coordinates = coordinates,
                    rotation = rotation,
                    drawDistance = 7f
            )
        } returns playerMapObject

        val createdPlayerMapObject = playerMapObjectService.createPlayerMapObject(
                player = player,
                modelId = 1337,
                coordinates = coordinates,
                rotation = rotation,
                drawDistance = 7f
        )

        assertThat(createdPlayerMapObject)
                .isEqualTo(playerMapObject)
    }

    @Nested
    inner class GetPlayerMapObjectTests {

        @Test
        fun givenPlayerMapObjectIdIsValidItShouldReturnPlayerMapObject() {
            val playerMapObjectId = PlayerMapObjectId.valueOf(1337)
            val expectedPlayerMapObject = mockk<PlayerMapObject>()
            every { playerMapObjectRegistry[playerMapObjectId] } returns expectedPlayerMapObject

            val playerMapObject = playerMapObjectService.getPlayerMapObject(player, playerMapObjectId)

            assertThat(playerMapObject)
                    .isEqualTo(expectedPlayerMapObject)
        }

        @Test
        fun givenInvalidPlayerMapObjectIdItShouldThrowException() {
            val playerMapObjectId = PlayerMapObjectId.valueOf(1337)
            every { playerMapObjectRegistry[playerMapObjectId] } returns null

            val caughtThrowable = catchThrowable {
                playerMapObjectService.getPlayerMapObject(player, playerMapObjectId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No player map object for player ID 69 and ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPlayerMapObjects() {
        val playerMapObject1 = mockk<PlayerMapObject>()
        val playerMapObject2 = mockk<PlayerMapObject>()
        every { playerMapObjectRegistry.getAll() } returns listOf(playerMapObject1, playerMapObject2)

        val playerMapObjects = playerMapObjectService.getAllPlayerMapObjects(player)

        assertThat(playerMapObjects)
                .containsExactly(playerMapObject1, playerMapObject2)
    }

}