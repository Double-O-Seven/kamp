package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerTextDrawFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextDrawRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class PlayerTextDrawServiceTest {

    private lateinit var playerTextDrawService: PlayerTextDrawService

    private val playerTextDrawFactory = mockk<PlayerTextDrawFactory>()
    private val playerTextDrawRegistry = mockk<PlayerTextDrawRegistry>()
    private val textProvider = mockk<TextProvider>()
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        every { player.playerTextDrawRegistry } returns playerTextDrawRegistry
        playerTextDrawService = PlayerTextDrawService(playerTextDrawFactory, textProvider)
    }

    @Nested
    inner class CreatePlayerTextDrawTests {

        @Test
        fun shouldCreatePlayerTextDrawWithString() {
            val playerTextDraw = mockk<PlayerTextDraw>()
            every {
                playerTextDrawFactory.create(
                        player = player,
                        text = "Hi there",
                        position = vector2DOf(1f, 2f)
                )
            } returns playerTextDraw

            val createdPlayerTextDraw = playerTextDrawService.createPlayerTextDraw(
                    player = player,
                    text = "Hi there",
                    position = vector2DOf(1f, 2f)
            )

            assertThat(createdPlayerTextDraw)
                    .isEqualTo(playerTextDraw)
        }

        @Test
        fun shouldCreatePlayerTextDrawWithTextKey() {
            val textKey = TextKey("text.draw")
            val locale = Locale.GERMANY
            every { player.locale } returns locale
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            val playerTextDraw = mockk<PlayerTextDraw>()
            every {
                playerTextDrawFactory.create(
                        player = player,
                        text = "Hi there",
                        position = vector2DOf(1f, 2f)
                )
            } returns playerTextDraw

            val createdPlayerTextDraw = playerTextDrawService.createPlayerTextDraw(
                    player = player,
                    textKey = textKey,
                    position = vector2DOf(1f, 2f)
            )

            assertThat(createdPlayerTextDraw)
                    .isEqualTo(playerTextDraw)
        }
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoPlayerTextDrawForPlayerTextDrawIdItShouldReturnFalse() {
            val playerTextDrawId = PlayerTextDrawId.valueOf(69)
            every { playerTextDrawRegistry[playerTextDrawId] } returns null

            val isValid = playerTextDrawService.isValidPlayerTextDraw(player, playerTextDrawId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenPlayerTextDrawForPlayerTextDrawIdExistsItShouldReturnTrue() {
            val playerTextDrawId = PlayerTextDrawId.valueOf(69)
            val playerTextDraw = mockk<PlayerTextDraw>()
            every { playerTextDrawRegistry[playerTextDrawId] } returns playerTextDraw

            val isValid = playerTextDrawService.isValidPlayerTextDraw(player, playerTextDrawId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetPlayerTextDrawTests {

        @Test
        fun givenPlayerTextDrawIdIsValidItShouldReturnPlayerTextDraw() {
            val playerTextDrawId = PlayerTextDrawId.valueOf(1337)
            val expectedPlayerTextDraw = mockk<PlayerTextDraw>()
            every { playerTextDrawRegistry[playerTextDrawId] } returns expectedPlayerTextDraw

            val playerTextDraw = playerTextDrawService.getPlayerTextDraw(player, playerTextDrawId)

            assertThat(playerTextDraw)
                    .isEqualTo(expectedPlayerTextDraw)
        }

        @Test
        fun givenInvalidPlayerTextDrawIdItShouldThrowException() {
            every { player.id } returns PlayerId.valueOf(69)
            val playerTextDrawId = PlayerTextDrawId.valueOf(1337)
            every { playerTextDrawRegistry[playerTextDrawId] } returns null

            val caughtThrowable = catchThrowable {
                playerTextDrawService.getPlayerTextDraw(player, playerTextDrawId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No player text draw for player ID 69 and ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPlayerTextDraws() {
        val playerTextDraw1 = mockk<PlayerTextDraw>()
        val playerTextDraw2 = mockk<PlayerTextDraw>()
        every { playerTextDrawRegistry.getAll() } returns listOf(playerTextDraw1, playerTextDraw2)

        val playerTextDraws = playerTextDrawService.getAllPlayerTextDraws(player)

        assertThat(playerTextDraws)
                .containsExactly(playerTextDraw1, playerTextDraw2)
    }

}