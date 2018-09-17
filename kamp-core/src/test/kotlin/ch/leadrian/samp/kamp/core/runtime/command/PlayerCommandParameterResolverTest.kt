package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerSearchIndex
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerCommandParameterResolverTest {

    private lateinit var playerCommandParameterResolver: PlayerCommandParameterResolver

    private val playerSearchIndex = mockk<PlayerSearchIndex>()
    private val playerRegistry = mockk<PlayerRegistry>()

    @BeforeEach
    fun setUp() {
        playerCommandParameterResolver = PlayerCommandParameterResolver(playerRegistry, playerSearchIndex)
    }

    @Test
    fun shouldReturnPlayerAsParameterType() {
        val parameterType = playerCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Player::class.java)
    }

    @Test
    fun givenValidPlayerIdStringItShouldReturnPlayer() {
        val expectedPlayer = mockk<Player>()
        every { playerRegistry[123] } returns expectedPlayer

        val player = playerCommandParameterResolver.resolve("123")

        assertThat(player)
                .isEqualTo(expectedPlayer)
    }

    @Test
    fun givenInvalidPlayerIdStringItShouldReturnNull() {
        every { playerRegistry[123] } returns null

        val player = playerCommandParameterResolver.resolve("123")

        assertThat(player)
                .isNull()
    }

    @Test
    fun givenPlayerNameWithExactlyOneMatchingPlayerItShouldReturnPlayer() {
        val expectedPlayer = mockk<Player>()
        every { playerSearchIndex.findPlayers("hans.wurst") } returns listOf(expectedPlayer)

        val player = playerCommandParameterResolver.resolve("hans.wurst")

        assertThat(player)
                .isEqualTo(expectedPlayer)
    }

    @Test
    fun givenPlayerNameWithNoMatchingPlayerItShouldReturnNull() {
        every { playerSearchIndex.findPlayers("hans.wurst") } returns emptyList()

        val player = playerCommandParameterResolver.resolve("hans.wurst")

        assertThat(player)
                .isNull()
    }

    @Test
    fun givenPlayerNameWithMultipleMatchingPlayersItShouldReturnNull() {
        val player1 = mockk<Player>()
        val player2 = mockk<Player>()
        every { playerSearchIndex.findPlayers("hans.wurst") } returns listOf(player1, player2)

        val player = playerCommandParameterResolver.resolve("hans.wurst")

        assertThat(player)
                .isNull()
    }

}