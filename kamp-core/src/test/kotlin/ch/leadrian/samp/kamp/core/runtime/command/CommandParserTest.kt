package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CommandParserTest {

    private val commandParser = CommandParser()

    @ParameterizedTest
    @ValueSource(strings = ["/kill", "/kill   "])
    fun shouldParseCommandWithoutParameters(commandLine: String) {
        val result = commandParser.parse(commandLine)

        assertThat(result)
                .isEqualTo(ParsedCommand("kill"))
    }

    @ParameterizedTest
    @ValueSource(
            strings = [
                "/pm 13 \"How are you?\"",
                "/pm 13 \'How are you?\'",
                "/pm    13    \"How are you?\"    ",
                "/pm    13    \'How are you?\'    "
            ]
    )
    fun shouldParseCommandWithParameters(commandLine: String) {
        val result = commandParser.parse(commandLine)

        assertThat(result)
                .isEqualTo(ParsedCommand("pm", listOf("13", "How are you?")))
    }

    @Test
    fun givenEmptyStringItShouldReturnNull() {
        val result = commandParser.parse("")

        assertThat(result)
                .isNull()
    }

    @Test
    fun givenOnlySlashItShouldReturnNull() {
        val result = commandParser.parse("/")

        assertThat(result)
                .isNull()
    }

    @Test
    fun givenInvalidCommandLineItShouldReturnNull() {
        val result = commandParser.parse("/pm 13 'Hi how are?")

        assertThat(result)
                .isNull()
    }
}