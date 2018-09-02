package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ConsoleVarsImplTest {

    private lateinit var consoleVars: ConsoleVarsImpl

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        consoleVars = ConsoleVarsImpl(nativeFunctionExecutor)
    }

    @Nested
    inner class GetStringTests {

        @Test
        fun shouldGetConsoleVarAsString() {
            every { nativeFunctionExecutor.getConsoleVarAsString("test", any(), 64) } answers {
                secondArg<ReferenceString>().value = "ABC123"
                true
            }

            val value = consoleVars.getString("test", 64)

            assertThat(value)
                    .isEqualTo("ABC123")
        }

        @Test
        fun givenValueIsNullItShouldReturnEmptyString() {
            every { nativeFunctionExecutor.getConsoleVarAsString("test", any(), 64) } returns true

            val value = consoleVars.getString("test", 64)

            assertThat(value)
                    .isEmpty()
        }
    }

    @Test
    fun shouldReturnConsoleVarAsInt() {
        every { nativeFunctionExecutor.getConsoleVarAsInt("test") } returns 1337

        val value = consoleVars.getInt("test")

        assertThat(value)
                .isEqualTo(1337)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnConsoleVarAsBoolean(expectedValue: Boolean) {
        every { nativeFunctionExecutor.getConsoleVarAsBool("test") } returns expectedValue

        val value = consoleVars.getBoolean("test")

        assertThat(value)
                .isEqualTo(expectedValue)
    }

}