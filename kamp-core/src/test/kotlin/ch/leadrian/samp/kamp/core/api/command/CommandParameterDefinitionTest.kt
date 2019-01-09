package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class CommandParameterDefinitionTest {

    private val locale = Locale.GERMANY
    private val textProvider = mockk<TextProvider>()

    @Nested
    inner class GetNameTests {

        @Test
        fun givenNoTextKeyAndNoTextItShouldReturnTypeName() {
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = null,
                    nameTextKey = null,
                    description = null,
                    descriptionTextKey = null,
                    textProvider = textProvider
            )

            val name = commandParameterDefinition.getName(locale)

            assertThat(name)
                    .isEqualTo("int")
        }

        @Test
        fun givenTextAndNoTextKeyItShouldReturnTextAsName() {
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = "test",
                    nameTextKey = null,
                    description = null,
                    descriptionTextKey = null,
                    textProvider = textProvider
            )

            val name = commandParameterDefinition.getName(locale)

            assertThat(name)
                    .isEqualTo("test")
        }

        @Test
        fun givenTextKeyItShouldReturnTranslatedName() {
            val nameTextKey = TextKey("command.definition.test")
            every { textProvider.getText(locale, nameTextKey, "test") } returns "Hi there"
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = "test",
                    nameTextKey = nameTextKey,
                    description = null,
                    descriptionTextKey = null,
                    textProvider = textProvider
            )

            val name = commandParameterDefinition.getName(locale)

            assertThat(name)
                    .isEqualTo("Hi there")
        }

    }

    @Nested
    inner class GetDescriptionTests {

        @Test
        fun givenNoTextKeyAndNoTextItShouldReturnNull() {
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = null,
                    nameTextKey = null,
                    description = null,
                    descriptionTextKey = null,
                    textProvider = textProvider
            )

            val description = commandParameterDefinition.getDescription(locale)

            assertThat(description)
                    .isNull()
        }

        @Test
        fun givenTextAndNoTextKeyItShouldReturnTextAsDescription() {
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = null,
                    nameTextKey = null,
                    description = "test",
                    descriptionTextKey = null,
                    textProvider = textProvider
            )

            val description = commandParameterDefinition.getDescription(locale)

            assertThat(description)
                    .isEqualTo("test")
        }

        @Test
        fun givenTextKeyItShouldReturnTranslatedDescription() {
            val descriptionTextKey = TextKey("command.definition.test")
            every { textProvider.getText(locale, descriptionTextKey, "test") } returns "Hi there"
            val commandParameterDefinition = CommandParameterDefinition(
                    type = Int::class.java,
                    resolver = mockk(),
                    invalidCommandParameterValueHandler = null,
                    name = null,
                    nameTextKey = null,
                    description = "test",
                    descriptionTextKey = descriptionTextKey,
                    textProvider = textProvider
            )

            val description = commandParameterDefinition.getDescription(locale)

            assertThat(description)
                    .isEqualTo("Hi there")
        }

    }

}