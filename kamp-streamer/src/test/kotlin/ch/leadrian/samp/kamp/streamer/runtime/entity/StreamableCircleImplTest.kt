package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.circleOf
import com.conversantmedia.util.collection.geometry.Rect2d
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamableCircleImplTest {

    @Test
    fun shouldReturnBoundingBox() {
        val streamableCircle = StreamableCircleImpl(
                circle = circleOf(x = 1f, y = 2f, radius = 3f),
                priority = 0,
                interiorIds = mutableSetOf(),
                virtualWorldIds = mutableSetOf(),
                onPlayerEnterStreamableAreaHandler = mockk(),
                onPlayerLeaveStreamableAreaHandler = mockk()
        )

        val rect2d = streamableCircle.boundingBox

        assertThat(rect2d)
                .isEqualTo(Rect2d(-2.0, -1.0, 4.0, 5.0))
    }

}