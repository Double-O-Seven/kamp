package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableActorImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableActorFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
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
import kotlin.reflect.KClass

internal class ActorStreamerTest {

    private lateinit var actorStreamer: ActorStreamer

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val coordinatesBasedGlobalStreamerFactory = mockk<CoordinatesBasedGlobalStreamerFactory>()
    private val coordinatesBasedGlobalStreamer = mockk<CoordinatesBasedGlobalStreamer<StreamableActorImpl, Rect3d>>()
    private val streamableActorFactory = mockk<StreamableActorFactory>()

    @BeforeEach
    fun setUp() {
        every {
            coordinatesBasedGlobalStreamerFactory.create<StreamableActorImpl, Rect3d>(any(), any(), any())
        } returns coordinatesBasedGlobalStreamer
        every { callbackListenerManager.register(any()) } just Runs
        actorStreamer = ActorStreamer(
                callbackListenerManager,
                coordinatesBasedGlobalStreamerFactory,
                streamableActorFactory
        )
    }

    @Test
    fun initializeShouldCreateCoordinatesBasedGlobalStreamer() {
        actorStreamer.initialize()

        verify {
            coordinatesBasedGlobalStreamerFactory.create(
                    any<SpatialIndex3D<StreamableActorImpl>>(),
                    SAMPConstants.MAX_ACTORS
            )
        }
    }

    @Nested
    inner class AfterInitializationTests {

        @BeforeEach
        fun setUp() {
            actorStreamer.initialize()
        }

        @Nested
        inner class CapacityTests {

            @Test
            fun shouldSetCapacity() {
                every { coordinatesBasedGlobalStreamer.capacity = any() } just Runs

                actorStreamer.capacity = 69

                verify { coordinatesBasedGlobalStreamer.capacity = 69 }
            }

            @Test
            fun shouldReturnCapacity() {
                every { coordinatesBasedGlobalStreamer.capacity } returns 187

                val capacity = actorStreamer.capacity

                assertThat(capacity)
                        .isEqualTo(187)
            }

        }

        @Nested
        inner class CreateActorTests {

            @BeforeEach
            fun setUp() {
                every { callbackListenerManager.registerOnlyAs(any<KClass<*>>(), any(), any()) } just Runs
                every { coordinatesBasedGlobalStreamer.add(any()) } just Runs
            }

            @Test
            fun shouldCreateStreamableActor() {
                val expectedStreamableActor = mockk<StreamableActorImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableActorFactory.create(
                            model = SkinModel.ARMY,
                            coordinates = mutableVector3DOf(1f, 2f, 3f),
                            angle = 69f,
                            isInvulnerable = true,
                            virtualWorldId = 13,
                            interiorIds = mutableSetOf(187),
                            streamDistance = 13.37f,
                            priority = 13,
                            actorStreamer = actorStreamer
                    )
                } returns expectedStreamableActor

                val streamableActor = actorStreamer.createActor(
                        model = SkinModel.ARMY,
                        coordinates = mutableVector3DOf(1f, 2f, 3f),
                        angle = 69f,
                        isInvulnerable = true,
                        virtualWorldId = 13,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 13.37f,
                        priority = 13
                )

                assertThat(streamableActor)
                        .isEqualTo(expectedStreamableActor)
            }

            @Test
            fun shouldAddCreatedActorToCoordinatesBasedStreamer() {
                val streamableActor = mockk<StreamableActorImpl> {
                    every { addOnDestroyListener(any()) } just Runs
                }
                every {
                    streamableActorFactory.create(any(), any(), any(), any(), any(), any(), any(), any(), any())
                } returns streamableActor

                actorStreamer.createActor(
                        model = SkinModel.ARMY,
                        coordinates = mutableVector3DOf(1f, 2f, 3f),
                        angle = 69f,
                        isInvulnerable = true,
                        virtualWorldId = 13,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 13.37f,
                        priority = 13
                )

                verify { coordinatesBasedGlobalStreamer.add(streamableActor) }
            }

        }

        @Test
        fun shouldDelegateStream() {
            every { coordinatesBasedGlobalStreamer.stream(any()) } just Runs
            val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
            val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
            val streamLocations = listOf(streamLocation1, streamLocation2)

            actorStreamer.stream(streamLocations)

            verify { coordinatesBasedGlobalStreamer.stream(streamLocations) }
        }

        @Test
        fun shouldDelegateOnBoundingBoxChange() {
            val streamableActor = mockk<StreamableActorImpl>()
            every { coordinatesBasedGlobalStreamer.onBoundingBoxChange(any()) } just Runs

            actorStreamer.onBoundingBoxChange(streamableActor)

            verify { coordinatesBasedGlobalStreamer.onBoundingBoxChange(streamableActor) }
        }

    }

}