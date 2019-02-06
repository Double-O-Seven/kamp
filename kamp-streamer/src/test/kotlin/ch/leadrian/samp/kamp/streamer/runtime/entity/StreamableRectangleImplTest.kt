package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import com.conversantmedia.util.collection.geometry.Rect2d
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamableRectangleImplTest {

    @Test
    fun shouldReturnBoundingBox() {
        val streamableRectangle = StreamableRectangleImpl(
                rectangle = rectangleOf(minX = 1f, minY = 2f, maxX = 3f, maxY = 4f),
                priority = 0,
                interiorIds = mutableSetOf(),
                virtualWorldIds = mutableSetOf(),
                onPlayerEnterStreamableAreaHandler = mockk(),
                onPlayerLeaveStreamableAreaHandler = mockk()
        )

        val rect2d = streamableRectangle.boundingBox

        assertThat(rect2d)
                .isEqualTo(Rect2d(1.0, 2.0, 3.0, 4.0))
    }

}