package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerNameProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler
) : ReadWriteProperty<Player, String> {

    private lateinit var name: String

    override fun getValue(thisRef: Player, property: KProperty<*>): String = getName(thisRef)

    override fun setValue(thisRef: Player, property: KProperty<*>, value: String) {
        if (value.isEmpty()) {
            throw InvalidPlayerNameException("", "Name cannot be empty")
        }
        val oldName = getName(thisRef)
        val result = nativeFunctionExecutor.setPlayerName(playerid = thisRef.id.value, name = value)
        when (result) {
            -1 -> throw InvalidPlayerNameException(
                    name = value,
                    message = "Name is already in use, too long or invalid"
            )
            else -> name = value
        }
        onPlayerNameChangeHandler.onPlayerNameChange(thisRef, oldName, value)
    }

    private fun getName(thisRef: Player): String {
        if (!this::name.isInitialized) {
            val name = ReferenceString()
            nativeFunctionExecutor.getPlayerName(
                    playerid = thisRef.id.value,
                    name = name,
                    size = SAMPConstants.MAX_PLAYER_NAME
            )
            name.value?.let { this.name = it }
        }
        return name
    }

}