package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerCoordinatesProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Vector3D> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): Vector3D {
        nativeFunctionExecutor.getPlayerPos(playerid = thisRef.id.value, x = x, y = y, z = z)
        return vector3DOf(x = x.value, y = y.value, z = z.value)
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Vector3D) {
        nativeFunctionExecutor.setPlayerPos(playerid = thisRef.id.value, x = value.x, y = value.y, z = value.z)
    }

}