package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class ActorCoordinatesPropertyTest {

    private val actorId: ActorId = ActorId.valueOf(50)
    private val actor: Actor = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var actorCoordinatesProperty: ActorCoordinatesProperty

    @BeforeEach
    fun setUp() {
        every { actor.id } returns actorId
        actorCoordinatesProperty = ActorCoordinatesProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetCoordinates() {
        every { nativeFunctionExecutor.getActorPos(actorId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val coordinates = actorCoordinatesProperty.getValue(actor, property)

        assertThat(coordinates)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetCoordinates() {
        every { nativeFunctionExecutor.setActorPos(any(), any(), any(), any()) } returns true

        actorCoordinatesProperty.setValue(actor, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setActorPos(actorid = actorId.value, x = 1f, y = 2f, z = 3f) }
    }
}