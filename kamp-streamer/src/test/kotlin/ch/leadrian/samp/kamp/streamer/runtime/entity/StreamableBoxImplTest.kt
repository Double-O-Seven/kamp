package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.boxOf
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamableBoxImplTest {

    @Test
    fun shouldReturnBoundingBox() {
        val streamableBox = StreamableBoxImpl(
                box = boxOf(minX = 1f, minY = 2f, minZ = 3f, maxX = 4f, maxY = 5f, maxZ = 6f),
                priority = 0,
                interiorIds = mutableSetOf(),
                virtualWorldIds = mutableSetOf(),
                onPlayerEnterStreamableAreaHandler = mockk(),
                onPlayerLeaveStreamableAreaHandler = mockk()
        )

        val rect3d = streamableBox.boundingBox

        assertThat(rect3d)
                .isEqualTo(Rect3d(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
    }

}