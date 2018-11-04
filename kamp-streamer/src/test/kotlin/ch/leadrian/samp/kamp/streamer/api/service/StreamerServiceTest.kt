package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObject
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamerServiceTest {

    private lateinit var streamerService: StreamerService
    private val mapObjectStreamer = mockk<MapObjectStreamer>()

    @BeforeEach
    fun setUp() {
        streamerService = StreamerService(
                mapObjectStreamer = mapObjectStreamer
        )
    }

    @Nested
    inner class CreateStreamableMapObjectTests {

        @Test
        fun shouldCreateStreamableMapObject() {
            val expectedStreamableMapObject = mockk<StreamableMapObject>()
            every {
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableMapObject

            val streamableMapObject = streamerService.createStreamableMapObject(
                    modelId = 1337,
                    priority = 69,
                    streamDistance = 187f,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f)
            )

            assertThat(streamableMapObject)
                    .isEqualTo(expectedStreamableMapObject)
        }

        @Test
        fun shouldCreateStreamableMapObjectWithInteriorIdAndVirtualWorldId() {
            val expectedStreamableMapObject = mockk<StreamableMapObject>()
            every {
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableMapObject

            val streamableMapObject = streamerService.createStreamableMapObject(
                    modelId = 1337,
                    priority = 69,
                    streamDistance = 187f,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorId = 12,
                    virtualWorldId = 34
            )

            assertThat(streamableMapObject)
                    .isEqualTo(expectedStreamableMapObject)
        }

        @Test
        fun shouldCreateStreamableMapObjectWithMultipleInteriorIdsAndVirtualWorldIds() {
            val expectedStreamableMapObject = mockk<StreamableMapObject>()
            every {
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf(34)
                )
            } returns expectedStreamableMapObject

            val streamableMapObject = streamerService.createStreamableMapObject(
                    modelId = 1337,
                    priority = 69,
                    streamDistance = 187f,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorIds = mutableSetOf(12),
                    virtualWorldIds = mutableSetOf(34)
            )

            assertThat(streamableMapObject)
                    .isEqualTo(expectedStreamableMapObject)
        }

        @Test
        fun shouldCreateStreamableMapObjectWithMultipleInteriorIds() {
            val expectedStreamableMapObject = mockk<StreamableMapObject>()
            every {
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 69,
                        streamDistance = 187f,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        interiorIds = mutableSetOf(12),
                        virtualWorldIds = mutableSetOf()
                )
            } returns expectedStreamableMapObject

            val streamableMapObject = streamerService.createStreamableMapObject(
                    modelId = 1337,
                    priority = 69,
                    streamDistance = 187f,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    interiorIds = mutableSetOf(12)
            )

            assertThat(streamableMapObject)
                    .isEqualTo(expectedStreamableMapObject)
        }
    }

    @Test
    fun shouldSetMaxStreamedInMapObjects() {
        every { mapObjectStreamer.capacity = any() } just Runs

        streamerService.setMaxStreamedInMapObjects(500)

        verify { mapObjectStreamer.capacity = 500 }
    }

}