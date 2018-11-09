package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
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
            every { distanceBasedPlayerStreamer.beforeStream(any()) } just Runs
        }

        @Nested
        inner class CallbackListenerTests {

            @Test
            fun shouldCreateStreamableMapObject() {
                val expectedStreamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { onDestroy(any()) } just Runs
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
                    every { onDestroy(any()) } just Runs
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
            fun destroyShouldUnregisterCreatedMapObjectAsCallbackListener() {
                every { callbackListenerManager.unregister(any()) } just Runs
                val streamableMapObject = mockk<StreamableMapObjectImpl> {
                    every { onDestroy(any()) } just Runs
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

                val slot = slot<StreamableMapObjectImpl.() -> Unit>()
                verify { streamableMapObject.onDestroy(capture(slot)) }
                slot.captured.invoke(streamableMapObject)
                verify { callbackListenerManager.unregister(streamableMapObject) }
            }

        }

    }

}