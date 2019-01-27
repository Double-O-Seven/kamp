package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class ActorHealthProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Actor, Float> {

    private val health = ReferenceFloat()

    override fun getValue(thisRef: Actor, property: KProperty<*>): Float {
        nativeFunctionExecutor.getActorHealth(actorid = thisRef.id.value, health = health)
        return health.value
    }

    override fun setValue(thisRef: Actor, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setActorHealth(actorid = thisRef.id.value, health = value)
    }

}