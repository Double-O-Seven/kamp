package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerArmourDelegate(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Float> {

    private val armour = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): Float {
        nativeFunctionExecutor.getPlayerArmour(playerid = thisRef.id.value, armour = armour)
        return armour.value
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setPlayerArmour(playerid = thisRef.id.value, armour = value)
    }

}