package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class ActorAngleProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Actor, Float> {

    private val angle = ReferenceFloat()

    override fun getValue(thisRef: Actor, property: KProperty<*>): Float {
        nativeFunctionExecutor.getActorFacingAngle(actorid = thisRef.id.value, angle = angle)
        return angle.value
    }

    override fun setValue(thisRef: Actor, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setActorFacingAngle(actorid = thisRef.id.value, angle = value)
    }

}