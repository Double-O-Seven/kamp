package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.LastShotVectors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class PlayerLastShotVectorsProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadOnlyProperty<Player, LastShotVectors> {

    private val hitPosX = ReferenceFloat()
    private val hitPosY = ReferenceFloat()
    private val hitPosZ = ReferenceFloat()
    private val originX = ReferenceFloat()
    private val originY = ReferenceFloat()
    private val originZ = ReferenceFloat()

    override fun getValue(thisRef: Player, property: KProperty<*>): LastShotVectors {
        nativeFunctionExecutor.getPlayerLastShotVectors(
                playerid = thisRef.id.value,
                fHitPosX = hitPosX,
                fHitPosY = hitPosY,
                fHitPosZ = hitPosZ,
                fOriginX = originX,
                fOriginY = originY,
                fOriginZ = originZ
        )
        return LastShotVectors(
                origin = vector3DOf(x = originX.value, y = originY.value, z = originZ.value),
                hitPosition = vector3DOf(x = hitPosX.value, y = hitPosY.value, z = hitPosZ.value)
        )
    }

}