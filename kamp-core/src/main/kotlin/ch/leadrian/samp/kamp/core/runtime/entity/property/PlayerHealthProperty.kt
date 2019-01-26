package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerHealthProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Float> {

    private val health = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): Float {
        nativeFunctionExecutor.getPlayerHealth(playerid = thisRef.id.value, health = health)
        return health.value
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setPlayerHealth(playerid = thisRef.id.value, health = value)
    }

}