package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableActorImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StreamableActorServiceTest {

    private lateinit var streamableActorService: StreamableActorService
    private val actorStreamer: ActorStreamer = mockk()

    @BeforeEach
    fun setUp() {
        streamableActorService = StreamableActorService(actorStreamer)
    }

    @Nested
    inner class CreateStreamableActorTests {

        private val expectedStreamableActor: StreamableActorImpl = mockk()

        @Test
        fun shouldCreateStreamableActorUsingVector3DAndSetOfInteriorIds() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    angle = 4f,
                    isInvulnerable = true,
                    virtualWorldId = 69,
                    interiorIds = mutableSetOf(187),
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }

        @Test
        fun shouldCreateStreamableActorUsingVector3DAndInteriorId() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    coordinates = vector3DOf(1f, 2f, 3f),
                    angle = 4f,
                    isInvulnerable = true,
                    virtualWorldId = 69,
                    interiorId = 187,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }

        @Test
        fun shouldCreateStreamableActorUsingPositionAndSetOfInteriorIds() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    position = positionOf(1f, 2f, 3f, 4f),
                    isInvulnerable = true,
                    virtualWorldId = 69,
                    interiorIds = mutableSetOf(187),
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }

        @Test
        fun shouldCreateStreamableActorUsingPositionAndInteriorId() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    position = positionOf(1f, 2f, 3f, 4f),
                    isInvulnerable = true,
                    virtualWorldId = 69,
                    interiorId = 187,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }

        @Test
        fun shouldCreateStreamableActorUsingAngledLocation() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    angledLocation = angledLocationOf(
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            interiorId = 187,
                            worldId = 69,
                            angle = 4f
                    ),
                    isInvulnerable = true,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }

        @Test
        fun shouldCreateStreamableActorUsingLocation() {
            every {
                actorStreamer.createActor(
                        model = SkinModel.TENPEN,
                        coordinates = match { it.x == 1f && it.y == 2f && it.z == 3f },
                        angle = 4f,
                        isInvulnerable = true,
                        virtualWorldId = 69,
                        interiorIds = mutableSetOf(187),
                        streamDistance = 300f,
                        priority = 1337
                )
            } returns expectedStreamableActor

            val streamableActor = streamableActorService.createStreamableActor(
                    model = SkinModel.TENPEN,
                    location = locationOf(
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            interiorId = 187,
                            worldId = 69
                    ),
                    angle = 4f,
                    isInvulnerable = true,
                    streamDistance = 300f,
                    priority = 1337
            )

            assertThat(streamableActor)
                    .isEqualTo(expectedStreamableActor)
        }
    }

    @Test
    fun shouldSetMaxStreamedInActors() {
        every { actorStreamer.capacity = any() } just Runs

        streamableActorService.setMaxStreamedInActors(500)

        verify { actorStreamer.capacity = 500 }
    }

}