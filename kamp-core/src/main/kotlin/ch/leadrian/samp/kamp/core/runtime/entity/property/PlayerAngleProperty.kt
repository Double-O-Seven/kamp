package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerAngleProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Float> {

    private val angle = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): Float {
        nativeFunctionExecutor.getPlayerFacingAngle(playerid = thisRef.id.value, angle = angle)
        return angle.value
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setPlayerFacingAngle(playerid = thisRef.id.value, angle = value)
    }

}