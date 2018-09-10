package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ServerVarType
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class ServerVarsTest {

    @Test
    fun shouldSetSVarInt() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setSVarInt(any(), any()) } returns true
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        serverVars.setInt("Hi", 999)

        verify { nativeFunctionExecutor.setSVarInt(varname = "Hi", int_value = 999) }
    }

    @Test
    fun shouldGetSVarInt() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarInt("Hi") } returns 999
        }
        val serverVars = ServerVars(
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = serverVars.getInt("Hi")

        assertThat(result)
                .isEqualTo(999)
    }

    @Test
    fun shouldSetSVarFloat() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setSVarFloat(any(), any()) } returns true
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        serverVars.setFloat("Hi", 999f)

        verify { nativeFunctionExecutor.setSVarFloat(varname = "Hi", float_value = 999f) }
    }

    @Test
    fun shouldGetSVarFloat() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarFloat("Hi") } returns 999f
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        val result = serverVars.getFloat("Hi")

        assertThat(result)
                .isEqualTo(999f)
    }

    @Test
    fun shouldSetSVarString() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setSVarString(any(), any()) } returns true
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        serverVars.setString("Hi", "999")

        verify { nativeFunctionExecutor.setSVarString(varname = "Hi", string_value = "999") }
    }

    @Test
    fun shouldGetSVarString() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarString("Hi", any(), 256) } answers {
                secondArg<ReferenceString>().value = "999"
                true
            }
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        val result = serverVars.getString("Hi", 256)

        assertThat(result)
                .isEqualTo("999")
    }

    @Test
    fun shouldDeleteSVar() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { deleteSVar(any()) } returns true
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        serverVars.delete("Hi")

        verify { nativeFunctionExecutor.deleteSVar("Hi") }
    }

    @Test
    fun shouldReturnUpperIndex() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarsUpperIndex() } returns 999
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        val upperIndex = serverVars.upperIndex

        assertThat(upperIndex)
                .isEqualTo(999)
    }

    @Test
    fun shouldReturnNameAtIndex() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarNameAtIndex(index = 999, ret_varname = any(), ret_len = 256) } answers {
                secondArg<ReferenceString>().value = "Hi"
                true
            }
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        val varName = serverVars.getNameAtIndex(999, 256)

        assertThat(varName)
                .isEqualTo("Hi")
    }

    @ParameterizedTest
    @EnumSource(ServerVarType::class)
    fun shouldReturnType(expectedType: ServerVarType) {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getSVarType("Hi") } returns expectedType.value
        }
        val serverVars = ServerVars(nativeFunctionExecutor)

        val type = serverVars.getType("Hi")

        assertThat(type)
                .isEqualTo(expectedType)
    }
}