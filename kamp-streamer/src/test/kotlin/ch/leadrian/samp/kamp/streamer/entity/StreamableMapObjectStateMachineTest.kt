package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamableMapObjectStateMachineTest {

    private val streamableMapObject = mockk<StreamableMapObject>()
    private val streamableMapObjectStateFactory = mockk<StreamableMapObjectStateFactory>()

    @Nested
    inner class FixedCoordinatesTests {

        private lateinit var streamableMapObjectStateMachine: StreamableMapObjectStateMachine
        private val initialState = mockk<StreamableMapObjectState>()
        private val fixedCoordinates = mockk<StreamableMapObjectState.FixedCoordinates>()
        private val coordinates = vector3DOf(1f, 2f, 3f)
        private val rotation = vector3DOf(4f, 5f, 6f)

        @BeforeEach
        fun setUp() {
            every { initialState.onLeave(any()) } just Runs
            every { fixedCoordinates.onEnter(any()) } just Runs
            every {
                streamableMapObjectStateFactory.createFixedCoordinates(
                        coordinates = coordinates,
                        rotation = rotation
                )
            } returns fixedCoordinates
            streamableMapObjectStateMachine = StreamableMapObjectStateMachine(
                    initialState = initialState,
                    streamableMapObject = streamableMapObject,
                    streamableMapObjectStateFactory = streamableMapObjectStateFactory
            )
        }

        @Test
        fun shouldCallOnLeaveAndThenOnEnter() {
            streamableMapObjectStateMachine.transitionToFixedCoordinates(
                    coordinates = coordinates,
                    rotation = rotation
            )

            verifyOrder {
                initialState.onLeave(streamableMapObject)
                fixedCoordinates.onEnter(streamableMapObject)
            }
        }

        @Test
        fun shouldUpdateCurrentState() {
            streamableMapObjectStateMachine.transitionToFixedCoordinates(
                    coordinates = coordinates,
                    rotation = rotation
            )

            assertThat(streamableMapObjectStateMachine.currentState)
                    .isSameAs(fixedCoordinates)
        }

        @Test
        fun shouldCallOnStateChangeHandlers() {
            val onStateChange1 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange1)
            val onStateChange2 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange2)

            streamableMapObjectStateMachine.transitionToFixedCoordinates(
                    coordinates = coordinates,
                    rotation = rotation
            )

            verify {
                onStateChange1.invoke(streamableMapObject, initialState, fixedCoordinates)
                onStateChange2.invoke(streamableMapObject, initialState, fixedCoordinates)
            }
        }
    }

    @Nested
    inner class MovingTests {

        private lateinit var streamableMapObjectStateMachine: StreamableMapObjectStateMachine
        private val initialState = mockk<StreamableMapObjectState>()
        private val moving = mockk<StreamableMapObjectState.Moving>()
        private val origin = vector3DOf(1f, 2f, 3f)
        private val destination = vector3DOf(4f, 5f, 6f)
        private val startRotation = vector3DOf(7f, 8f, 9f)
        private val targetRotation = vector3DOf(10f, 11f, 12f)

        @BeforeEach
        fun setUp() {
            every { initialState.onLeave(any()) } just Runs
            every { moving.onEnter(any()) } just Runs
            every {
                streamableMapObjectStateFactory.createMoving(
                        origin = origin,
                        destination = destination,
                        startRotation = startRotation,
                        targetRotation = targetRotation,
                        speed = 13f,
                        onMoved = any()
                )
            } returns moving
            streamableMapObjectStateMachine = StreamableMapObjectStateMachine(
                    initialState = initialState,
                    streamableMapObject = streamableMapObject,
                    streamableMapObjectStateFactory = streamableMapObjectStateFactory
            )
        }

        @Test
        fun shouldCallOnLeaveAndThenOnEnter() {
            streamableMapObjectStateMachine.transitionToMoving(
                    origin = origin,
                    destination = destination,
                    startRotation = startRotation,
                    targetRotation = targetRotation,
                    speed = 13f
            )

            verifyOrder {
                initialState.onLeave(streamableMapObject)
                moving.onEnter(streamableMapObject)
            }
        }

        @Test
        fun shouldUpdateCurrentState() {
            streamableMapObjectStateMachine.transitionToMoving(
                    origin = origin,
                    destination = destination,
                    startRotation = startRotation,
                    targetRotation = targetRotation,
                    speed = 13f
            )

            assertThat(streamableMapObjectStateMachine.currentState)
                    .isSameAs(moving)
        }

        @Test
        fun shouldCallOnStateChangeHandlers() {
            val onStateChange1 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange1)
            val onStateChange2 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange2)

            streamableMapObjectStateMachine.transitionToMoving(
                    origin = origin,
                    destination = destination,
                    startRotation = startRotation,
                    targetRotation = targetRotation,
                    speed = 13f
            )

            verify {
                onStateChange1.invoke(streamableMapObject, initialState, moving)
                onStateChange2.invoke(streamableMapObject, initialState, moving)
            }
        }

        @Test
        fun shouldSetStreamableMapObjectOnMovedAsOnMoved() {
            every { streamableMapObject.onMoved() } just Runs

            streamableMapObjectStateMachine.transitionToMoving(
                    origin = origin,
                    destination = destination,
                    startRotation = startRotation,
                    targetRotation = targetRotation,
                    speed = 13f
            )

            val slot = slot<() -> Unit>()
            verify {
                streamableMapObjectStateFactory.createMoving(
                        origin = any(),
                        destination = any(),
                        startRotation = any(),
                        targetRotation = any(),
                        speed = any(),
                        onMoved = capture(slot)
                )
            }
            slot.captured.invoke()
            verify { streamableMapObject.onMoved() }
        }
    }

    @Nested
    inner class AttachedToPlayerTests {

        private lateinit var streamableMapObjectStateMachine: StreamableMapObjectStateMachine
        private val initialState = mockk<StreamableMapObjectState>()
        private val attachedToPlayer = mockk<StreamableMapObjectState.Attached.ToPlayer>()
        private val offset = vector3DOf(1f, 2f, 3f)
        private val attachRotation = vector3DOf(4f, 5f, 6f)
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { initialState.onLeave(any()) } just Runs
            every { attachedToPlayer.onEnter(any()) } just Runs
            every {
                streamableMapObjectStateFactory.createAttachedToPlayer(
                        player = player,
                        offset = offset,
                        attachRotation = attachRotation
                )
            } returns attachedToPlayer
            streamableMapObjectStateMachine = StreamableMapObjectStateMachine(
                    initialState = initialState,
                    streamableMapObject = streamableMapObject,
                    streamableMapObjectStateFactory = streamableMapObjectStateFactory
            )
        }

        @Test
        fun shouldCallOnLeaveAndThenOnEnter() {
            streamableMapObjectStateMachine.transitionToAttachedToPlayer(
                    player = player,
                    offset = offset,
                    rotation = attachRotation
            )

            verifyOrder {
                initialState.onLeave(streamableMapObject)
                attachedToPlayer.onEnter(streamableMapObject)
            }
        }

        @Test
        fun shouldUpdateCurrentState() {
            streamableMapObjectStateMachine.transitionToAttachedToPlayer(
                    player = player,
                    offset = offset,
                    rotation = attachRotation
            )

            assertThat(streamableMapObjectStateMachine.currentState)
                    .isSameAs(attachedToPlayer)
        }

        @Test
        fun shouldCallOnStateChangeHandlers() {
            val onStateChange1 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange1)
            val onStateChange2 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange2)

            streamableMapObjectStateMachine.transitionToAttachedToPlayer(
                    player = player,
                    offset = offset,
                    rotation = attachRotation
            )

            verify {
                onStateChange1.invoke(streamableMapObject, initialState, attachedToPlayer)
                onStateChange2.invoke(streamableMapObject, initialState, attachedToPlayer)
            }
        }
    }

    @Nested
    inner class AttachedToVehicleTests {

        private lateinit var streamableMapObjectStateMachine: StreamableMapObjectStateMachine
        private val initialState = mockk<StreamableMapObjectState>()
        private val attachedToVehicle = mockk<StreamableMapObjectState.Attached.ToVehicle>()
        private val offset = vector3DOf(1f, 2f, 3f)
        private val attachRotation = vector3DOf(4f, 5f, 6f)
        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { initialState.onLeave(any()) } just Runs
            every { attachedToVehicle.onEnter(any()) } just Runs
            every {
                streamableMapObjectStateFactory.createAttachedToVehicle(
                        vehicle = vehicle,
                        offset = offset,
                        attachRotation = attachRotation
                )
            } returns attachedToVehicle
            streamableMapObjectStateMachine = StreamableMapObjectStateMachine(
                    initialState = initialState,
                    streamableMapObject = streamableMapObject,
                    streamableMapObjectStateFactory = streamableMapObjectStateFactory
            )
        }

        @Test
        fun shouldCallOnLeaveAndThenOnEnter() {
            streamableMapObjectStateMachine.transitionToAttachedToVehicle(
                    vehicle = vehicle,
                    offset = offset,
                    rotation = attachRotation
            )

            verifyOrder {
                initialState.onLeave(streamableMapObject)
                attachedToVehicle.onEnter(streamableMapObject)
            }
        }

        @Test
        fun shouldUpdateCurrentState() {
            streamableMapObjectStateMachine.transitionToAttachedToVehicle(
                    vehicle = vehicle,
                    offset = offset,
                    rotation = attachRotation
            )

            assertThat(streamableMapObjectStateMachine.currentState)
                    .isSameAs(attachedToVehicle)
        }

        @Test
        fun shouldCallOnStateChangeHandlers() {
            val onStateChange1 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange1)
            val onStateChange2 = mockk<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>(relaxed = true)
            streamableMapObjectStateMachine.onStateChange(onStateChange2)

            streamableMapObjectStateMachine.transitionToAttachedToVehicle(
                    vehicle = vehicle,
                    offset = offset,
                    rotation = attachRotation
            )

            verify {
                onStateChange1.invoke(streamableMapObject, initialState, attachedToVehicle)
                onStateChange2.invoke(streamableMapObject, initialState, attachedToVehicle)
            }
        }
    }

}