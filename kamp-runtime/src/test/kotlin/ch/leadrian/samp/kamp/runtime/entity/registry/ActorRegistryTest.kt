package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.id.ActorId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class ActorRegistryTest {

    @Test
    fun shouldRegisterAndGetActor() {
        val actorId = 50
        val actor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val actorRegistry = ActorRegistry()

        actorRegistry.register(actor)

        val registeredActor = actorRegistry.getActor(actorId)
        assertThat(registeredActor)
                .isSameAs(actor)
    }

    @Test
    fun givenAnotherActorWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val actorId = 50
        val alreadyRegisteredActor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val newActor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val actorRegistry = ActorRegistry()
        actorRegistry.register(alreadyRegisteredActor)

        val caughtThrowable = catchThrowable { actorRegistry.register(newActor) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredActor = actorRegistry.getActor(actorId)
        assertThat(registeredActor)
                .isSameAs(alreadyRegisteredActor)
    }

    @Test
    fun shouldUnregisterRegisteredActor() {
        val actorId = 50
        val actor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val actorRegistry = ActorRegistry()
        actorRegistry.register(actor)

        actorRegistry.unregister(actor)

        val registeredActor = actorRegistry.getActor(actorId)
        assertThat(registeredActor)
                .isNull()
    }


    @Test
    fun givenActorIsNotRegisteredItShouldThrowAnException() {
        val actorId = ActorId.valueOf(50)
        val actor = mockk<Actor> {
            every { id } returns actorId
        }
        val actorRegistry = ActorRegistry()

        val caughtThrowable = catchThrowable { actorRegistry.unregister(actor) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherActorWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val actorId = 50
        val alreadyRegisteredActor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val newActor = mockk<Actor> {
            every { id } returns ActorId.valueOf(actorId)
        }
        val actorRegistry = ActorRegistry()
        actorRegistry.register(alreadyRegisteredActor)

        val caughtThrowable = catchThrowable { actorRegistry.unregister(newActor) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredActor = actorRegistry.getActor(actorId)
        assertThat(registeredActor)
                .isSameAs(alreadyRegisteredActor)
    }

    @Test
    fun shouldReturnAllRegisteredActors() {
        val actorId1 = ActorId.valueOf(10)
        val actor1 = mockk<Actor> {
            every { id } returns actorId1
        }
        val actorId2 = ActorId.valueOf(15)
        val actor2 = mockk<Actor> {
            every { id } returns actorId2
        }
        val actorId3 = ActorId.valueOf(30)
        val actor3 = mockk<Actor> {
            every { id } returns actorId3
        }
        val actorRegistry = ActorRegistry()
        actorRegistry.register(actor1)
        actorRegistry.register(actor2)
        actorRegistry.register(actor3)

        val allActors = actorRegistry.getAllActors()

        assertThat(allActors)
                .containsExactly(actor1, actor2, actor3)
    }

}