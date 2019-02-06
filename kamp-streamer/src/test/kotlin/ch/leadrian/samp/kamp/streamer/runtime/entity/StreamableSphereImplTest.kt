package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.sphereOf
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamableSphereImplTest {

    @Test
    fun shouldReturnBoundingBox() {
        val streamableSphere = StreamableSphereImpl(
                sphere = sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f),
                priority = 0,
                interiorIds = mutableSetOf(),
                virtualWorldIds = mutableSetOf(),
                onPlayerEnterStreamableAreaHandler = mockk(),
                onPlayerLeaveStreamableAreaHandler = mockk()
        )

        val rect3d = streamableSphere.boundingBox

        assertThat(rect3d)
                .isEqualTo(Rect3d(-3.0, -2.0, -1.0, 5.0, 6.0, 7.0))
    }

}