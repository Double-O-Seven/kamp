package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ActorFactoryTest {

    private lateinit var actorFactory: ActorFactory

    private val actorRegistry = mockk<ActorRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.createActor(any(), any(), any(), any(), any()) } returns 0
        every { actorRegistry.register(any()) } just Runs
        every { actorRegistry.unregister(any()) } just Runs
        actorFactory = ActorFactory(actorRegistry, nativeFunctionExecutor)
    }

    @Test
    fun shouldCreateActor() {
        actorFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f
        )

        verify {
            nativeFunctionExecutor.createActor(
                    modelid = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    rotation = 4f
            )
        }
    }

    @Test
    fun shouldRegisterActor() {
        val actor = actorFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f
        )

        verify { actorRegistry.register(actor) }
    }

    @Test
    fun shouldUnregisterActorOnDestroy() {
        every { nativeFunctionExecutor.destroyActor(any()) } returns true
        val actor = actorFactory.create(
                model = ch.leadrian.samp.kamp.core.api.constants.SkinModel.ARMY,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = 4f
        )
        val onDestroy = mockk<Actor.() -> Unit>(relaxed = true)
        actor.onDestroy(onDestroy)

        actor.destroy()

        verify { onDestroy.invoke(actor) }
    }

}