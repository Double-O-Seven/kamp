package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.PlayerMarkersMode
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerSearchIndex
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class PlayerServiceTest {

    private lateinit var playerService: PlayerService

    private val playerRegistry = mockk<PlayerRegistry>()
    private val playerSearchIndex = mockk<PlayerSearchIndex>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        playerService = PlayerService(playerRegistry, playerSearchIndex, nativeFunctionExecutor)
    }

    @Nested
    inner class IsPlayerConnectedTests {

        @Test
        fun givenNoPlayerForPlayerIdItShouldReturnFalse() {
            val playerId = PlayerId.valueOf(69)
            every { playerRegistry[playerId] } returns null

            val isValid = playerService.isPlayerConnected(playerId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenPlayerForPlayerIdExistsItShouldReturnTrue() {
            val playerId = PlayerId.valueOf(69)
            val player = mockk<Player>()
            every { playerRegistry[playerId] } returns player

            val isValid = playerService.isPlayerConnected(playerId)

            assertThat(isValid)
                    .isTrue()
        }

    }

    @Nested
    inner class GetPlayerTests {

        @Test
        fun givenPlayerIdIsValidItShouldReturnPlayer() {
            val playerId = PlayerId.valueOf(1337)
            val expectedPlayer = mockk<Player>()
            every { playerRegistry[playerId] } returns expectedPlayer

            val player = playerService.getPlayer(playerId)

            assertThat(player)
                    .isEqualTo(expectedPlayer)
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowException() {
            val playerId = PlayerId.valueOf(1337)
            every { playerRegistry[playerId] } returns null

            val caughtThrowable = catchThrowable { playerService.getPlayer(playerId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No player with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllPlayers() {
        val player1 = mockk<Player>()
        val player2 = mockk<Player>()
        every { playerRegistry.getAll() } returns listOf(player1, player2)

        val players = playerService.getAllPlayers()

        assertThat(players)
                .containsExactly(player1, player2)
    }

    @Test
    fun shouldEnableStuntBonusForAll() {
        every { nativeFunctionExecutor.enableStuntBonusForAll(any()) } returns true

        playerService.enableStuntBonusForAll()

        verify { nativeFunctionExecutor.enableStuntBonusForAll(true) }
    }

    @Test
    fun shouldDisableStuntBonusForAll() {
        every { nativeFunctionExecutor.enableStuntBonusForAll(any()) } returns true

        playerService.disableStuntBonusForAll()

        verify { nativeFunctionExecutor.enableStuntBonusForAll(false) }
    }

    @Test
    fun shouldReturnMaxPlayers() {
        every { nativeFunctionExecutor.getMaxPlayers() } returns 69

        val maxPlayers = playerService.getMaxPlayers()

        assertThat(maxPlayers)
                .isEqualTo(69)
    }

    @Test
    fun shouldReturnPoolSize() {
        every { nativeFunctionExecutor.getPlayerPoolSize() } returns 69

        val poolSize = playerService.getPoolSize()

        assertThat(poolSize)
                .isEqualTo(69)
    }

    @Test
    fun shouldShowNameTags() {
        every { nativeFunctionExecutor.showNameTags(any()) } returns true

        playerService.showNameTags()

        verify { nativeFunctionExecutor.showNameTags(true) }
    }

    @Test
    fun shouldHideNameTags() {
        every { nativeFunctionExecutor.showNameTags(any()) } returns true

        playerService.hideNameTags()

        verify { nativeFunctionExecutor.showNameTags(false) }
    }

    @ParameterizedTest
    @EnumSource(PlayerMarkersMode::class)
    fun shouldShowMarkers(mode: PlayerMarkersMode) {
        every { nativeFunctionExecutor.showPlayerMarkers(any()) } returns true

        playerService.showMarkers(mode)

        verify { nativeFunctionExecutor.showPlayerMarkers(mode.value) }
    }

    @Test
    fun shouldAllowInteriorWeapons() {
        every { nativeFunctionExecutor.allowInteriorWeapons(any()) } returns true

        playerService.allowInteriorWeapons()

        verify { nativeFunctionExecutor.allowInteriorWeapons(true) }
    }

    @Test
    fun shouldForbidInteriorWeapons() {
        every { nativeFunctionExecutor.allowInteriorWeapons(any()) } returns true

        playerService.forbidInteriorWeapons()

        verify { nativeFunctionExecutor.allowInteriorWeapons(false) }
    }

    @Test
    fun shouldSetDeathDropAmount() {
        every { nativeFunctionExecutor.setDeathDropAmount(any()) } returns true

        playerService.setDeathDropAmount(5000)

        verify { nativeFunctionExecutor.setDeathDropAmount(5000) }
    }

    @Test
    fun shouldEnableZoneNames() {
        every { nativeFunctionExecutor.enableZoneNames(any()) } returns true

        playerService.enableZoneNames()

        verify { nativeFunctionExecutor.enableZoneNames(true) }
    }

    @Test
    fun shouldDisableZoneNames() {
        every { nativeFunctionExecutor.enableZoneNames(any()) } returns true

        playerService.disableZoneNames()

        verify { nativeFunctionExecutor.enableZoneNames(false) }
    }

    @Test
    fun shouldUsePlayerPedAnimations() {
        every { nativeFunctionExecutor.usePlayerPedAnims() } returns true

        playerService.usePlayerPedAnimations()

        verify { nativeFunctionExecutor.usePlayerPedAnims() }
    }

    @Test
    fun shouldSetNameTagDrawDistance() {
        every { nativeFunctionExecutor.setNameTagDrawDistance(any()) } returns true

        playerService.setNameTagDrawDistance(13.37f)

        verify { nativeFunctionExecutor.setNameTagDrawDistance(13.37f) }
    }

    @Test
    fun shouldDisableNameTagLineOfSight() {
        every { nativeFunctionExecutor.disableNameTagLOS() } returns true

        playerService.disableNameTagLineOfSight()

        verify { nativeFunctionExecutor.disableNameTagLOS() }
    }

    @Test
    fun shouldLimitGlobalChatRadius() {
        every { nativeFunctionExecutor.limitGlobalChatRadius(any()) } returns true

        playerService.limitGlobalChatRadius(187f)

        verify { nativeFunctionExecutor.limitGlobalChatRadius(187f) }
    }

    @Test
    fun shouldLimitPlayerMarkerRadius() {
        every { nativeFunctionExecutor.limitPlayerMarkerRadius(any()) } returns true

        playerService.limitPlayerMarkerRadius(187f)

        verify { nativeFunctionExecutor.limitPlayerMarkerRadius(187f) }
    }

    @Test
    fun shouldGetPlayerByName() {
        val name = "hans.wurst"
        val expectedPlayer = mockk<Player>()
        every { playerSearchIndex.getPlayer(any()) } returns expectedPlayer

        val player = playerService.getPlayerByName(name)

        assertThat(player)
                .isEqualTo(expectedPlayer)
    }

    @Test
    fun shouldFindPlayersByName() {
        val player1 = mockk<Player>()
        val player2 = mockk<Player>()
        val name = "hans.wurst"
        every { playerSearchIndex.findPlayers(name) } returns listOf(player1, player2)

        val players = playerService.findPlayersByName(name)

        assertThat(players)
                .containsExactly(player1, player2)
    }

    @Nested
    inner class SendDeathMessageTests {

        private val victim = mockk<Player>()
        private val victimId = PlayerId.valueOf(50)

        @BeforeEach
        fun setUp() {
            every { victim.id } returns victimId
        }

        @Test
        fun shouldDeathMessageWithKiller() {
            val killerId = PlayerId.valueOf(75)
            val killer = mockk<Player> {
                every { id } returns killerId
            }
            every { nativeFunctionExecutor.sendDeathMessage(any(), any(), any()) } returns true

            playerService.sendDeathMessage(victim = victim, weapon = WeaponModel.AK47, killer = killer)

            verify {
                nativeFunctionExecutor.sendDeathMessage(
                        killer = killerId.value,
                        weapon = WeaponModel.AK47.value,
                        killee = victimId.value
                )
            }
        }

        @Test
        fun shouldDeathMessageWithoutKiller() {
            every { nativeFunctionExecutor.sendDeathMessage(any(), any(), any()) } returns true

            playerService.sendDeathMessage(victim = victim, weapon = WeaponModel.AK47, killer = null)

            verify {
                nativeFunctionExecutor.sendDeathMessage(
                        killer = SAMPConstants.INVALID_PLAYER_ID,
                        weapon = WeaponModel.AK47.value,
                        killee = victimId.value
                )
            }
        }

    }

}