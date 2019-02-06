package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.boxOf
import ch.leadrian.samp.kamp.core.api.data.circleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.streamer.runtime.Area2DStreamer
import ch.leadrian.samp.kamp.streamer.runtime.Area3DStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableBoxImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCircleImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRectangleImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableSphereImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamableAreaServiceTest {

    private lateinit var streamableAreaService: StreamableAreaService
    private val area2DStreamer: Area2DStreamer = mockk()
    private val area3DStreamer: Area3DStreamer = mockk()
    private val onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler = mockk()
    private val onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler = mockk()

    @BeforeEach
    fun setUp() {
        streamableAreaService = StreamableAreaService(
                area2DStreamer,
                area3DStreamer,
                onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler
        )
    }

    @Nested
    inner class CreateStreamableRectangleTests {

        @BeforeEach
        fun setUp() {
            every { area2DStreamer.add(any()) } just Runs
        }

        @Test
        fun shouldCreateStreamableRectangleUsingSetOfInteriorIdsAndVirtualWorldIds() {
            val streamableRectangle = streamableAreaService.createStreamableRectangle(
                    rectangle = rectangleOf(minX = -1f, maxX = 2f, minY = -3f, maxY = 4f),
                    interiorIds = mutableSetOf(69),
                    virtualWorldIds = mutableSetOf(1337)
            )

            assertAll(
                    { assertThat(streamableRectangle.minX).isEqualTo(-1f) },
                    { assertThat(streamableRectangle.maxX).isEqualTo(2f) },
                    { assertThat(streamableRectangle.minY).isEqualTo(-3f) },
                    { assertThat(streamableRectangle.maxY).isEqualTo(4f) },
                    { assertThat(streamableRectangle.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableRectangle.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area2DStreamer.add(streamableRectangle as StreamableRectangleImpl) } }
            )
        }

        @Test
        fun shouldCreateStreamableRectangleUsingInteriorIdAndVirtualWorldId() {
            val streamableRectangle = streamableAreaService.createStreamableRectangle(
                    rectangle = rectangleOf(minX = -1f, maxX = 2f, minY = -3f, maxY = 4f),
                    interiorId = 69,
                    virtualWorldId = 1337
            )

            assertAll(
                    { assertThat(streamableRectangle.minX).isEqualTo(-1f) },
                    { assertThat(streamableRectangle.maxX).isEqualTo(2f) },
                    { assertThat(streamableRectangle.minY).isEqualTo(-3f) },
                    { assertThat(streamableRectangle.maxY).isEqualTo(4f) },
                    { assertThat(streamableRectangle.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableRectangle.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area2DStreamer.add(streamableRectangle as StreamableRectangleImpl) } }
            )
        }

    }

    @Nested
    inner class CreateStreamableCircleTests {

        @BeforeEach
        fun setUp() {
            every { area2DStreamer.add(any()) } just Runs
        }

        @Test
        fun shouldCreateStreamableCircleUsingSetOfInteriorIdsAndVirtualWorldIds() {
            val streamableCircle = streamableAreaService.createStreamableCircle(
                    circle = circleOf(x = -1f, y = 2f, radius = -3f),
                    interiorIds = mutableSetOf(69),
                    virtualWorldIds = mutableSetOf(1337)
            )

            assertAll(
                    { assertThat(streamableCircle.x).isEqualTo(-1f) },
                    { assertThat(streamableCircle.y).isEqualTo(2f) },
                    { assertThat(streamableCircle.radius).isEqualTo(-3f) },
                    { assertThat(streamableCircle.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableCircle.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area2DStreamer.add(streamableCircle as StreamableCircleImpl) } }
            )
        }

        @Test
        fun shouldCreateStreamableCircleUsingInteriorIdAndVirtualWorldId() {
            val streamableCircle = streamableAreaService.createStreamableCircle(
                    circle = circleOf(x = -1f, y = 2f, radius = -3f),
                    interiorId = 69,
                    virtualWorldId = 1337
            )

            assertAll(
                    { assertThat(streamableCircle.x).isEqualTo(-1f) },
                    { assertThat(streamableCircle.y).isEqualTo(2f) },
                    { assertThat(streamableCircle.radius).isEqualTo(-3f) },
                    { assertThat(streamableCircle.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableCircle.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area2DStreamer.add(streamableCircle as StreamableCircleImpl) } }
            )
        }

    }

    @Nested
    inner class CreateStreamableBoxTests {

        @BeforeEach
        fun setUp() {
            every { area3DStreamer.add(any()) } just Runs
        }

        @Test
        fun shouldCreateStreamableBoxUsingSetOfInteriorIdsAndVirtualWorldIds() {
            val streamableBox = streamableAreaService.createStreamableBox(
                    box = boxOf(minX = -1f, maxX = 2f, minY = -3f, maxY = 4f, minZ = -5f, maxZ = 6f),
                    interiorIds = mutableSetOf(69),
                    virtualWorldIds = mutableSetOf(1337)
            )

            assertAll(
                    { assertThat(streamableBox.minX).isEqualTo(-1f) },
                    { assertThat(streamableBox.maxX).isEqualTo(2f) },
                    { assertThat(streamableBox.minY).isEqualTo(-3f) },
                    { assertThat(streamableBox.maxY).isEqualTo(4f) },
                    { assertThat(streamableBox.minZ).isEqualTo(-5f) },
                    { assertThat(streamableBox.maxZ).isEqualTo(6f) },
                    { assertThat(streamableBox.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableBox.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area3DStreamer.add(streamableBox as StreamableBoxImpl) } }
            )
        }

        @Test
        fun shouldCreateStreamableBoxUsingInteriorIdAndVirtualWorldId() {
            val streamableBox = streamableAreaService.createStreamableBox(
                    box = boxOf(minX = -1f, maxX = 2f, minY = -3f, maxY = 4f, minZ = -5f, maxZ = 6f),
                    interiorId = 69,
                    virtualWorldId = 1337
            )

            assertAll(
                    { assertThat(streamableBox.minX).isEqualTo(-1f) },
                    { assertThat(streamableBox.maxX).isEqualTo(2f) },
                    { assertThat(streamableBox.minY).isEqualTo(-3f) },
                    { assertThat(streamableBox.maxY).isEqualTo(4f) },
                    { assertThat(streamableBox.minZ).isEqualTo(-5f) },
                    { assertThat(streamableBox.maxZ).isEqualTo(6f) },
                    { assertThat(streamableBox.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableBox.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area3DStreamer.add(streamableBox as StreamableBoxImpl) } }
            )
        }

    }

    @Nested
    inner class CreateStreamableSphereTests {

        @BeforeEach
        fun setUp() {
            every { area3DStreamer.add(any()) } just Runs
        }

        @Test
        fun shouldCreateStreamableSphereUsingSetOfInteriorIdsAndVirtualWorldIds() {
            val streamableSphere = streamableAreaService.createStreamableSphere(
                    sphere = sphereOf(x = -1f, y = 2f, z = -3f, radius = 4f),
                    interiorIds = mutableSetOf(69),
                    virtualWorldIds = mutableSetOf(1337)
            )

            assertAll(
                    { assertThat(streamableSphere.x).isEqualTo(-1f) },
                    { assertThat(streamableSphere.y).isEqualTo(2f) },
                    { assertThat(streamableSphere.z).isEqualTo(-3f) },
                    { assertThat(streamableSphere.radius).isEqualTo(4f) },
                    { assertThat(streamableSphere.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableSphere.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area3DStreamer.add(streamableSphere as StreamableSphereImpl) } }
            )
        }

        @Test
        fun shouldCreateStreamableSphereUsingInteriorIdAndVirtualWorldId() {
            val streamableSphere = streamableAreaService.createStreamableSphere(
                    sphere = sphereOf(x = -1f, y = 2f, z = -3f, radius = 4f),
                    interiorId = 69,
                    virtualWorldId = 1337
            )

            assertAll(
                    { assertThat(streamableSphere.x).isEqualTo(-1f) },
                    { assertThat(streamableSphere.y).isEqualTo(2f) },
                    { assertThat(streamableSphere.z).isEqualTo(-3f) },
                    { assertThat(streamableSphere.radius).isEqualTo(4f) },
                    { assertThat(streamableSphere.interiorIds).containsExactlyInAnyOrder(69) },
                    { assertThat(streamableSphere.virtualWorldIds).containsExactlyInAnyOrder(1337) },
                    { verify { area3DStreamer.add(streamableSphere as StreamableSphereImpl) } }
            )
        }

    }
}