package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.ActorFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
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
import org.junit.jupiter.params.provider.ValueSource

internal class ActorServiceTest {

    private lateinit var actorService: ActorService

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val actorRegistry = mockk<ActorRegistry>()
    private val actorFactory = mockk<ActorFactory>()

    @BeforeEach
    fun setUp() {
        actorService = ActorService(nativeFunctionExecutor, actorRegistry, actorFactory)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isValidShouldReturnExpectedResult(expectedResult: Boolean) {
        val actorId = 69
        every { nativeFunctionExecutor.isValidActor(actorId) } returns expectedResult

        val result = actorService.isValid(ActorId.valueOf(actorId))

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class CreateActorTests {

        private val actor = mockk<Actor>()

        @BeforeEach
        fun setUp() {
            every { actorFactory.create(any(), any(), any()) } returns actor
        }

        @Test
        fun shouldCreateActorWithPosition() {
            val createdActor = actorService.createActor(SkinModel.ARMY, positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))

            verify { actorFactory.create(SkinModel.ARMY, positionOf(x = 1f, y = 2f, z = 3f, angle = 4f), 4f) }
            assertThat(createdActor)
                    .isSameAs(actor)
        }

        @Test
        fun shouldCreateActorWithCoordinatesAndAngle() {
            val createdActor = actorService.createActor(SkinModel.ARMY, vector3DOf(x = 1f, y = 2f, z = 3f), 4f)

            verify { actorFactory.create(SkinModel.ARMY, vector3DOf(x = 1f, y = 2f, z = 3f), 4f) }
            assertThat(createdActor)
                    .isSameAs(actor)
        }

        @Test
        fun shouldCreateActorWithAngledLocation() {
            every { actor.virtualWorldId = any() } just Runs
            val createdActor = actorService.createActor(
                    SkinModel.ARMY,
                    angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, worldId = 1337, interiorId = 0)
            )

            verifyOrder {
                actorFactory.create(
                        SkinModel.ARMY,
                        angledLocationOf(x = 1f, y = 2f, z = 3f, angle = 4f, worldId = 1337, interiorId = 0),
                        4f
                )
                actor.virtualWorldId = 1337
            }
            assertThat(createdActor)
                    .isSameAs(actor)
        }

    }

    @Nested
    inner class GetActorTests {

        @Test
        fun shouldReturnActor() {
            val actorId = ActorId.valueOf(69)
            val expectedActor = mockk<Actor>()
            every { actorRegistry[actorId] } returns expectedActor

            val actor = actorService.getActor(actorId)

            assertThat(actor)
                    .isEqualTo(expectedActor)
        }

        @Test
        fun givenNoActorForGivenIdItShouldThrowException() {
            val actorId = ActorId.valueOf(69)
            every { actorRegistry[actorId] } returns null

            val caughtThrowable = catchThrowable { actorService.getActor(actorId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No actor with ID 69")
        }

    }

    @Test
    fun shouldReturnAllActors() {
        val actor1 = mockk<Actor>()
        val actor2 = mockk<Actor>()
        every { actorRegistry.getAll() } returns listOf(actor1, actor2)

        val actors = actorService.getAllActors()

        assertThat(actors)
                .containsExactlyInAnyOrder(actor1, actor2)
    }

    @Test
    fun shouldReturnActorPoolSize() {
        every { nativeFunctionExecutor.getActorPoolSize() } returns 1337

        val poolSize = actorService.getPoolSize()

        assertThat(poolSize)
                .isEqualTo(1337)
    }
}