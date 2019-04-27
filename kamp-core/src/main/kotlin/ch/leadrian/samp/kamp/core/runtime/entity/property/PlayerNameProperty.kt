package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal abstract class PlayerNameProperty(
        protected val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler
) : ReadWriteProperty<Player, String> {

    final override fun getValue(thisRef: Player, property: KProperty<*>): String = getName(thisRef)

    final override fun setValue(thisRef: Player, property: KProperty<*>, value: String) {
        if (value.isEmpty()) {
            throw InvalidPlayerNameException(name = "", message = "Name cannot be empty")
        }
        val oldName = getName(thisRef)
        val result = nativeFunctionExecutor.setPlayerName(playerid = thisRef.id.value, name = value)
        when (result) {
            -1 -> throw InvalidPlayerNameException(
                    name = value,
                    message = "Name is already in use, too long or invalid"
            )
            else -> afterNameChange(value)
        }
        onPlayerNameChangeHandler.onPlayerNameChange(thisRef, oldName, value)
    }

    protected abstract fun afterNameChange(newName: String)

    protected abstract fun getName(player: Player): String

}