package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerKeys
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class PlayerKeysDelegate(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadOnlyProperty<Player, PlayerKeys> {

    private val keys = ReferenceInt()
    private val leftRight = ReferenceInt()
    private val upDown = ReferenceInt()

    override fun getValue(thisRef: Player, property: KProperty<*>): PlayerKeys {
        nativeFunctionExecutor.getPlayerKeys(
                playerid = thisRef.id.value,
                keys = keys,
                leftright = leftRight,
                updown = upDown
        )
        return PlayerKeys(
                keys = keys.value,
                leftRight = leftRight.value,
                upDown = upDown.value,
                player = thisRef
        )
    }

}