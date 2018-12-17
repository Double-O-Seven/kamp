package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectState
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class MapObjectStreamerTest {

    private lateinit var mapObjectStreamer: MapObjectStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val distanceBasedPlayerStreamerFactory = mockk<DistanceBasedPlayerStreamerFactory>()
    private val distanceBasedPlayerStreamer = mockk<DistanceBasedPlayerStreamer<StreamableMapObjectImpl>>()
    private val streamableMapObjectFactory = mockk<StreamableMapObjectFactory>()

    @BeforeEach
    fun setUp() {
        every {
            distanceBasedPlayerStreamerFactory.create<StreamableMapObjectImpl>(any(), any())
        } returns distanceBasedPlayerStreamer
        every { callbackListenerManager.register(any()) } just Runs
        mapObjectStreamer = MapObjectStreamer(
                callbackListenerManager,
                distanceBasedPlayerStreamerFactory,
                streamableMapObjectFactory
        )
    }

    @Nested
    inner class InitializeTests {

        @Test
        fun shouldCreateDistanceBasedPlayerStreamer() {
            mapObjectStreamer.initialize()

            verify {
                distanceBasedPlayerStreamerFactory.create(SAMPConstants.MAX_OBJECTS - 1, mapObjectStreamer)
            }
        }

        @Test
        fun initializeShouldRegisterAsCallbackListener() {
            mapObjectStreamer.initialize()

            verify { callbackListenerManager.register(distanceBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            mapObjectStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { distanceBasedPlayerStreamer.capacity = any() } just Runs

                mapObjectStreamer.capacity = 69

                verify { distanceBasedPlayerStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { distanceBasedPlayerStreamer.capacity } returns 187

                val capacity = mapObjectStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreateMapObjectTests {

            @BeforeEach
            fun setUp() {
                every { distanceBasedPlayerStreamer.beforeStream(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableMapObject() {
                val expectedStreamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(
                            modelId = 1337,
                            priority = 0,
                            streamDistance = 300f,
                            coordinates = vector3DOf(150f, 100f, 20f),
                            rotation = vector3DOf(1f, 2f, 3f),
                            interiorIds = mutableSetOf(123),
                            virtualWorldIds = mutableSetOf(456)
                    )
                } returns expectedStreamableMapObject

                val streamableMapObject = mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(123),
                        virtualWorldIds = mutableSetOf(456)
                )

                assertThat(streamableMapObject)
                        .isEqualTo(expectedStreamableMapObject)
            }

            @Test
            fun shouldRegisterCreatedMapObjectAsCallbackListener() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapObject

                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                verify { callbackListenerManager.register(streamableMapObject) }
            }

            @Test
            fun onDestroyShouldUnregisterCreatedMapObjectAsCallbackListener() {
                every { callbackListenerManager.unregister(any()) } just Runs
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returns streamableMapObject

                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )

                val slot = slot<OnDestroyListener>()
                verify { streamableMapObject.addOnDestroyListener(capture(slot)) }
                slot.captured.onDestroy(streamableMapObject)
                verify { callbackListenerManager.unregister(streamableMapObject) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { distanceBasedPlayerStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            mapObjectStreamer.stream(streamLocations)

            verify { distanceBasedPlayerStreamer.stream(streamLocations) }
        }

        @Nested
        inner class GetStreamInCandidatesTests {

            @BeforeEach
            fun setUp() {
                every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                    firstArg<() -> Unit>().invoke()
                }
            }

            @Test
            fun shouldReturnStreamInCandidates() {
                val streamableMapObject1 = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } just Runs
                    every { getBoundingBox() } returns Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0)
                }
                val streamableMapObject2 = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } just Runs
                    every { getBoundingBox() } returns Rect3d(-20.0, 10.0, -100.0, 101.0, 201.0, 303.0)
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject1, streamableMapObject2)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .containsExactlyInAnyOrder(streamableMapObject1, streamableMapObject2)
            }

            @Test
            fun givenStreamableMapObjectIsDestroyedItShouldNotBeReturnedAsStreamInCandidate() {
                every { callbackListenerManager.unregister(any()) } just Runs
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } answers {
                        every { spatialIndexEntry } returns firstArg()
                    }
                    every { getBoundingBox() } returns Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0)
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val slot = slot<OnDestroyListener>()
                verify { streamableMapObject.addOnDestroyListener(capture(slot)) }
                slot.captured.onDestroy(streamableMapObject)
                val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .isEmpty()
            }

            @ParameterizedTest
            @CsvSource(
                    "true, false",
                    "false, true",
                    "true, true",
                    "false, false"
            )
            fun givenStreamableMapObjectIsDestroyedAfterStateChangeItShouldNotBeReturnedAsStreamInCandidate(isAttached: Boolean, isMoving: Boolean) {
                every { callbackListenerManager.unregister(any()) } just Runs
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } answers {
                        every { spatialIndexEntry } returns firstArg()
                    }
                    every { this@mockk.isAttached } returns isAttached
                    every { this@mockk.isMoving } returns isMoving
                    every { getBoundingBox() } returns Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0)
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val onStateChangeSlot = slot<StreamableMapObjectImpl.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>()
                verify { streamableMapObject.onStateChange(capture(onStateChangeSlot)) }
                onStateChangeSlot.captured.invoke(streamableMapObject, mockk(), mockk())
                val onDestroySlot = slot<OnDestroyListener>()
                verify { streamableMapObject.addOnDestroyListener(capture(onDestroySlot)) }
                onDestroySlot.captured.onDestroy(streamableMapObject)
                val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .isEmpty()
            }

            @ParameterizedTest
            @CsvSource(
                    "true, false",
                    "false, true",
                    "true, true",
                    "false, false"
            )
            fun givenStateChangesItShouldBeReturnAsStreamInCandidate(isAttached: Boolean, isMoving: Boolean) {
                every { callbackListenerManager.unregister(any()) } just Runs
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } answers {
                        every { spatialIndexEntry } returns firstArg()
                    }
                    every { this@mockk.isAttached } returns isAttached
                    every { this@mockk.isMoving } returns isMoving
                    every { getBoundingBox() } returns Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0)
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val slot = slot<StreamableMapObjectImpl.(StreamableMapObjectState, StreamableMapObjectState) -> Unit>()
                verify { streamableMapObject.onStateChange(capture(slot)) }
                slot.captured.invoke(streamableMapObject, mockk(), mockk())
                val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .containsExactlyInAnyOrder(streamableMapObject)
            }

            @Test
            fun givenBoundingBoxChangedAndStreamLocationIsWithinNewBoundingBoxItShouldReturnCandidate() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } answers {
                        every { spatialIndexEntry } returns firstArg()
                    }
                    every { getBoundingBox() } returnsMany listOf(
                            Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0),
                            Rect3d(1075.0, 1175.0, 1275.0, 1140.0, 1240.0, 1340.0)
                    )
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val slot = slot<StreamableMapObjectImpl.(Rect3d) -> Unit>()
                verify { streamableMapObject.onBoundingBoxChanged(capture(slot)) }
                slot.captured.invoke(streamableMapObject, mockk())
                val streamLocation = StreamLocation(mockk(), locationOf(1100f, 1200f, 1300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .containsExactlyInAnyOrder(streamableMapObject)
            }

            @Test
            fun givenBoundingBoxChangedAndStreamLocationIsWithinOldBoundingBoxItShouldNotReturnCandidate() {
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                    every { onBoundingBoxChanged(any()) } just Runs
                    every { onStateChange(any()) } just Runs
                    every { spatialIndexEntry = any() } answers {
                        every { spatialIndexEntry } returns firstArg()
                    }
                    every { getBoundingBox() } returnsMany listOf(
                            Rect3d(75.0, 175.0, 275.0, 140.0, 240.0, 340.0),
                            Rect3d(1075.0, 1175.0, 1275.0, 1140.0, 1240.0, 1340.0)
                    )
                }
                every {
                    streamableMapObjectFactory.create(any(), any(), any(), any(), any(), any(), any())
                } returnsMany listOf(streamableMapObject)
                mapObjectStreamer.createMapObject(
                        modelId = 1337,
                        priority = 0,
                        streamDistance = 300f,
                        coordinates = vector3DOf(150f, 100f, 20f),
                        rotation = vector3DOf(1f, 2f, 3f),
                        interiorIds = mutableSetOf(),
                        virtualWorldIds = mutableSetOf()
                )
                val slot = slot<StreamableMapObjectImpl.(Rect3d) -> Unit>()
                verify { streamableMapObject.onBoundingBoxChanged(capture(slot)) }
                slot.captured.invoke(streamableMapObject, mockk())
                val streamLocation = StreamLocation(mockk(), locationOf(100f, 200f, 300f, 4, 5))

                val candidates = mapObjectStreamer.getStreamInCandidates(streamLocation)

                assertThat(candidates)
                        .isEmpty()
            }

        }

    }

}