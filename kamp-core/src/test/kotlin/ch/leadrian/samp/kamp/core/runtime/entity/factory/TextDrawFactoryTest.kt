package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

internal class TextDrawFactoryTest {

    private val locale = Locale.GERMANY
    private lateinit var textDrawFactory: TextDrawFactory

    private val textDrawRegistry = mockk<TextDrawRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.textDrawCreate(any(), any(), any()) } returns 0
        every { textDrawRegistry.register(any()) } just Runs
        textDrawFactory = TextDrawFactory(nativeFunctionExecutor, textDrawRegistry, textProvider, textFormatter)
    }

    @Test
    fun shouldCreateTextDraw() {
        textDrawFactory.create(
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test",
                locale = locale
        )

        verify {
            nativeFunctionExecutor.textDrawCreate(
                    x = 1f,
                    y = 2f,
                    text = "Test"
            )
        }
    }

    @Test
    fun shouldRegisterTextDraw() {
        val textDraw = textDrawFactory.create(
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test",
                locale = locale
        )

        verify { textDrawRegistry.register(textDraw) }
    }

    @Test
    fun shouldUnregisterTextDrawOnDestroy() {
        every { textDrawRegistry.unregister(any()) } just Runs
        every { nativeFunctionExecutor.textDrawDestroy(any()) } returns true
        val textDraw = textDrawFactory.create(
                position = vector2DOf(x = 1f, y = 2f),
                text = "Test",
                locale = locale
        )

        textDraw.destroy()

        verify { textDrawRegistry.unregister(textDraw) }
    }

}