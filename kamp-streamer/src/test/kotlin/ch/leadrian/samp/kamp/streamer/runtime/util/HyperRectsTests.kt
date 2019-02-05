package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.boxOf
import ch.leadrian.samp.kamp.core.api.data.circleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import com.conversantmedia.util.collection.geometry.Rect2d
import com.conversantmedia.util.collection.geometry.Rect3d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HyperRectsTests {

    @Test
    fun shouldConvertVector3DToRect3d() {
        val vector = vector3DOf(1f, 2f, 3f)

        val rect3d = vector.toRect3d(10f)

        assertThat(rect3d)
                .isEqualTo(Rect3d(-9.0, -8.0, -7.0, 11.0, 12.0, 13.0))
    }

    @Test
    fun shouldConvertSphereToRect3d() {
        val sphere = sphereOf(1f, 2f, 3f, 10f)

        val rect3d = sphere.toRect3d()

        assertThat(rect3d)
                .isEqualTo(Rect3d(-9.0, -8.0, -7.0, 11.0, 12.0, 13.0))
    }

    @Test
    fun shouldConvertCircleToRect3d() {
        val circle = circleOf(1f, 2f, 10f)

        val rect3d = circle.toRect2d()

        assertThat(rect3d)
                .isEqualTo(Rect2d(-9.0, -8.0, 11.0, 12.0))
    }

    @Test
    fun shouldConvertRectangleToRect2d() {
        val rectangle = rectangleOf(minX = 1f, minY = 2f, maxX = 3f, maxY = 4f)

        val rect2d = rectangle.toRect2d()

        assertThat(rect2d)
                .isEqualTo(Rect2d(1.0, 2.0, 3.0, 4.0))
    }

    @Test
    fun shouldConvertBoxToRect3d() {
        val box = boxOf(minX = 1f, minY = 2f, minZ = 3f, maxX = 4f, maxY = 5f, maxZ = 6f)

        val rect3d = box.toRect3d()

        assertThat(rect3d)
                .isEqualTo(Rect3d(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
    }

}