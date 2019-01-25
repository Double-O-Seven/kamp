package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerPositionDelegate(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Position> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): Position {
        nativeFunctionExecutor.getPlayerPos(playerid = thisRef.id.value, x = x, y = y, z = z)
        return positionOf(x = x.value, y = y.value, z = z.value, angle = thisRef.angle)
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Position) {
        thisRef.apply {
            coordinates = value
            angle = value.angle
        }
    }

}