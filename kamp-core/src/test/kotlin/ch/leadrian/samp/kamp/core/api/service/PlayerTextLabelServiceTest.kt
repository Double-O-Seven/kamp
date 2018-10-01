package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerTextLabelFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextLabelRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class PlayerTextLabelServiceTest {

    private lateinit var playerTextLabelService: PlayerTextLabelService

    private val playerTextLabelFactory = mockk<PlayerTextLabelFactory>()
    private val playerTextLabelRegistry = mockk<PlayerTextLabelRegistry>()
    private val textProvider = mockk<TextProvider>()
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        every { player.playerTextLabelRegistry } returns playerTextLabelRegistry
        playerTextLabelService = PlayerTextLabelService(playerTextLabelFactory, textProvider)
    }

    @Nested
    inner class CreatePlayerTextLabelTests {

        @Test
        fun shouldCreatePlayerTextLabelWithString() {
            val attachToPlayer = mockk<Player>()
            val attachToVehicle = mockk<Vehicle>()
            val playerTextLabel = mockk<PlayerTextLabel>()
            every {
                playerTextLabelFactory.create(
                        player = player,
                        text = "Hi there",
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 4f,
                        color = Colors.RED,
                        testLOS = true,
                        attachToPlayer = attachToPlayer,
                        attachToVehicle = attachToVehicle
                )
            } returns playerTextLabel

            val createdPlayerTextLabel = playerTextLabelService.createPlayerTextLabel(
                    player = player,
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    drawDistance = 4f,
                    testLOS = true,
                    attachedToPlayer = attachToPlayer,
                    attachedToVehicle = attachToVehicle
            )

            assertThat(createdPlayerTextLabel)
                    .isEqualTo(playerTextLabel)
        }

        @Test
        fun shouldCreatePlayerTextLabelWithTextKey() {
            val textKey = TextKey("text.label")
            val locale = Locale.GERMANY
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            val attachToPlayer = mockk<Player>()
            val attachToVehicle = mockk<Vehicle>()
            val playerTextLabel = mockk<PlayerTextLabel>()
            every {
                playerTextLabelFactory.create(
                        player = player,
                        text = "Hi there",
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 4f,
                        color = Colors.RED,
                        testLOS = true,
                        attachToPlayer = attachToPlayer,
                        attachToVehicle = attachToVehicle
                )
            } returns playerTextLabel

            val createdPlayerTextLabel = playerTextLabelService.createPlayerTextLabel(
                    player = player,
                    textKey = textKey,
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    drawDistance = 4f,
                    testLOS = true,
                    attachedToPlayer = attachToPlayer,
                    attachedToVehicle = attachToVehicle,
                    locale = locale
            )

            assertThat(createdPlayerTextLabel)
                    .isEqualTo(playerTextLabel)
        }
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoPlayerTextLabelForPlayerTextLabelIdItShouldReturnFalse() {
            val playerTextLabelId = PlayerTextLabelId.valueOf(69)
            every { playerTextLabelRegistry[playerTextLabelId] } returns null

            val isValid = playerTextLabelService.isValidPlayerTextLabel(player, playerTextLabelId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenPlayerTextLabelForPlayerTextLabelIdExistsItShouldReturnTrue() {
            val playerTextLabelId = PlayerTextLabelId.valueOf(69)
            val playerTextLabel = mockk<PlayerTextLabel>()
            every { playerTextLabelRegistry[playerTextLabelId] } returns playerTextLabel

            val isValid = playerTextLabelService.isValidPlayerTextLabel(player, playerTextLabelId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetPlayerTextLabelTests {

        @Test
        fun givenPlayerTextLabelIdIsValidItShouldReturnPlayerTextLabel() {
            val playerTextLabelId = PlayerTextLabelId.valueOf(1337)
            val expectedPlayerTextLabel = mockk<PlayerTextLabel>()
            every { playerTextLabelRegistry[playerTextLabelId] } returns expectedPlayerTextLabel

            val playerTextLabel = playerTextLabelService.getPlayerTextLabel(player, playerTextLabelId)

            assertThat(playerTextLabel)
                    .isEqualTo(expectedPlayerTextLabel)
        }

        @Test
        fun givenInvalidPlayerTextLabelIdItShouldThrowException() {
            every { player.id } returns PlayerId.valueOf(69)
            val playerTextLabelId = PlayerTextLabelId.valueOf(1337)
            every { playerTextLabelRegistry[playerTextLabelId] } returns null

            val caughtThrowable = catchThrowable {
                playerTextLabelService.getPlayerTextLabel(player, playerTextLabelId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No player text label for player ID 69 and ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPlayerTextLabels() {
        val playerTextLabel1 = mockk<PlayerTextLabel>()
        val playerTextLabel2 = mockk<PlayerTextLabel>()
        every { playerTextLabelRegistry.getAll() } returns listOf(playerTextLabel1, playerTextLabel2)

        val playerTextLabels = playerTextLabelService.getAllPlayerTextLabels(player)

        assertThat(playerTextLabels)
                .containsExactly(playerTextLabel1, playerTextLabel2)
    }
}