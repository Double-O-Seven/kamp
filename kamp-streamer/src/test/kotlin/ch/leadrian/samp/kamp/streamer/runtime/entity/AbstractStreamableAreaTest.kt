package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.boxOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaReceiverDelegate
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class AbstractStreamableAreaTest {

    private lateinit var streamableArea: TestStreamableArea
    private val onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler = mockk()
    private val onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler = mockk()
    private val onPlayerEnterStreamableAreaReceiver: OnPlayerEnterStreamableAreaReceiverDelegate = mockk()
    private val onPlayerLeaveStreamableAreaReceiver: OnPlayerLeaveStreamableAreaReceiverDelegate = mockk()

    @BeforeEach
    fun setUp() {
        streamableArea = TestStreamableArea(
                0,
                onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler,
                onPlayerEnterStreamableAreaReceiver,
                onPlayerLeaveStreamableAreaReceiver
        )
    }

    @Nested
    inner class OnEnterTests {

        private val player: Player = mockk()

        @BeforeEach
        fun setUp() {
            every { onPlayerEnterStreamableAreaReceiver.onPlayerEnterStreamableArea(any(), any()) } just Runs
            every { onPlayerEnterStreamableAreaHandler.onPlayerEnterStreamableArea(any(), any()) } just Runs
            streamableArea.onEnter(player)
        }

        @Test
        fun shouldCallReceiver() {
            verify { onPlayerEnterStreamableAreaReceiver.onPlayerEnterStreamableArea(player, streamableArea) }
        }

        @Test
        fun shouldCallHandler() {
            verify { onPlayerEnterStreamableAreaHandler.onPlayerEnterStreamableArea(player, streamableArea) }
        }
    }

    @Nested
    inner class OnLeaveTests {

        private val player: Player = mockk()

        @BeforeEach
        fun setUp() {
            every { onPlayerLeaveStreamableAreaReceiver.onPlayerLeaveStreamableArea(any(), any()) } just Runs
            every { onPlayerLeaveStreamableAreaHandler.onPlayerLeaveStreamableArea(any(), any()) } just Runs
            streamableArea.onLeave(player)
        }

        @Test
        fun shouldCallReceiver() {
            verify { onPlayerLeaveStreamableAreaReceiver.onPlayerLeaveStreamableArea(player, streamableArea) }
        }

        @Test
        fun shouldCallHandler() {
            verify { onPlayerLeaveStreamableAreaHandler.onPlayerLeaveStreamableArea(player, streamableArea) }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ContainsLocationArgumentsProvider::class)
    fun containsLocationShouldReturnTrueIfAndOnlyIfLocationIsInAreaAndInteriorIdAndVirtualWorldsMatch(
            location: Location,
            area: Box,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            expectedContains: Boolean
    ) {
        streamableArea.area = area
        streamableArea.interiorIds = interiorIds
        streamableArea.virtualWorldIds = virtualWorldIds

        val contains = streamableArea.contains(location)

        assertThat(contains)
                .isEqualTo(expectedContains)
    }

    private class ContainsLocationArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<ContainsLocationArguments> =
                Stream.of(
                        ContainsLocationArguments(
                                location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(),
                                virtualWorldIds = mutableSetOf(),
                                expectedContains = true
                        ),
                        ContainsLocationArguments(
                                location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(4),
                                virtualWorldIds = mutableSetOf(5),
                                expectedContains = true
                        ),
                        ContainsLocationArguments(
                                location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(1337),
                                virtualWorldIds = mutableSetOf(5),
                                expectedContains = false
                        ),
                        ContainsLocationArguments(
                                location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(4),
                                virtualWorldIds = mutableSetOf(69),
                                expectedContains = false
                        ),
                        ContainsLocationArguments(
                                location = locationOf(x = 100f, y = 200f, z = 300f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(),
                                virtualWorldIds = mutableSetOf(),
                                expectedContains = false
                        ),
                        ContainsLocationArguments(
                                location = locationOf(x = 100f, y = 200f, z = 300f, interiorId = 4, worldId = 5),
                                area = boxOf(-10f, 10f, -20f, 20f, -30f, 30f),
                                interiorIds = mutableSetOf(4),
                                virtualWorldIds = mutableSetOf(5),
                                expectedContains = false
                        )
                )

    }

    private class ContainsLocationArguments(
            private val location: Location,
            private val area: Box,
            private val interiorIds: MutableSet<Int>,
            private val virtualWorldIds: MutableSet<Int>,
            private val expectedContains: Boolean
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(location, area, interiorIds, virtualWorldIds, expectedContains)

    }

    private class TestStreamableArea(
            priority: Int,
            onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
            onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler,
            onPlayerEnterStreamableAreaReceiver: OnPlayerEnterStreamableAreaReceiverDelegate,
            onPlayerLeaveStreamableAreaReceiver: OnPlayerLeaveStreamableAreaReceiverDelegate,
            var area: Box = boxOf(0f, 0f, 0f, 0f, 0f, 0f)
    ) : AbstractStreamableArea<Rect3d>(
            interiorIds = mutableSetOf(),
            virtualWorldIds = mutableSetOf(),
            priority = priority,
            onPlayerEnterStreamableAreaHandler = onPlayerEnterStreamableAreaHandler,
            onPlayerLeaveStreamableAreaHandler = onPlayerLeaveStreamableAreaHandler,
            onPlayerEnterStreamableAreaReceiver = onPlayerEnterStreamableAreaReceiver,
            onPlayerLeaveStreamableAreaReceiver = onPlayerLeaveStreamableAreaReceiver
    ) {

        override val boundingBox: Rect3d
            get() {
                throw UnsupportedOperationException()
            }

        override fun contains(player: Player): Boolean {
            throw UnsupportedOperationException()
        }

        override fun contains(coordinates: Vector3D): Boolean = coordinates in area

        override fun onDestroy() {
            throw UnsupportedOperationException()
        }

    }

}