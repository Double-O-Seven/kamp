package ch.leadrian.samp.kamp.core.api.command

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CommandDefinitionTest {

    @Test
    fun shouldReturnNameAndAliases() {
        val commandDefinition = CommandDefinition(
                name = "foo",
                aliases = setOf("bar", "baz"),
                parameters = listOf(),
                commandsInstance = mockk(),
                method = Any::class.java.getMethod("hashCode")
        )

        val nameAndAliases = commandDefinition.nameAndAliases

        assertThat(nameAndAliases)
                .containsExactlyInAnyOrder("foo", "bar", "baz")
    }

}