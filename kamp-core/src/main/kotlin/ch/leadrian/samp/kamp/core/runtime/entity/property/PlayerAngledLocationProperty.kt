package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerAngledLocationProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, AngledLocation> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): AngledLocation {
        nativeFunctionExecutor.getPlayerPos(playerid = thisRef.id.value, x = x, y = y, z = z)
        return angledLocationOf(
                x = x.value,
                y = y.value,
                z = z.value,
                interiorId = thisRef.interiorId,
                worldId = thisRef.virtualWorldId,
                angle = thisRef.angle
        )
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: AngledLocation) {
        thisRef.apply {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
            angle = value.angle
        }
    }

}