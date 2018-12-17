package ch.leadrian.samp.kamp.streamer.runtime.index

import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatiallyIndexedStreamable
import com.conversantmedia.util.collection.geometry.Point3d
import com.conversantmedia.util.collection.geometry.Rect3d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SpatialIndex3DTest {

    @Test
    fun shouldReturnMbr() {
        val spatialIndex3D = SpatialIndex3D<TestStreamable>()

        val mbr = spatialIndex3D.getMbr(Point3d(1.0, 2.0, 3.0), Point3d(4.0, 5.0, 6.0))

        assertThat(mbr)
                .isEqualTo(Rect3d(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
    }

    private class TestStreamable : SpatiallyIndexedStreamable<TestStreamable, Rect3d>() {

        override fun getBoundingBox(): Rect3d {
            throw UnsupportedOperationException()
        }

        override val self: TestStreamable
            get() = throw UnsupportedOperationException()
        override val priority: Int
            get() = throw UnsupportedOperationException()

        override fun onDestroy() {}

    }

}