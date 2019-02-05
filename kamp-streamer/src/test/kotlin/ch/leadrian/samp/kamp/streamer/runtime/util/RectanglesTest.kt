package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.boxOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import com.conversantmedia.util.collection.geometry.Rect2d
import com.conversantmedia.util.collection.geometry.Rect3d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RectanglesTest {

    @Test
    fun shouldConvertRectangleToRect2d() {
        val rectangle = rectangleOf(minX = 1f, minY = 2f, maxX = 3f, maxY = 4f)

        val rect2d = rectangle.toRect2d()

        assertThat(rect2d)
                .isEqualTo(Rect2d(1.0, 2.0, 3.0, 4.0))
    }

    @Test
    fun shouldConvertBoxToRect2d() {
        val box = boxOf(minX = 1f, minY = 2f, minZ = 3f, maxX = 4f, maxY = 5f, maxZ = 6f)

        val rect3d = box.toRect3d()

        assertThat(rect3d)
                .isEqualTo(Rect3d(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
    }

}