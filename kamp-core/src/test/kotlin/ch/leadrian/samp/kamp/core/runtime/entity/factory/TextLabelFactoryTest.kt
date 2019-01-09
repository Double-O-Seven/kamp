package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextLabelRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TextLabelFactoryTest {

    private lateinit var textLabelFactory: TextLabelFactory

    private val textLabelRegistry = mockk<TextLabelRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()

    @BeforeEach
    fun setUp() {
        every {
            nativeFunctionExecutor.create3DTextLabel(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } returns 0
        every { textLabelRegistry.register(any()) } just Runs
        textLabelFactory = TextLabelFactory(nativeFunctionExecutor, textLabelRegistry, textProvider, textFormatter)
    }

    @Test
    fun shouldCreateTextLabel() {
        textLabelFactory.create(
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                virtualWorldId = 69
        )

        verify {
            nativeFunctionExecutor.create3DTextLabel(
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    text = "Test",
                    testLOS = true,
                    color = Colors.RED.value,
                    DrawDistance = 4f,
                    virtualworld = 69
            )
        }
    }

    @Test
    fun shouldRegisterTextLabel() {
        val textLabel = textLabelFactory.create(
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                virtualWorldId = 69
        )

        verify { textLabelRegistry.register(textLabel) }
    }

    @Test
    fun shouldDeleteTextLabelOnDestroy() {
        every { textLabelRegistry.unregister(any()) } just Runs
        every { nativeFunctionExecutor.delete3DTextLabel(any()) } returns true
        val textLabel = textLabelFactory.create(
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                text = "Test",
                color = Colors.RED,
                testLOS = true,
                drawDistance = 4f,
                virtualWorldId = 69
        )
        textLabel.destroy()

        verify { textLabelRegistry.unregister(textLabel) }
    }

}