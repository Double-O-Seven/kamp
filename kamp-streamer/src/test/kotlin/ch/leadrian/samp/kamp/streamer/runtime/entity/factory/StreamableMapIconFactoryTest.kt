package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamableMapIconFactoryTest {

    private lateinit var streamableMapIconFactory: StreamableMapIconFactory
    private val mapIconStreamer = mockk<MapIconStreamer>()

    @BeforeEach
    fun setUp() {
        streamableMapIconFactory = StreamableMapIconFactory(mockk(), mockk(), mockk())
    }

    @Test
    fun shouldCreateStreamableMapIcon() {
        val streamableMapIcon = streamableMapIconFactory.create(
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                type = MapIconType.BALLAS,
                color = Colors.RED,
                style = MapIconStyle.LOCAL_CHECKPOINT,
                priority = 69,
                streamDistance = 187f,
                interiorIds = mutableSetOf(12, 34),
                virtualWorldIds = mutableSetOf(56, 78, 90),
                mapIconStreamer = mapIconStreamer
        )

        assertAll(
                { assertThat(streamableMapIcon.coordinates).isEqualTo(vector3DOf(1f, 2f, 3f)) },
                { assertThat(streamableMapIcon.type).isEqualTo(MapIconType.BALLAS) },
                { assertThat(streamableMapIcon.color).isEqualTo(Colors.RED) },
                { assertThat(streamableMapIcon.style).isEqualTo(MapIconStyle.LOCAL_CHECKPOINT) },
                { assertThat(streamableMapIcon.priority).isEqualTo(69) },
                { assertThat(streamableMapIcon.streamDistance).isEqualTo(187f) },
                { assertThat(streamableMapIcon.interiorIds).containsExactlyInAnyOrder(12, 34) },
                { assertThat(streamableMapIcon.virtualWorldIds).containsExactlyInAnyOrder(56, 78, 90) }
        )
    }

}