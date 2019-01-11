package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class StreamableTextLabelFactoryTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldCreateStreamableTextLabelWithInputValues(testLOS: Boolean) {
        val textLabelStreamer = mockk<TextLabelStreamer>()
        val textProvider = mockk<TextProvider>()
        val fixedCoordinates = mockk<StreamableTextLabelState.FixedCoordinates> {
            every { coordinates } returns vector3DOf(123f, 456f, 789f)
        }
        val streamableTextLabelStateFactory = mockk<StreamableTextLabelStateFactory> {
            every { createFixedCoordinates(any(), any()) } returns fixedCoordinates
        }
        val onStreamableTextLabelStreamInHandler = mockk<OnStreamableTextLabelStreamInHandler>()
        val onStreamableTextLabelStreamOutHandler = mockk<OnStreamableTextLabelStreamOutHandler>()
        val streamableTextLabelFactory = StreamableTextLabelFactory(
                textProvider,
                streamableTextLabelStateFactory,
                onStreamableTextLabelStreamInHandler,
                onStreamableTextLabelStreamOutHandler
        )

        val streamableTextLabel = streamableTextLabelFactory.create(
                coordinates = vector3DOf(1f, 2f, 3f),
                color = Colors.RED,
                streamDistance = 13.37f,
                priority = 69,
                interiorIds = mutableSetOf(1, 2, 3),
                virtualWorldIds = mutableSetOf(4, 5),
                testLOS = testLOS,
                textLabelStreamer = textLabelStreamer
        ) { "Test" }

        assertAll(
                { assertThat(streamableTextLabel.coordinates).isEqualTo(vector3DOf(123f, 456f, 789f)) },
                { assertThat(streamableTextLabel.color).isEqualTo(Colors.RED) },
                { assertThat(streamableTextLabel.text).isEqualTo("Test") },
                { assertThat(streamableTextLabel.streamDistance).isEqualTo(13.37f) },
                { assertThat(streamableTextLabel.priority).isEqualTo(69) },
                { assertThat(streamableTextLabel.interiorIds).containsExactlyInAnyOrder(1, 2, 3) },
                { assertThat(streamableTextLabel.virtualWorldIds).containsExactlyInAnyOrder(4, 5) },
                { assertThat(streamableTextLabel.testLOS).isEqualTo(testLOS) }
        )
        verify { streamableTextLabelStateFactory.createFixedCoordinates(streamableTextLabel, vector3DOf(1f, 2f, 3f)) }
    }

}