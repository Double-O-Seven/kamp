package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.onEnter
import ch.leadrian.samp.kamp.streamer.api.callback.onLeave
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableArea
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.AbstractStreamableArea
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex2D
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect2d
import com.conversantmedia.util.collection.geometry.Rect2d
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AreaStreamerTest {

    private lateinit var areaStreamer: AreaStreamer<Rect2d>
    private val asyncExecutor: AsyncExecutor = mockk()

    @BeforeEach
    fun setUp() {
        every { asyncExecutor.executeOnMainThread(any()) } answers {
            firstArg<() -> Unit>().invoke()
        }
        areaStreamer = TestAreaStreamer(asyncExecutor)
    }

    @Test
    fun shouldCallOnEnter() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onEnter(onEnter)
        areaStreamer.add(streamableArea)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onEnter(streamableArea, player) }
    }

    @Test
    fun givenAreaIsNotActiveForPlayerItShouldCallNotOnEnter() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        streamableArea.activeWhen { p -> p != player }
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onEnter(onEnter)
        areaStreamer.add(streamableArea)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onEnter wasNot Called }
    }

    @Test
    fun shouldCallOnEnterAccordingToPriorities() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea1 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 2)
        val streamableArea2 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 3)
        val streamableArea3 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 1)
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea1.onEnter(onEnter)
        areaStreamer.add(streamableArea1)
        streamableArea2.onEnter(onEnter)
        areaStreamer.add(streamableArea2)
        streamableArea3.onEnter(onEnter)
        areaStreamer.add(streamableArea3)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verifyOrder {
            onEnter(streamableArea2, player)
            onEnter(streamableArea1, player)
            onEnter(streamableArea3, player)
        }
    }

    @Test
    fun shouldNotCallOnEnterWhenPlayerIsAlreadyInArea() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onEnter(onEnter)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onEnter wasNot Called }
    }

    @Test
    fun givenAreaWasDestroyItShouldNotCallOnEnter() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onEnter(onEnter)
        areaStreamer.add(streamableArea)
        streamableArea.destroy()

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onEnter wasNot Called }
    }

    @Test
    fun shouldNotCallOnLeaveWhenAreaIsEntered() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onLeave wasNot Called }
    }

    @Test
    fun givenAreaWasEnteredItShouldCallOnLeave() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verify { onLeave(streamableArea, player) }
    }

    @Test
    fun givenAreaIsNoLongerActiveForPlayerItShouldCallOnLeave() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))
        streamableArea.activeWhen { p -> p != player }

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        verify { onLeave(streamableArea, player) }
    }

    @Test
    fun shouldNotCallOnEnterWhenAreaIsLeft() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))
        val onEnter = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onEnter(onEnter)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verify { onEnter wasNot Called }
    }

    @Test
    fun givenAreaWasDestroyedItShouldNotCallOnLeave() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))
        streamableArea.destroy()

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verify { onLeave wasNot Called }
    }

    @Test
    fun givenPlayerHasDisconnectedItShouldNotCallOnLeave() {
        val player: Player = mockk {
            every { isConnected } returnsMany listOf(true, false)
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))
        areaStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT)

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verify { onLeave wasNot Called }
    }

    @Test
    fun givenPlayerHasDisconnectedDuringStreamingItShouldNotCallOnLeave() {
        val player: Player = mockk {
            every { isConnected } returnsMany listOf(true, false)
        }
        val streamableArea = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f))
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea.onLeave(onLeave)
        areaStreamer.add(streamableArea)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verify { onLeave wasNot Called }
    }

    @Test
    fun shouldCallOnLeaveAccordingToPriorities() {
        val player: Player = mockk {
            every { isConnected } returns true
        }
        val streamableArea1 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 2)
        val streamableArea2 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 3)
        val streamableArea3 = TestStreamableArea(rectangleOf(-10f, 20f, 60f, 100f), priority = 1)
        val onLeave = mockk<StreamableArea.(Player) -> Unit>(relaxed = true)
        streamableArea1.onLeave(onLeave)
        areaStreamer.add(streamableArea1)
        streamableArea2.onLeave(onLeave)
        areaStreamer.add(streamableArea2)
        streamableArea3.onLeave(onLeave)
        areaStreamer.add(streamableArea3)
        areaStreamer.stream(listOf(StreamLocation(player, locationOf(15f, 75f, 0f, 0, 0))))

        areaStreamer.stream(listOf(StreamLocation(player, locationOf(150f, 750f, 0f, 0, 0))))

        verifyOrder {
            onLeave(streamableArea2, player)
            onLeave(streamableArea1, player)
            onLeave(streamableArea3, player)
        }
    }

    private class TestStreamableArea(
            private val area: Rectangle,
            priority: Int = 0,
            onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler = mockk(relaxed = true),
            onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler = mockk(relaxed = true)
    ) : AbstractStreamableArea<Rect2d>(
            mutableSetOf(),
            mutableSetOf(),
            priority,
            onPlayerEnterStreamableAreaHandler,
            onPlayerLeaveStreamableAreaHandler
    ) {

        override val boundingBox: Rect2d
            get() = area.toRect2d()

        override fun contains(player: Player): Boolean {
            throw UnsupportedOperationException()
        }

        override fun contains(coordinates: Vector3D): Boolean = coordinates in area

        override fun onDestroy() {
        }

    }

    private class TestAreaStreamer(asyncExecutor: AsyncExecutor) : AreaStreamer<Rect2d>(asyncExecutor, SpatialIndex2D())

}