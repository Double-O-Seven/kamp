package ch.leadrian.samp.kamp.streamer.entity.factory

import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObjectStateMachine
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObjectStateMachineFactory
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StreamableMapObjectFactoryTest {

    private lateinit var streamableMapObjectFactory: StreamableMapObjectFactory
    private val streamableMapObjectStateMachineFactory = mockk<StreamableMapObjectStateMachineFactory>()
    private val streamableMapObjectStateMachine = mockk<StreamableMapObjectStateMachine>(relaxed = true)

    @BeforeEach
    fun setUp() {
        streamableMapObjectFactory = StreamableMapObjectFactory(
                playerMapObjectService = mockk(),
                onStreamableMapObjectMovedHandler = mockk(),
                onPlayerEditStreamableMapObjectHandler = mockk(),
                onPlayerSelectStreamableMapObjectHandler = mockk(),
                textProvider = mockk(),
                streamableMapObjectStateMachineFactory = streamableMapObjectStateMachineFactory
        )
        every { streamableMapObjectStateMachineFactory.create(any(), any(), any()) } returns streamableMapObjectStateMachine
    }

    @Test
    fun shouldCreateStreamableMapObject() {
        val streamableMapObject = streamableMapObjectFactory.create(
                modelId = 1337,
                priority = 69,
                streamDistance = 187f,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                rotation = mutableVector3DOf(4f, 5f, 6f),
                interiorIds = mutableSetOf(12, 34),
                virtualWorldIds = mutableSetOf(56, 78, 90)
        )

        assertThat(streamableMapObject)
                .satisfies {
                    assertThat(it.modelId)
                            .isEqualTo(1337)
                    assertThat(it.priority)
                            .isEqualTo(69)
                    assertThat(it.streamDistance)
                            .isEqualTo(187f)
                    assertThat(it.interiorIds)
                            .containsExactlyInAnyOrder(12, 34)
                    assertThat(it.virtualWorldIds)
                            .containsExactlyInAnyOrder(56, 78, 90)
                }
    }

}