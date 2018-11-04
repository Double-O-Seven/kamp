package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage.withPercentage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit

internal class StreamableMapObjectStateTest {

    @Nested
    inner class FixedCoordinatesTests {

        @Test
        fun shouldReturnCoordinates() {
            val state = StreamableMapObjectState.FixedCoordinates(
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f)
            )

            assertThat(state.coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @Test
        fun shouldReturnRotation() {
            val state = StreamableMapObjectState.FixedCoordinates(
                    coordinates = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f)
            )

            assertThat(state.rotation)
                    .isEqualTo(vector3DOf(4f, 5f, 6f))
        }

    }

    @Nested
    inner class MovingTests {

        private val timeProvider = mockk<TimeProvider>()
        private val timerExecutor = mockk<TimerExecutor>()

        @ParameterizedTest
        @CsvSource(
                "1, 2, 3, 935.41434675, 1001, 2002, 3003, 0, 1, 2, 3",
                "1, 2, 3, 935.41434675, 1001, 2002, 3003, 2000, 501, 1002, 1503",
                "1, 2, 3, 935.41434675, 1001, 2002, 3003, 3000, 751, 1502, 2253",
                "1, 2, 3, 935.41434675, 1001, 2002, 3003, 4000, 1001, 2002, 3003",
                "1, 2, 3, 935.41434675, 1001, 2002, 3003, 5678, 1001, 2002, 3003"
        )
        fun shouldReturnExpectedCoordinates(
                originX: Float,
                originY: Float,
                originZ: Float,
                speed: Float,
                destinationX: Float,
                destinationY: Float,
                destinationZ: Float,
                movementTime: Long,
                expectedX: Float,
                expectedY: Float,
                expectedZ: Float
        ) {
            every { timeProvider.getCurrentTimeInMs() } returnsMany listOf(10000L, 10000L + movementTime)
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            val state = StreamableMapObjectState.Moving(
                    origin = vector3DOf(originX, originY, originZ),
                    destination = vector3DOf(destinationX, destinationY, destinationZ),
                    startRotation = vector3DOf(0f, 0f, 0f),
                    targetRotation = null,
                    speed = speed,
                    onMoved = {},
                    timerExecutor = timerExecutor,
                    timeProvider = timeProvider
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .satisfies {
                        assertThat(it.x)
                                .isCloseTo(expectedX, withPercentage(0.005))
                        assertThat(it.y)
                                .isCloseTo(expectedY, withPercentage(0.005))
                        assertThat(it.z)
                                .isCloseTo(expectedZ, withPercentage(0.005))
                    }
        }

        @ParameterizedTest
        @ValueSource(longs = [0, 1, 5, 10, 999])
        fun givenNoTargetRotationItShouldNotChangeRotation(movementTime: Long) {
            every { timeProvider.getCurrentTimeInMs() } returnsMany listOf(10000L, 10000L + movementTime)
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            val state = StreamableMapObjectState.Moving(
                    origin = vector3DOf(0f, 0f, 0f),
                    destination = vector3DOf(1f, 2f, 3f),
                    startRotation = vector3DOf(15f, 20f, 25f),
                    targetRotation = null,
                    speed = 1f,
                    onMoved = {},
                    timerExecutor = timerExecutor,
                    timeProvider = timeProvider
            )

            val rotation = state.rotation

            assertThat(rotation)
                    .isEqualTo(vector3DOf(15f, 20f, 25f))
        }

        @ParameterizedTest
        @CsvSource(
                "0, 30, 60, 90, 90, 180, 180, 30, 60, 90",
                "1000, 30, 60, 90, 90, 180, 180, 42, 84, 108",
                "4000, 30, 60, 90, 90, 180, 180, 78, 156, 162",
                "5000, 30, 60, 90, 90, 180, 180, 90, 180, 180",
                "6789, 30, 60, 90, 90, 180, 180, 90, 180, 180"
        )
        fun givenTargetRotationItShouldRotateObject(
                movementTime: Long,
                startRotationX: Float,
                startRotationY: Float,
                startRotationZ: Float,
                targetRotationX: Float,
                targetRotationY: Float,
                targetRotationZ: Float,
                expectedRotationX: Float,
                expectedRotationY: Float,
                expectedRotationZ: Float
        ) {
            every { timeProvider.getCurrentTimeInMs() } returnsMany listOf(10000L, 10000L + movementTime)
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            val state = StreamableMapObjectState.Moving(
                    origin = vector3DOf(0f, 0f, 0f),
                    destination = vector3DOf(0f, 0f, 5f),
                    startRotation = vector3DOf(startRotationX, startRotationY, startRotationZ),
                    targetRotation = vector3DOf(targetRotationX, targetRotationY, targetRotationZ),
                    speed = 1f,
                    onMoved = {},
                    timerExecutor = timerExecutor,
                    timeProvider = timeProvider
            )

            val rotation = state.rotation

            assertThat(rotation)
                    .satisfies {
                        assertThat(it.x)
                                .isCloseTo(expectedRotationX, withPercentage(0.005))
                        assertThat(it.y)
                                .isCloseTo(expectedRotationY, withPercentage(0.005))
                        assertThat(it.z)
                                .isCloseTo(expectedRotationZ, withPercentage(0.005))
                    }
        }

        @Nested
        inner class TimerTests {

            @ParameterizedTest
            @CsvSource(
                    "0, 0",
                    "2, 2000",
                    "2.5, 2500"
            )
            fun shouldCreateTimer(distance: Float, expectedInterval: Long) {
                every { timeProvider.getCurrentTimeInMs() } returns 0
                every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()

                StreamableMapObjectState.Moving(
                        origin = vector3DOf(0f, 0f, 0f),
                        destination = vector3DOf(0f, 0f, distance),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = null,
                        speed = 1f,
                        onMoved = {},
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )

                verify { timerExecutor.addTimer(expectedInterval, TimeUnit.MILLISECONDS, any()) }
            }

            @Test
            fun timerShouldCallOnMoved() {
                val onMoved = mockk<() -> Unit>(relaxed = true)
                every { timeProvider.getCurrentTimeInMs() } returns 0
                every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
                StreamableMapObjectState.Moving(
                        origin = vector3DOf(0f, 0f, 0f),
                        destination = vector3DOf(0f, 0f, 2f),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = null,
                        speed = 1f,
                        onMoved = onMoved,
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )
                val slot = slot<() -> Unit>()
                verify { timerExecutor.addTimer(any(), any(), capture(slot)) }

                slot.captured.invoke()

                verify { onMoved.invoke() }
            }

            @Test
            fun shouldOnlyCallOnMovedOnce() {
                val onMoved = mockk<() -> Unit>(relaxed = true)
                every { timeProvider.getCurrentTimeInMs() } returns 0
                every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
                StreamableMapObjectState.Moving(
                        origin = vector3DOf(0f, 0f, 0f),
                        destination = vector3DOf(0f, 0f, 2f),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = null,
                        speed = 1f,
                        onMoved = onMoved,
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )
                val slot = slot<() -> Unit>()
                verify { timerExecutor.addTimer(any(), any(), capture(slot)) }

                slot.captured.invoke()
                slot.captured.invoke()

                verify(exactly = 1) { onMoved.invoke() }
            }
        }

        @Test
        fun onStreamInShouldMovePlayerMapObjects() {
            every { timeProvider.getCurrentTimeInMs() } returns 0
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            val playerMapObject = mockk<PlayerMapObject> {
                every { moveTo(any(), any(), any()) } returns 50
            }
            val state = StreamableMapObjectState.Moving(
                    origin = vector3DOf(1f, 2f, 3f),
                    destination = vector3DOf(4f, 5f, 6f),
                    startRotation = vector3DOf(7f, 8f, 9f),
                    targetRotation = vector3DOf(10f, 11f, 12f),
                    speed = 1f,
                    onMoved = {},
                    timerExecutor = timerExecutor,
                    timeProvider = timeProvider
            )

            state.onStreamIn(playerMapObject)

            verify {
                playerMapObject.moveTo(
                        coordinates = vector3DOf(4f, 5f, 6f),
                        speed = 1f,
                        rotation = vector3DOf(10f, 11f, 12f)
                )
            }
        }

        @Test
        fun shouldMovePlayerObjectsOnEnter() {
            every { timeProvider.getCurrentTimeInMs() } returns 0
            every { timerExecutor.addTimer(any(), any(), any()) } returns mockk()
            val playerMapObject1 = mockk<PlayerMapObject> {
                every { moveTo(any(), any(), any()) } returns 50
            }
            val playerMapObject2 = mockk<PlayerMapObject> {
                every { moveTo(any(), any(), any()) } returns 50
            }
            val streamableMapObject = mockk<StreamableMapObjectImpl> {
                every { playerMapObjects } returns listOf(playerMapObject1, playerMapObject2)
            }
            val state = StreamableMapObjectState.Moving(
                    origin = vector3DOf(1f, 2f, 3f),
                    destination = vector3DOf(4f, 5f, 6f),
                    startRotation = vector3DOf(7f, 8f, 9f),
                    targetRotation = vector3DOf(10f, 11f, 12f),
                    speed = 1f,
                    onMoved = {},
                    timerExecutor = timerExecutor,
                    timeProvider = timeProvider
            )

            state.onEnter(streamableMapObject)

            verify {
                playerMapObject1.moveTo(
                        coordinates = vector3DOf(4f, 5f, 6f),
                        speed = 1f,
                        rotation = vector3DOf(10f, 11f, 12f)
                )
                playerMapObject2.moveTo(
                        coordinates = vector3DOf(4f, 5f, 6f),
                        speed = 1f,
                        rotation = vector3DOf(10f, 11f, 12f)
                )
            }
        }

        @Nested
        inner class OnLeaveTests {

            @Test
            fun shouldStopPlayerMapObjects() {
                every { timeProvider.getCurrentTimeInMs() } returns 0
                val timer = mockk<Timer> {
                    every { stop() } just Runs
                }
                every { timerExecutor.addTimer(any(), any(), any()) } returns timer
                val playerMapObject1 = mockk<PlayerMapObject> {
                    every { stop() } just Runs
                }
                val playerMapObject2 = mockk<PlayerMapObject> {
                    every { stop() } just Runs
                }
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { playerMapObjects } returns listOf(playerMapObject1, playerMapObject2)
                }
                val state = StreamableMapObjectState.Moving(
                        origin = vector3DOf(1f, 2f, 3f),
                        destination = vector3DOf(4f, 5f, 6f),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = vector3DOf(10f, 11f, 12f),
                        speed = 1f,
                        onMoved = {},
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )

                state.onLeave(streamableMapObject)

                verify {
                    playerMapObject1.stop()
                    playerMapObject2.stop()
                }
            }

            @Test
            fun shouldStopTimer() {
                every { timeProvider.getCurrentTimeInMs() } returns 0
                val timer = mockk<Timer> {
                    every { stop() } just Runs
                }
                every { timerExecutor.addTimer(any(), any(), any()) } returns timer
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { playerMapObjects } returns listOf()
                }
                val state = StreamableMapObjectState.Moving(
                        origin = vector3DOf(1f, 2f, 3f),
                        destination = vector3DOf(4f, 5f, 6f),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = vector3DOf(10f, 11f, 12f),
                        speed = 1f,
                        onMoved = {},
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )

                state.onLeave(streamableMapObject)

                verify { timer.stop() }
            }

            @Test
            fun shouldNotStopTwice() {
                every { timeProvider.getCurrentTimeInMs() } returns 0
                val timer = mockk<Timer> {
                    every { stop() } just Runs
                }
                every { timerExecutor.addTimer(any(), any(), any()) } returns timer
                val playerMapObject = mockk<PlayerMapObject> {
                    every { stop() } just Runs
                }
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { playerMapObjects } returns listOf(playerMapObject)
                }
                val state = StreamableMapObjectState.Moving(
                        origin = vector3DOf(1f, 2f, 3f),
                        destination = vector3DOf(4f, 5f, 6f),
                        startRotation = vector3DOf(7f, 8f, 9f),
                        targetRotation = vector3DOf(10f, 11f, 12f),
                        speed = 1f,
                        onMoved = {},
                        timerExecutor = timerExecutor,
                        timeProvider = timeProvider
                )

                state.onLeave(streamableMapObject)
                state.onLeave(streamableMapObject)

                verify(exactly = 1) {
                    timer.stop()
                    playerMapObject.stop()
                }
            }
        }
    }

    @Nested
    inner class AttachedToPlayerTests {

        private val player = mockk<Player>()

        @ParameterizedTest
        @CsvSource(
                "100, 200, 300, 5, 10, 15, 0, 105, 210, 315",
                "100, 200, 300, 5, 10, 15, 90, 90, 205, 315",
                "100, 200, 300, 5, 10, 15, 180, 95, 190, 315"
        )
        fun shouldReturnCoordinates(
                playerX: Float,
                playerY: Float,
                playerZ: Float,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                angle: Float,
                expectedX: Float,
                expectedY: Float,
                expectedZ: Float
        ) {
            every { player.coordinates } returns vector3DOf(playerX, playerY, playerZ)
            every { player.angle } returns angle
            val state = StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = vector3DOf(offsetX, offsetY, offsetZ),
                    attachRotation = vector3DOf(0f, 0f, 0f)
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .satisfies {
                        assertThat(it.x)
                                .isCloseTo(expectedX, withPercentage(0.005))
                        assertThat(it.y)
                                .isCloseTo(expectedY, withPercentage(0.005))
                        assertThat(it.z)
                                .isCloseTo(expectedZ, withPercentage(0.005))
                    }
        }

        @Test
        fun shouldReturnRotation() {
            every { player.coordinates } returns vector3DOf(0f, 0f, 0f)
            every { player.angle } returns 15f
            val state = StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = vector3DOf(0f, 0f, 0f),
                    attachRotation = vector3DOf(10f, 20f, 30f)
            )

            val rotation = state.rotation

            assertThat(rotation)
                    .isEqualTo(vector3DOf(10f, 20f, 45f))
        }

        @Test
        fun shouldAttachPlayerMapObjectsOnEnter() {
            val playerMapObject1 = mockk<PlayerMapObject> {
                every { attachTo(any<Player>(), any(), any()) } just Runs
            }
            val playerMapObject2 = mockk<PlayerMapObject> {
                every { attachTo(any<Player>(), any(), any()) } just Runs
            }
            val streamableMapObject = mockk<StreamableMapObjectImpl> {
                every { playerMapObjects } returns listOf(playerMapObject1, playerMapObject2)
            }
            val state = StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onEnter(streamableMapObject)

            verify {
                playerMapObject1.attachTo(
                        player = player,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
                playerMapObject2.attachTo(
                        player = player,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldAttachPlayerMapObjectOnStreamIn() {
            val playerMapObject = mockk<PlayerMapObject> {
                every { attachTo(any<Player>(), any(), any()) } just Runs
            }
            val state = StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onStreamIn(playerMapObject)

            verify {
                playerMapObject.attachTo(
                        player = player,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldDestroyPlayerMapObjectsOnLeave() {
            val streamableMapObject = mockk<StreamableMapObjectImpl> {
                every { destroyPlayerMapObjects() } just Runs
            }
            val state = StreamableMapObjectState.Attached.ToPlayer(
                    player = player,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onLeave(streamableMapObject)

            verify { streamableMapObject.destroyPlayerMapObjects() }
        }
    }

    @Nested
    inner class AttachedToVehicleTests {

        private val vehicle = mockk<Vehicle>()

        @ParameterizedTest
        @CsvSource(
                "100, 200, 300, 5, 10, 15, 0, 105, 210, 315",
                "100, 200, 300, 5, 10, 15, 90, 90, 205, 315",
                "100, 200, 300, 5, 10, 15, 180, 95, 190, 315"
        )
        fun shouldReturnCoordinates(
                vehicleX: Float,
                vehicleY: Float,
                vehicleZ: Float,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                angle: Float,
                expectedX: Float,
                expectedY: Float,
                expectedZ: Float
        ) {
            every { vehicle.coordinates } returns vector3DOf(vehicleX, vehicleY, vehicleZ)
            every { vehicle.angle } returns angle
            val state = StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = vector3DOf(offsetX, offsetY, offsetZ),
                    attachRotation = vector3DOf(0f, 0f, 0f)
            )

            val coordinates = state.coordinates

            assertThat(coordinates)
                    .satisfies {
                        assertThat(it.x)
                                .isCloseTo(expectedX, withPercentage(0.005))
                        assertThat(it.y)
                                .isCloseTo(expectedY, withPercentage(0.005))
                        assertThat(it.z)
                                .isCloseTo(expectedZ, withPercentage(0.005))
                    }
        }

        @Test
        fun shouldReturnRotation() {
            every { vehicle.coordinates } returns vector3DOf(0f, 0f, 0f)
            every { vehicle.angle } returns 15f
            val state = StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = vector3DOf(0f, 0f, 0f),
                    attachRotation = vector3DOf(10f, 20f, 30f)
            )

            val rotation = state.rotation

            assertThat(rotation)
                    .isEqualTo(vector3DOf(10f, 20f, 45f))
        }

        @Test
        fun shouldAttachplayerMapObjectsOnEnter() {
            val playerMapObject1 = mockk<PlayerMapObject> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            val playerMapObject2 = mockk<PlayerMapObject> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            val streamableMapObject = mockk<StreamableMapObjectImpl> {
                every { playerMapObjects } returns listOf(playerMapObject1, playerMapObject2)
            }
            val state = StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onEnter(streamableMapObject)

            verify {
                playerMapObject1.attachTo(
                        vehicle = vehicle,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
                playerMapObject2.attachTo(
                        vehicle = vehicle,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldAttachplayerMapObjectOnStreamIn() {
            val playerMapObject = mockk<PlayerMapObject> {
                every { attachTo(any<Vehicle>(), any(), any()) } just Runs
            }
            val state = StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onStreamIn(playerMapObject)

            verify {
                playerMapObject.attachTo(
                        vehicle = vehicle,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun shouldDestroyPlayerMapObjectsOnLeave() {
            val streamableMapObject = mockk<StreamableMapObjectImpl> {
                every { destroyPlayerMapObjects() } just Runs
            }
            val state = StreamableMapObjectState.Attached.ToVehicle(
                    vehicle = vehicle,
                    offset = vector3DOf(1f, 2f, 3f),
                    attachRotation = vector3DOf(4f, 5f, 6f)
            )

            state.onLeave(streamableMapObject)

            verify { streamableMapObject.destroyPlayerMapObjects() }
        }
    }

}