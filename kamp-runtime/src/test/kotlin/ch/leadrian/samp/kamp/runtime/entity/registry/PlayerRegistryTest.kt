package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_PLAYERS - 1])
    fun shouldRegisterAndGetPlayer(playerId: Int) {
        val player = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)

        playerRegistry.register(player)

        val registeredPlayer = playerRegistry[playerId]
        assertThat(registeredPlayer)
                .isSameAs(player)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_PLAYERS, SAMPConstants.MAX_PLAYERS + 1])
    fun givenUnknownPlayerIdGetPlayerShouldReturn(playerId: Int) {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)

        val registeredPlayer = playerRegistry[playerId]
        assertThat(registeredPlayer)
                .isNull()
    }

    @Test
    fun givenAnotherPlayerWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val playerId = 50
        val alreadyRegisteredPlayer = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val newPlayer = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)
        playerRegistry.register(alreadyRegisteredPlayer)

        val caughtThrowable = catchThrowable { playerRegistry.register(newPlayer) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayer = playerRegistry[playerId]
        assertThat(registeredPlayer)
                .isSameAs(alreadyRegisteredPlayer)
    }

    @Test
    fun shouldUnregisterRegisteredPlayer() {
        val playerId = 50
        val player = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)
        playerRegistry.register(player)

        playerRegistry.unregister(player)

        val registeredPlayer = playerRegistry[playerId]
        assertThat(registeredPlayer)
                .isNull()
    }

    @Test
    fun givenPlayerIsNotRegisteredItShouldThrowAnException() {
        val playerId = PlayerId.valueOf(50)
        val player = mockk<PlayerImpl> {
            every { id } returns playerId
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)

        val caughtThrowable = catchThrowable { playerRegistry.unregister(player) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherPlayerWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val playerId = 50
        val alreadyRegisteredPlayer = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val newPlayer = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)
        playerRegistry.register(alreadyRegisteredPlayer)

        val caughtThrowable = catchThrowable { playerRegistry.unregister(newPlayer) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayer = playerRegistry[playerId]
        assertThat(registeredPlayer)
                .isSameAs(alreadyRegisteredPlayer)
    }

    @Test
    fun shouldReturnAllRegisteredPlayers() {
        val playerId1 = PlayerId.valueOf(10)
        val player1 = mockk<PlayerImpl> {
            every { id } returns playerId1
        }
        val playerId2 = PlayerId.valueOf(15)
        val player2 = mockk<PlayerImpl> {
            every { id } returns playerId2
        }
        val playerId3 = PlayerId.valueOf(30)
        val player3 = mockk<PlayerImpl> {
            every { id } returns playerId3
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns SAMPConstants.MAX_PLAYERS
        }
        val playerRegistry = PlayerRegistry(nativeFunctionExecutor)
        playerRegistry.register(player1)
        playerRegistry.register(player2)
        playerRegistry.register(player3)

        val allPlayers = playerRegistry.getAll()

        assertThat(allPlayers)
                .containsExactly(player1, player2, player3)
    }

}