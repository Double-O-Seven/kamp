package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.TextDrawFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class TextDrawServiceTest {

    private lateinit var textDrawService: TextDrawService

    private val textDrawFactory = mockk<TextDrawFactory>()
    private val textDrawRegistry = mockk<TextDrawRegistry>()
    private val textProvider = mockk<TextProvider>()

    @BeforeEach
    fun setUp() {
        textDrawService = TextDrawService(textDrawFactory, textDrawRegistry, textProvider)
    }

    @Nested
    inner class CreateTextDrawTests {

        @Test
        fun shouldCreateTextDrawWithString() {
            val textDraw = mockk<TextDraw>()
            every {
                textDrawFactory.create(
                        text = "Hi there",
                        position = vector2DOf(1f, 2f),
                        locale = Locale.GERMANY
                )
            } returns textDraw

            val createdTextDraw = textDrawService.createTextDraw(
                    text = "Hi there",
                    position = vector2DOf(1f, 2f),
                    locale = Locale.GERMANY
            )

            assertThat(createdTextDraw)
                    .isEqualTo(textDraw)
        }

        @Test
        fun shouldCreateTextDrawWithTextKey() {
            val textKey = TextKey("text.draw")
            val locale = Locale.GERMANY
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            val textDraw = mockk<TextDraw>()
            every {
                textDrawFactory.create(
                        text = "Hi there",
                        position = vector2DOf(1f, 2f),
                        locale = Locale.GERMANY
                )
            } returns textDraw

            val createdTextDraw = textDrawService.createTextDraw(
                    textKey = textKey,
                    position = vector2DOf(1f, 2f),
                    locale = Locale.GERMANY
            )

            assertThat(createdTextDraw)
                    .isEqualTo(textDraw)
        }
    }

    @Nested
    inner class IsValidTests {

        @Test
        fun givenNoTextDrawForTextDrawIdItShouldReturnFalse() {
            val textDrawId = TextDrawId.valueOf(69)
            every { textDrawRegistry[textDrawId] } returns null

            val isValid = textDrawService.isValidTextDraw(textDrawId)

            assertThat(isValid)
                    .isFalse()
        }

        @Test
        fun givenTextDrawForTextDrawIdExistsItShouldReturnTrue() {
            val textDrawId = TextDrawId.valueOf(69)
            val textDraw = mockk<TextDraw>()
            every { textDrawRegistry[textDrawId] } returns textDraw

            val isValid = textDrawService.isValidTextDraw(textDrawId)

            assertThat(isValid)
                    .isTrue()
        }
    }

    @Nested
    inner class GetTextDrawTests {

        @Test
        fun givenTextDrawIdIsValidItShouldReturnTextDraw() {
            val textDrawId = TextDrawId.valueOf(1337)
            val expectedTextDraw = mockk<TextDraw>()
            every { textDrawRegistry[textDrawId] } returns expectedTextDraw

            val textDraw = textDrawService.getTextDraw(textDrawId)

            assertThat(textDraw)
                    .isEqualTo(expectedTextDraw)
        }

        @Test
        fun givenInvalidTextDrawIdItShouldThrowException() {
            val textDrawId = TextDrawId.valueOf(1337)
            every { textDrawRegistry[textDrawId] } returns null

            val caughtThrowable = catchThrowable {
                textDrawService.getTextDraw(textDrawId)
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No text draw with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllTextDraws() {
        val textDraw1 = mockk<TextDraw>()
        val textDraw2 = mockk<TextDraw>()
        every { textDrawRegistry.getAll() } returns listOf(textDraw1, textDraw2)

        val textDraws = textDrawService.getAllTextDraws()

        assertThat(textDraws)
                .containsExactly(textDraw1, textDraw2)
    }
}