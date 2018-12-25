package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class StreamableTextLabelServiceTest {

    private lateinit var streamableTextLabelService: StreamableTextLabelService
    private val textLabelStreamer = mockk<TextLabelStreamer>()
    private val textProvider = mockk<TextProvider>()

    @BeforeEach
    fun setUp() {
        streamableTextLabelService = StreamableTextLabelService(
                textLabelStreamer = textLabelStreamer,
                textProvider = textProvider
        )
    }

    @Nested
    inner class CreateStreamableTextLabelWithPlainTextStringTests {


        @Test
        fun shouldUseTextInTextSupplier() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = any(),
                        color = any(),
                        priority = any(),
                        streamDistance = any(),
                        coordinates = any(),
                        interiorIds = any(),
                        virtualWorldIds = any()
                )
            } returns expectedStreamableTextLabel
            streamableTextLabelService.createStreamableTextLabel(
                    text = "Test",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            )
            val slot = slot<(Locale) -> String>()
            verify { textLabelStreamer.createTextLabel(any(), capture(slot), any(), any(), any(), any(), any(), any()) }

            val text = slot.captured.invoke(Locale.GERMANY)

            assertThat(text)
                    .isEqualTo("Test")
        }

        @Test
        fun shouldCreateStreamableTextLabel() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    text = "Test",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithInteriorIdAndVirtualWorldId() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    text = "Test",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorId = 12,
                    virtualWorldId = 34
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIdsAndVirtualWorldIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    text = "Test",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12),
                    virtualWorldIds = mutableSetOf(34)
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12, 13),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    text = "Test",
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12, 13)
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }
    }

    @Nested
    inner class CreateStreamableTextLabelWithProvidedStringTests {

        @Test
        fun shouldUseTextInTextSupplier() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = any(),
                        color = any(),
                        priority = any(),
                        streamDistance = any(),
                        coordinates = any(),
                        interiorIds = any(),
                        virtualWorldIds = any()
                )
            } returns expectedStreamableTextLabel
            streamableTextLabelService.createStreamableTextLabel(
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            ) { "Test" }
            val slot = slot<(Locale) -> String>()
            verify { textLabelStreamer.createTextLabel(any(), capture(slot), any(), any(), any(), any(), any(), any()) }

            val text = slot.captured.invoke(Locale.GERMANY)

            assertThat(text)
                    .isEqualTo("Test")
        }

        @Test
        fun shouldCreateStreamableTextLabel() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            ) { "Test" }

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithInteriorIdAndVirtualWorldId() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorId = 12,
                    virtualWorldId = 34
            ) { "Test" }

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIdsAndVirtualWorldIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12),
                    virtualWorldIds = mutableSetOf(34)
            ) { "Test" }

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12, 13),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12, 13)
            ) { "Test" }

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }
    }

    @Nested
    inner class CreateStreamableTextLabelWithTextKeyTests {

        @Test
        fun shouldUseTextInTextSupplier() {
            val textKey = TextKey("test.text.key")
            every { textProvider.getText(Locale.GERMANY, textKey) } returns "Test"
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = any(),
                        color = any(),
                        priority = any(),
                        streamDistance = any(),
                        coordinates = any(),
                        interiorIds = any(),
                        virtualWorldIds = any()
                )
            } returns expectedStreamableTextLabel
            streamableTextLabelService.createStreamableTextLabel(
                    textKey = textKey,
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            )
            val slot = slot<(Locale) -> String>()
            verify { textLabelStreamer.createTextLabel(any(), capture(slot), any(), any(), any(), any(), any(), any()) }

            val text = slot.captured.invoke(Locale.GERMANY)

            assertThat(text)
                    .isEqualTo("Test")
        }

        @Test
        fun shouldCreateStreamableTextLabel() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    textKey = TextKey("test.text.key"),
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithInteriorIdAndVirtualWorldId() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    textKey = TextKey("test.text.key"),
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorId = 12,
                    virtualWorldId = 34
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIdsAndVirtualWorldIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    textKey = TextKey("test.text.key"),
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12),
                    virtualWorldIds = mutableSetOf(34)
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }

        @Test
        fun shouldCreateStreamableTextLabelWithMultipleInteriorIds() {
            val expectedStreamableTextLabel = mockk<StreamableTextLabelImpl>()
            every {
                textLabelStreamer.createTextLabel(
                        textSupplier = any(),
                        testLOS = true,
                        color = Colors.RED,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(12, 13),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableTextLabel

            val streamableTextLabel = streamableTextLabelService.createStreamableTextLabel(
                    textKey = TextKey("test.text.key"),
                    color = Colors.RED,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    streamDistance = 187f,
                    priority = 69,
                    testLOS = true,
                    interiorIds = mutableSetOf(12, 13)
            )

            assertThat(streamableTextLabel)
                    .isEqualTo(expectedStreamableTextLabel)
        }
    }

    @Test
    fun shouldSetMaxStreamedInTextLabels() {
        every { textLabelStreamer.capacity = any() } just Runs

        streamableTextLabelService.setMaxStreamedInTextLabels(500)

        verify { textLabelStreamer.capacity = 500 }
    }

}