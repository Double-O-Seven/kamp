package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.PlayerVarType
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class PlayerVarsImplTest {

    @Test
    fun shouldSetPVarInt() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPVarInt(any(), any(), any()) } returns true
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerVars.setInt("Hi", 999)

        verify { nativeFunctionExecutor.setPVarInt(playerid = playerId, varname = "Hi", value = 999) }
    }

    @Test
    fun shouldGetPVarInt() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarInt(playerId, "Hi") } returns 999
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerVars.getInt("Hi")

        assertThat(result)
                .isEqualTo(999)
    }

    @Test
    fun shouldSetPVarFloat() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPVarFloat(any(), any(), any()) } returns true
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerVars.setFloat("Hi", 999f)

        verify { nativeFunctionExecutor.setPVarFloat(playerid = playerId, varname = "Hi", value = 999f) }
    }

    @Test
    fun shouldGetPVarFloat() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarFloat(playerId, "Hi") } returns 999f
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerVars.getFloat("Hi")

        assertThat(result)
                .isEqualTo(999f)
    }

    @Test
    fun shouldSetPVarString() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPVarString(any(), any(), any()) } returns true
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerVars.setString("Hi", "999")

        verify { nativeFunctionExecutor.setPVarString(playerid = playerId, varname = "Hi", value = "999") }
    }

    @Test
    fun shouldGetPVarString() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarString(playerId, "Hi", any(), 256) } answers {
                thirdArg<ReferenceString>().value = "999"
                true
            }
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = playerVars.getString("Hi", 256)

        assertThat(result)
                .isEqualTo("999")
    }

    @Test
    fun shouldDeletePVar() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { deletePVar(any(), any()) } returns true
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerVars.delete("Hi")

        verify { nativeFunctionExecutor.deletePVar(playerid = playerId, varname = "Hi") }
    }

    @Test
    fun shouldReturnUpperIndex() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarsUpperIndex(playerId) } returns 999
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val upperIndex = playerVars.upperIndex

        assertThat(upperIndex)
                .isEqualTo(999)
    }

    @Test
    fun shouldReturnNameAtIndex() {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarNameAtIndex(playerid = playerId, index = 999, varname = any(), size = 256) } answers {
                thirdArg<ReferenceString>().value = "Hi"
                true
            }
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val varName = playerVars.getNameAtIndex(999, 256)

        assertThat(varName)
                .isEqualTo("Hi")
    }

    @ParameterizedTest
    @EnumSource(PlayerVarType::class)
    fun shouldReturnType(expectedType: PlayerVarType) {
        val playerId = 1337
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getPVarType(playerid = playerId, varname = "Hi") } returns expectedType.value
        }
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val playerVars = PlayerVarsImpl(
                player = player,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val type = playerVars.getType("Hi")

        assertThat(type)
                .isEqualTo(expectedType)
    }
}