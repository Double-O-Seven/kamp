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

internal class ActorHealthPropertyTest {

    private val actorId: ActorId = ActorId.valueOf(50)
    private val actor: Actor = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var actorHealthProperty: ActorHealthProperty

    @BeforeEach
    fun setUp() {
        every { actor.id } returns actorId
        actorHealthProperty = ActorHealthProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetHealth() {
        every { nativeFunctionExecutor.getActorHealth(actorId.value, any()) } answers {
            secondArg<ReferenceFloat>().value = 4f
            true
        }

        val health = actorHealthProperty.getValue(actor, property)

        assertThat(health)
                .isEqualTo(4f)
    }

    @Test
    fun shouldSetHealth() {
        every { nativeFunctionExecutor.setActorHealth(any(), any()) } returns true

        actorHealthProperty.setValue(actor, property, 4f)

        verify { nativeFunctionExecutor.setActorHealth(actorid = actorId.value, health = 4f) }
    }

}