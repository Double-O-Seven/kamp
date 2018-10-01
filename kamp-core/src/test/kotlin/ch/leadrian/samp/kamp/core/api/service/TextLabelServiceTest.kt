package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.TextLabelFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextLabelRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class TextLabelServiceTest {

    private lateinit var textLabelService: TextLabelService

    private val textLabelFactory = mockk<TextLabelFactory>()
    private val textLabelRegistry = mockk<TextLabelRegistry>()
    private val textProvider = mockk<TextProvider>()

    @BeforeEach
    fun setUp() {
        textLabelService = TextLabelService(textLabelFactory, textLabelRegistry, textProvider)
    }

    @Nested
    inner class CreateTextLabelTests {

        @Test
        fun shouldCreateTextLabelWithString() {
            val textLabel = mockk<TextLabel>()
            every {
                textLabelFactory.create(
                        text = "Hi there",
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 4f,
                        color = Colors.RED,
                        testLOS = true,
                        virtualWorldId = 69
                )
            } returns textLabel

            val createdTextLabel = textLabelService.createTextLabel(
                    text = "Hi there",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    drawDistance = 4f,
                    testLOS = true,
                    virtualWorldId = 69
            )

            assertThat(createdTextLabel)
                    .isEqualTo(textLabel)
        }

        @Test
        fun shouldCreateTextLabelWithTextKey() {
            val textKey = TextKey("text.label")
            val locale = Locale.GERMANY
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            val textLabel = mockk<TextLabel>()
            every {
                textLabelFactory.create(
                        text = "Hi there",
                        coordinates = vector3DOf(1f, 2f, 3f),
                        drawDistance = 4f,
                        color = Colors.RED,
                        testLOS = true,
                        virtualWorldId = 69
                )
            } returns textLabel

            val createdTextLabel = textLabelService.createTextLabel(
                    textKey = textKey,
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    drawDistance = 4f,
                    testLOS = true,
                    locale = locale,
                    virtualWorldId = 69
            )

            assertThat(createdTextLabel)
                    .isEqualTo(textLabel)
        }
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoTextLabelForTextLabelIdItShouldReturnFalse() {
            val textLabelId = TextLabelId.valueOf(69)
            every { textLabelRegistry[textLabelId] } returns null

            val isValid = textLabelService.isValidTextLabel(textLabelId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenTextLabelForTextLabelIdExistsItShouldReturnTrue() {
            val textLabelId = TextLabelId.valueOf(69)
            val textLabel = mockk<TextLabel>()
            every { textLabelRegistry[textLabelId] } returns textLabel

            val isValid = textLabelService.isValidTextLabel(textLabelId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetTextLabelTests {

        @Test
        fun givenTextLabelIdIsValidItShouldReturnTextLabel() {
            val textLabelId = TextLabelId.valueOf(1337)
            val expectedTextLabel = mockk<TextLabel>()
            every { textLabelRegistry[textLabelId] } returns expectedTextLabel

            val textLabel = textLabelService.getTextLabel(textLabelId)

            assertThat(textLabel)
                    .isEqualTo(expectedTextLabel)
        }

        @Test
        fun givenInvalidTextLabelIdItShouldThrowException() {
            val textLabelId = TextLabelId.valueOf(1337)
            every { textLabelRegistry[textLabelId] } returns null

            val caughtThrowable = catchThrowable {
                textLabelService.getTextLabel(textLabelId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No text label with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllTextLabels() {
        val textLabel1 = mockk<TextLabel>()
        val textLabel2 = mockk<TextLabel>()
        every { textLabelRegistry.getAll() } returns listOf(textLabel1, textLabel2)

        val textLabels = textLabelService.getAllTextLabels()

        assertThat(textLabels)
                .containsExactly(textLabel1, textLabel2)
    }
}