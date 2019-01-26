package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Time
import ch.leadrian.samp.kamp.core.api.data.timeOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerTimeProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Player, Time> {

    private val hour = ReferenceInt()
    private val minute = ReferenceInt()

    override fun getValue(thisRef: Player, property: KProperty<*>): Time {
        nativeFunctionExecutor.getPlayerTime(playerid = thisRef.id.value, hour = hour, minute = minute)
        return timeOf(hour = hour.value, minute = minute.value)
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Time) {
        nativeFunctionExecutor.setPlayerTime(playerid = thisRef.id.value, hour = value.hour, minute = value.minute)
    }

}