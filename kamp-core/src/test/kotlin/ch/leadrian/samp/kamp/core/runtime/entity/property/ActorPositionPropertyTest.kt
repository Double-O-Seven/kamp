package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class ActorPositionPropertyTest {

    private val actorId: ActorId = ActorId.valueOf(50)
    private val actor: Actor = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var actorPositionProperty: ActorPositionProperty

    @BeforeEach
    fun setUp() {
        every { actor.id } returns actorId
        actorPositionProperty = ActorPositionProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetPosition() {
        every { nativeFunctionExecutor.getActorPos(actorId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }
        every { actor.angle } returns 4f

        val position = actorPositionProperty.getValue(actor, property)

        assertThat(position)
                .isEqualTo(positionOf(x = 1f, y = 2f, z = 3f, angle = 4f))
    }

    @Test
    fun shouldSetPosition() {
        actor.apply {
            every { coordinates = any() } just Runs
            every { angle = any() } just Runs
        }

        actorPositionProperty.setValue(
                actor,
                property,
                positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
        )

        verify {
            actor.coordinates = positionOf(x = 1f, y = 2f, z = 3f, angle = 4f)
            actor.angle = 4f
        }
    }
}