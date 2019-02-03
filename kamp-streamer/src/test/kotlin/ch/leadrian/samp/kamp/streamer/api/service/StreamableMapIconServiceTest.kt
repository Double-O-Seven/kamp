package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapIconImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamableMapIconServiceTest {

    private lateinit var streamableMapIconService: StreamableMapIconService
    private val mapIconStreamer = mockk<MapIconStreamer>()

    @BeforeEach
    fun setUp() {
        streamableMapIconService = StreamableMapIconService(mapIconStreamer)
    }

    @Nested
    inner class CreateStreamableMapIconTests {

        @Test
        fun shouldCreateStreamableMapIcon() {
            val expectedStreamableMapIcon = mockk<StreamableMapIconImpl>()
            every {
                mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 69,
                        streamDistance = 187f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )
            } returns expectedStreamableMapIcon

            val streamableMapIcon = streamableMapIconService.createStreamableMapIcon(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    type = MapIconType.AIR_YARD,
                    color = Colors.PINK,
                    style = MapIconStyle.LOCAL_CHECKPOINT,
                    priority = 69,
                    streamDistance = 187f,
                    interiorIds = mutableSetOf(123),
                    virtualWorldIds = mutableSetOf(456)
            )

            assertThat(streamableMapIcon)
                    .isEqualTo(expectedStreamableMapIcon)
        }

        @Test
        fun shouldCreateStreamableMapIconWithSingleInteriorIdAndVirtualWorldId() {
            val expectedStreamableMapIcon = mockk<StreamableMapIconImpl>()
            every {
                mapIconStreamer.createMapIcon(
                        coordinates = vector3DOf(150f, 100f, 20f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 69,
                        streamDistance = 187f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )
            } returns expectedStreamableMapIcon

            val streamableMapIcon = streamableMapIconService.createStreamableMapIcon(
                    coordinates = vector3DOf(150f, 100f, 20f),
                    type = MapIconType.AIR_YARD,
                    color = Colors.PINK,
                    style = MapIconStyle.LOCAL_CHECKPOINT,
                    priority = 69,
                    streamDistance = 187f,
                    interiorId = 123,
                    virtualWorldId = 456
            )

            assertThat(streamableMapIcon)
                    .isEqualTo(expectedStreamableMapIcon)
        }

        @Test
        fun shouldCreateStreamableMapIconWithLocation() {
            val expectedStreamableMapIcon = mockk<StreamableMapIconImpl>()
            every {
                mapIconStreamer.createMapIcon(
                        coordinates = match { it.x == 150f && it.y == 100f && it.z == 20f },
                        type = MapIconType.AIR_YARD,
                        color = Colors.PINK,
                        style = MapIconStyle.LOCAL_CHECKPOINT,
                        priority = 69,
                        streamDistance = 187f,
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )
            } returns expectedStreamableMapIcon

            val streamableMapIcon = streamableMapIconService.createStreamableMapIcon(
                    location = locationOf(
                            x = 150f,
                            y = 100f,
                            z = 20f,
                            interiorId = 123,
                            worldId = 456
                    ),
                    type = MapIconType.AIR_YARD,
                    color = Colors.PINK,
                    style = MapIconStyle.LOCAL_CHECKPOINT,
                    priority = 69,
                    streamDistance = 187f
            )

            assertThat(streamableMapIcon)
                    .isEqualTo(expectedStreamableMapIcon)
        }
    }

    @Test
    fun shouldSetMaxStreamedInMapIcons() {
        every { mapIconStreamer.capacity = any() } just Runs

        streamableMapIconService.setMaxStreamedInMapIcons(50)

        verify { mapIconStreamer.capacity = 50 }
    }

}