package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import com.conversantmedia.util.collection.geometry.Rect3d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VectorsTest {

    @Test
    fun shouldReturnRect3d() {
        val vector = vector3DOf(1f, 2f, 3f)

        val rect3d = vector.toRect3d(10f)

        assertThat(rect3d)
                .isEqualTo(Rect3d(-9.0, -8.0, -7.0, 11.0, 12.0, 13.0))
    }

}