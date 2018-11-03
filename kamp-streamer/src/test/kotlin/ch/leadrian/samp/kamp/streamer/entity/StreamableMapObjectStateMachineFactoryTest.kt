package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamableMapObjectStateMachineFactoryTest {

    @Test
    fun shouldCreateStateMachine() {
        val fixedCoordinates = mockk<StreamableMapObjectState.FixedCoordinates>()
        val streamableMapObjectStateFactory = mockk<StreamableMapObjectStateFactory> {
            every {
                createFixedCoordinates(vector3DOf(1f, 2f, 3f), vector3DOf(4f, 5f, 6f))
            } returns fixedCoordinates
        }
        val streamableMapObject = mockk<StreamableMapObject>()
        val streamableMapObjectStateMachineFactory = StreamableMapObjectStateMachineFactory(streamableMapObjectStateFactory)

        val streamableMapObjectStateMachine = streamableMapObjectStateMachineFactory.create(
                streamableMapObject = streamableMapObject,
                coordinates = vector3DOf(1f, 2f, 3f),
                rotation = vector3DOf(4f, 5f, 6f)
        )

        assertThat(streamableMapObjectStateMachine.currentState)
                .isEqualTo(fixedCoordinates)
    }

}