package ch.leadrian.samp.kamp.streamer.runtime.index

import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
import com.conversantmedia.util.collection.geometry.Point2d
import com.conversantmedia.util.collection.geometry.Rect2d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SpatialIndex2DTest {

    @Test
    fun shouldReturnMbr() {
        val spatialIndex2D = SpatialIndex2D<TestStreamable>()

        val mbr = spatialIndex2D.getMbr(Point2d(1.0, 2.0), Point2d(3.0, 4.0))

        assertThat(mbr)
                .isEqualTo(Rect2d(1.0, 2.0, 3.0, 4.0))
    }

    private class TestStreamable : SpatialIndexBasedStreamable<TestStreamable, Rect2d>() {

        override fun getBoundingBox(): Rect2d {
            throw UnsupportedOperationException()
        }

        override val priority: Int
            get() = throw UnsupportedOperationException()

        override fun onDestroy() {}

    }

}