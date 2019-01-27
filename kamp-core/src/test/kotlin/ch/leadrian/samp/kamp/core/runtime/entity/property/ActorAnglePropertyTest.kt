package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
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

internal class ActorAnglePropertyTest {

    private val actorId: ActorId = ActorId.valueOf(50)
    private val actor: Actor = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var actorAngleProperty: ActorAngleProperty

    @BeforeEach
    fun setUp() {
        every { actor.id } returns actorId
        actorAngleProperty = ActorAngleProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetAngle() {
        every { nativeFunctionExecutor.getActorFacingAngle(actorId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val angle = actorAngleProperty.getValue(actor, property)

        assertThat(angle)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetAngle() {
        every { nativeFunctionExecutor.setActorFacingAngle(any(), any()) } returns true

        actorAngleProperty.setValue(actor, property, 4f)

        verify { nativeFunctionExecutor.setActorFacingAngle(actorid = actorId.value, angle = 4f) }
    }

}