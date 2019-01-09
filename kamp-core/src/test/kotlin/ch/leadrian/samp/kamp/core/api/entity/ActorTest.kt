package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnActorStreamInReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnActorStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class ActorTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructActor() {
            val actorId = ActorId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createActor(
                            modelid = SkinModel.BALLAS2.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rotation = 4f
                    )
                } returns actorId.value
            }

            val actor = Actor(
                    model = SkinModel.BALLAS2,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = 4f,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(actor.id)
                    .isEqualTo(actorId)
        }

        @Test
        fun givenCreateActorReturnsInvalidActorIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createActor(
                            modelid = SkinModel.BALLAS2.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rotation = 4f
                    )
                } returns SAMPConstants.INVALID_ACTOR_ID
            }

            val caughtThrowable = catchThrowable {
                Actor(
                        model = SkinModel.BALLAS2,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = 4f,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val actorId = ActorId.valueOf(10)
        private lateinit var actor: Actor
        private val onActorStreamInReceiver = mockk<OnActorStreamInReceiverDelegate>()
        private val onActorStreamOutReceiver = mockk<OnActorStreamOutReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.createActor(any(), any(), any(), any(), any()) } returns actorId.value
            actor = Actor(
                    model = SkinModel.BALLAS2,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = 4f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    onActorStreamInReceiver = onActorStreamInReceiver,
                    onActorStreamOutReceiver = onActorStreamOutReceiver
            )
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun isStreamedInShouldReturnExpectedReturn(expectedResult: Boolean) {
            val playerId = PlayerId.valueOf(69)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every {
                nativeFunctionExecutor.isActorStreamedIn(actorid = actorId.value, forplayerid = playerId.value)
            } returns expectedResult

            val result = actor.isStreamedIn(player)

            assertThat(result)
                    .isEqualTo(expectedResult)
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
        fun shouldApplyAnimation(loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean) {
            every {
                nativeFunctionExecutor
                        .applyActorAnimation(any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns true

            actor.applyAnimation(
                    animation = ch.leadrian.samp.kamp.core.api.data.Animation.valueOf(library = "ABC", name = "xyz"),
                    fDelta = 1f,
                    time = 60,
                    loop = loop,
                    lockX = lockX,
                    lockY = lockY,
                    freeze = freeze
            )

            verify {
                nativeFunctionExecutor.applyActorAnimation(
                        actorid = actorId.value,
                        animlib = "ABC",
                        animname = "xyz",
                        fDelta = 1f,
                        time = 60,
                        loop = loop,
                        lockx = lockX,
                        locky = lockY,
                        freeze = freeze
                )
            }
        }

        @Test
        fun shouldClearAnimation() {
            every { nativeFunctionExecutor.clearActorAnimations(any()) } returns true

            actor.clearAnimation()

            verify { nativeFunctionExecutor.clearActorAnimations(actorId.value) }
        }

        @Nested
        inner class CoordinatesTests {

            @Test
            fun shouldGetCoordinates() {
                every { nativeFunctionExecutor.getActorPos(actorId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }

                val coordinates = actor.coordinates

                assertThat(coordinates)
                        .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
            }

            @Test
            fun shouldSetCoordinates() {
                every { nativeFunctionExecutor.setActorPos(any(), any(), any(), any()) } returns true

                actor.coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)

                verify { nativeFunctionExecutor.setActorPos(actorid = actorId.value, x = 1f, y = 2f, z = 3f) }
            }
        }

        @Nested
        inner class PositionTests {

            @Test
            fun shouldGetPosition() {
                every { nativeFunctionExecutor.getActorPos(actorId.value, any(), any(), any()) } answers {
                    secondArg<ReferenceFloat>().value = 1f
                    thirdArg<ReferenceFloat>().value = 2f
                    arg<ReferenceFloat>(3).value = 3f
                    true
                }
                every { nativeFunctionExecutor.getActorFacingAngle(actorId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 4f
                    true
                }

                val position = actor.position

                assertThat(position)
                        .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
            }

            @Test
            fun shouldSetPosition() {
                every { nativeFunctionExecutor.setActorPos(any(), any(), any(), any()) } returns true
                every { nativeFunctionExecutor.setActorFacingAngle(any(), any()) } returns true

                actor.position = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)

                verify {
                    nativeFunctionExecutor.setActorPos(actorid = actorId.value, x = 1f, y = 2f, z = 3f)
                    nativeFunctionExecutor.setActorFacingAngle(actorid = actorId.value, angle = 4f)
                }
            }
        }

        @Nested
        inner class AngleTests {

            @Test
            fun shouldGetAngle() {
                every { nativeFunctionExecutor.getActorFacingAngle(actorId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 4f
                    true
                }

                val angle = actor.angle

                assertThat(angle)
                        .isEqualTo(4f)
            }

            @Test
            fun shouldSetAngle() {
                every { nativeFunctionExecutor.setActorFacingAngle(any(), any()) } returns true

                actor.angle = 4f

                verify { nativeFunctionExecutor.setActorFacingAngle(actorid = actorId.value, angle = 4f) }
            }
        }

        @Nested
        inner class VirtualWorldIdTests {

            @Test
            fun shouldGetVirtualWorldId() {
                every { nativeFunctionExecutor.getActorVirtualWorld(actorId.value) } returns 1337

                val virtualWorldId = actor.virtualWorldId

                assertThat(virtualWorldId)
                        .isEqualTo(1337)
            }

            @Test
            fun shouldSetVirtualWorldId() {
                every { nativeFunctionExecutor.setActorVirtualWorld(any(), any()) } returns true

                actor.virtualWorldId = 1337

                verify { nativeFunctionExecutor.setActorVirtualWorld(actorid = actorId.value, vworld = 1337) }
            }
        }

        @Nested
        inner class HealthTests {

            @Test
            fun shouldGetHealth() {
                every { nativeFunctionExecutor.getActorHealth(actorId.value, any()) } answers {
                    secondArg<ReferenceFloat>().value = 50f
                    true
                }

                val health = actor.health

                assertThat(health)
                        .isEqualTo(50f)
            }

            @Test
            fun shouldSetHealth() {
                every { nativeFunctionExecutor.setActorHealth(any(), any()) } returns true

                actor.health = 50f

                verify { nativeFunctionExecutor.setActorHealth(actorid = actorId.value, health = 50f) }
            }
        }

        @Nested
        inner class IsInvulnerableTests {

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldReturnWhetherActorIsInvulnerable(expectedResult: Boolean) {
                every { nativeFunctionExecutor.isActorInvulnerable(actorId.value) } returns expectedResult

                val result = actor.isInvulnerable

                assertThat(result)
                        .isEqualTo(expectedResult)
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "false"])
            fun shouldSetActorInvulnerable(isInvulnerable: Boolean) {
                every { nativeFunctionExecutor.setActorInvulnerable(any(), any()) } returns true

                actor.isInvulnerable = isInvulnerable

                verify { nativeFunctionExecutor.setActorInvulnerable(actorId.value, isInvulnerable) }
            }

        }

        @Test
        fun shouldCallOnActorStreamInReceiverDelegate() {
            val player = mockk<Player>()
            every { onActorStreamInReceiver.onActorStreamIn(any(), any()) } just Runs

            actor.onStreamIn(player)

            verify { onActorStreamInReceiver.onActorStreamIn(actor, player) }
        }

        @Test
        fun shouldCallOnActorStreamOutReceiverDelegate() {
            val player = mockk<Player>()
            every { onActorStreamOutReceiver.onActorStreamOut(any(), any()) } just Runs

            actor.onStreamOut(player)

            verify { onActorStreamOutReceiver.onActorStreamOut(actor, player) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.destroyActor(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = actor.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyActor() {
                val onDestroy = mockk<Actor.() -> Unit>(relaxed = true)
                actor.onDestroy(onDestroy)

                actor.destroy()

                verifyOrder {
                    onDestroy.invoke(actor)
                    nativeFunctionExecutor.destroyActor(actorId.value)
                }
                assertThat(actor.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<Actor.() -> Unit>(relaxed = true)
                actor.onDestroy(onDestroy)

                actor.destroy()
                actor.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(actor)
                    nativeFunctionExecutor.destroyActor(actorId.value)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                actor.destroy()

                val caughtThrowable = catchThrowable { actor.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}