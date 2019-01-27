package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.animationOf
import ch.leadrian.samp.kamp.core.api.data.mutablePositionOf
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutReceiverDelegate
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class StreamableActorImplTest {

    private lateinit var streamableActor: StreamableActorImpl

    private val actorService: ActorService = mockk()
    private val onStreamableActorStreamInHandler: OnStreamableActorStreamInHandler = mockk()
    private val onStreamableActorStreamOutHandler: OnStreamableActorStreamOutHandler = mockk()
    private val onPlayerDamageStreamableActorHandler: OnPlayerDamageStreamableActorHandler = mockk()
    private val actorStreamer: ActorStreamer = mockk()
    private val onStreamableActorStreamInReceiver: OnStreamableActorStreamInReceiverDelegate = mockk()
    private val onStreamableActorStreamOutReceiver: OnStreamableActorStreamOutReceiverDelegate = mockk()
    private val onPlayerDamageStreamableActorReceiver: OnPlayerDamageStreamableActorReceiverDelegate = mockk()

    @BeforeEach
    fun setUp() {
        streamableActor = StreamableActorImpl(
                model = SkinModel.ARMY,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                angle = 69f,
                isInvulnerable = true,
                virtualWorldId = 13,
                interiorIds = mutableSetOf(187),
                streamDistance = 13.37f,
                priority = 13,
                actorStreamer = actorStreamer,
                actorService = actorService,
                onStreamableActorStreamInHandler = onStreamableActorStreamInHandler,
                onStreamableActorStreamOutHandler = onStreamableActorStreamOutHandler,
                onPlayerDamageStreamableActorHandler = onPlayerDamageStreamableActorHandler,
                onStreamableActorStreamInReceiver = onStreamableActorStreamInReceiver,
                onStreamableActorStreamOutReceiver = onStreamableActorStreamOutReceiver,
                onPlayerDamageStreamableActorReceiver = onPlayerDamageStreamableActorReceiver

        )
    }

    @Nested
    inner class ModelTests {

        @Test
        fun shouldInitializeModel() {
            val model = streamableActor.model

            assertThat(model)
                    .isEqualTo(SkinModel.ARMY)
        }

        @Test
        fun shouldUpdateModel() {
            streamableActor.model = SkinModel.TENPEN

            assertThat(streamableActor.model)
                    .isEqualTo(SkinModel.TENPEN)
        }

        @Test
        fun givenActorIsStreamedInItShouldDestroyOldActorAndCreateNewActor() {
            val oldActor: Actor = mockk(relaxed = true) {
                every { health } returns 85f
            }
            val newActor: Actor = mockk {
                every { isInvulnerable = any() } just Runs
                every { health = any() } just Runs
                every { virtualWorldId = any() } just Runs
                every { addOnActorStreamInListener(any()) } just Runs
                every { addOnActorStreamOutListener(any()) } just Runs
                every { addOnPlayerGiveDamageActorListener(any()) } just Runs
            }
            every { actorService.createActor(SkinModel.ARMY, vector3DOf(1f, 2f, 3f), 69f) } returns oldActor
            every { actorService.createActor(SkinModel.TENPEN, vector3DOf(1f, 2f, 3f), 69f) } returns newActor
            streamableActor.onStreamIn()

            streamableActor.model = SkinModel.TENPEN

            assertAll(
                    {
                        verifyOrder {
                            oldActor.destroy()
                            actorService.createActor(SkinModel.TENPEN, vector3DOf(1f, 2f, 3f), 69f)
                        }
                    },
                    {
                        verify {
                            oldActor.removeOnActorStreamInListener(streamableActor)
                            oldActor.removeOnActorStreamOutListener(streamableActor)
                            oldActor.removeOnPlayerGiveDamageActorListener(streamableActor)
                            newActor.isInvulnerable = true
                            newActor.health = 85f
                            newActor.virtualWorldId = 13
                            newActor.addOnActorStreamInListener(streamableActor)
                            newActor.addOnActorStreamOutListener(streamableActor)
                            newActor.addOnPlayerGiveDamageActorListener(streamableActor)
                        }
                    }
            )
        }

    }

    @Nested
    inner class IsInvulnerableTests {

        @Test
        fun shouldInitializeIsInvulnerable() {
            val isInvulnerable = streamableActor.isInvulnerable

            assertThat(isInvulnerable)
                    .isTrue()
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldUpdateInvulnerability(isInvulnerable: Boolean) {
            streamableActor.isInvulnerable = isInvulnerable

            assertThat(streamableActor.isInvulnerable)
                    .isEqualTo(isInvulnerable)
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun givenActorIsStreamedInItShouldUpdateActorInvulnerability(isInvulnerable: Boolean) {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            clearMocks(actor)

            streamableActor.isInvulnerable = isInvulnerable

            verify { actor.isInvulnerable = isInvulnerable }
        }

    }

    @Nested
    inner class CoordinatesTests {

        @BeforeEach
        fun setUp() {
            every { actorStreamer.onBoundingBoxChange(any()) } just Runs
        }

        @Test
        fun shouldInitializeCoordinates() {
            val coordinates = streamableActor.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @Test
        fun shouldUpdateCoordinates() {
            streamableActor.coordinates = mutableVector3DOf(11f, 22f, 33f)

            assertThat(streamableActor.coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun givenActorIsStreamedInItShouldUpdateActorCoordinates() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            clearMocks(actor)

            streamableActor.coordinates = mutableVector3DOf(11f, 22f, 33f)

            verify { actor.coordinates = vector3DOf(11f, 22f, 33f) }
        }

        @Test
        fun shouldCallOnBoundingBoxChange() {
            streamableActor.coordinates = mutableVector3DOf(11f, 22f, 33f)

            verify { actorStreamer.onBoundingBoxChange(streamableActor) }
        }

    }

    @Nested
    inner class AngleTests {

        @Test
        fun shouldInitializeAngle() {
            val angle = streamableActor.angle

            assertThat(angle)
                    .isEqualTo(69f)
        }

        @Test
        fun shouldUpdateAngle() {
            streamableActor.angle = 0.815f

            assertThat(streamableActor.angle)
                    .isEqualTo(0.815f)
        }

        @Test
        fun givenActorIsStreamedInItShouldUpdateActorAngle() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            clearMocks(actor)

            streamableActor.angle = 0.815f

            verify { actor.angle = 0.815f }
        }

    }

    @Nested
    inner class PositionTests {

        @BeforeEach
        fun setUp() {
            every { actorStreamer.onBoundingBoxChange(any()) } just Runs
        }

        @Test
        fun shouldReturnInitialPosition() {
            val position = streamableActor.position

            assertThat(position)
                    .isEqualTo(positionOf(1f, 2f, 3f, 69f))
        }

        @Test
        fun shouldUpdatePosition() {
            streamableActor.position = mutablePositionOf(11f, 22f, 33f, 270f)

            assertThat(streamableActor.position)
                    .isEqualTo(positionOf(11f, 22f, 33f, 270f))
        }

        @Test
        fun givenActorIsStreamedInItShouldUpdateActorPosition() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            clearMocks(actor)

            streamableActor.position = mutablePositionOf(11f, 22f, 33f, 270f)

            verify {
                actor.coordinates = vector3DOf(11f, 22f, 33f)
                actor.angle = 270f
            }
        }

        @Test
        fun shouldCallOnBoundingBoxChange() {
            streamableActor.position = mutablePositionOf(11f, 22f, 33f, 270f)

            verify { actorStreamer.onBoundingBoxChange(streamableActor) }
        }

    }

    @Nested
    inner class VirtualWorldIdTests {

        @Test
        fun shouldInitializeVirtualWorldId() {
            val virtualWorldId = streamableActor.virtualWorldId

            assertThat(virtualWorldId)
                    .isEqualTo(13)
        }

        @Test
        fun shouldUpdateVirtualWorldId() {
            streamableActor.virtualWorldId = 1337

            assertThat(streamableActor.virtualWorldId)
                    .isEqualTo(1337)
        }

        @Test
        fun givenActorIsStreamedInItShouldUpdateActorVirtualWorldId() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()

            streamableActor.virtualWorldId = 1337

            verify { actor.virtualWorldId = 1337 }
        }

    }

    @Nested
    inner class HealthTests {

        @Test
        fun shouldInitializeHealth() {
            val health = streamableActor.health

            assertThat(health)
                    .isEqualTo(100f)
        }

        @Test
        fun givenActorIsStreamedInItShouldReturnTheActorsHealth() {
            val actor: Actor = mockk(relaxed = true) {
                every { health } returns 123f
            }
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()

            val health = streamableActor.health

            assertThat(health)
                    .isEqualTo(123f)
        }

        @Test
        fun shouldUpdateHealth() {
            streamableActor.health = 77f

            assertThat(streamableActor.health)
                    .isEqualTo(77f)
        }

        @Test
        fun givenActorIsStreamedInItShouldUpdateActorHealth() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()

            streamableActor.health = 77f

            verify { actor.health = 77f }
        }

    }

    @ParameterizedTest
    @CsvSource(
            "false, false, false, false",
            "true, true, true, true, true",
            "true, false, false, false",
            "false, true, false, false",
            "false, false, true, false",
            "false, false, false, true"
    )
    fun shouldApplyAnimationToStreamedInActor(loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean) {
        val actor: Actor = mockk(relaxed = true)
        every { actorService.createActor(any(), any(), any()) } returns actor
        streamableActor.onStreamIn()

        streamableActor.applyAnimation(
                animation = animationOf(library = "ABC", name = "xyz"),
                fDelta = 1f,
                time = 60,
                loop = loop,
                lockX = lockX,
                lockY = lockY,
                freeze = freeze
        )

        verify {
            actor.applyAnimation(
                    animation = animationOf(library = "ABC", name = "xyz"),
                    fDelta = 1f,
                    time = 60,
                    loop = loop,
                    lockX = lockX,
                    lockY = lockY,
                    freeze = freeze
            )
        }
    }

    @Test
    fun shouldClearAnimationsOfStreamedInActor() {
        val actor: Actor = mockk(relaxed = true)
        every { actorService.createActor(any(), any(), any()) } returns actor
        streamableActor.onStreamIn()

        streamableActor.clearAnimation()

        verify { actor.clearAnimation() }
    }

    @Test
    fun isStreamedInShouldInitiallyBeFalse() {
        val isStreamedIn = streamableActor.isStreamedIn

        assertThat(isStreamedIn)
                .isFalse()
    }

    @Nested
    inner class OnStreamInTests {

        @Test
        fun shouldStreamInActor() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor

            streamableActor.onStreamIn()

            verify {
                actor.isInvulnerable = true
                actor.health = 100f
                actor.virtualWorldId = 13
                actor.addOnActorStreamInListener(streamableActor)
                actor.addOnActorStreamOutListener(streamableActor)
                actor.addOnPlayerGiveDamageActorListener(streamableActor)
            }
        }

        @Test
        fun isStreamedInShouldBeTrueAfterOnStreamInWasCalled() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()

            val isStreamedIn = streamableActor.isStreamedIn

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun givenActorIsAlreadyStreamedInOnStreamInShouldThrowException() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()

            val caughtThrowable = catchThrowable { streamableActor.onStreamIn() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Actor is already streamed in")
        }

    }

    @Nested
    inner class OnStreamOutTests {

        @Test
        fun shouldStreamInActor() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            clearMocks(actor)

            streamableActor.onStreamOut()

            verify {
                actor.destroy()
                actor.removeOnActorStreamInListener(streamableActor)
                actor.removeOnActorStreamOutListener(streamableActor)
                actor.removeOnPlayerGiveDamageActorListener(streamableActor)
            }
        }

        @Test
        fun isStreamedInShouldBeFalseAfterOnStreamOutWasCalled() {
            val actor: Actor = mockk(relaxed = true)
            every { actorService.createActor(any(), any(), any()) } returns actor
            streamableActor.onStreamIn()
            streamableActor.onStreamOut()

            val isStreamedIn = streamableActor.isStreamedIn

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun givenActorIsAlreadyStreamedOutOnStreamOutShouldThrowException() {
            val caughtThrowable = catchThrowable { streamableActor.onStreamOut() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Actor was not streamed in")
        }

    }
}