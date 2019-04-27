package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal class CachingPlayerNameProperty(
        nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        onPlayerNameChangeHandler: OnPlayerNameChangeHandler
) : PlayerNameProperty(nativeFunctionExecutor, onPlayerNameChangeHandler) {

    private var name: String? = null

    override fun afterNameChange(newName: String) {
        name = newName
    }

    override fun getName(player: Player): String {
        if (name == null) {
            val name = ReferenceString()
            nativeFunctionExecutor.getPlayerName(
                    playerid = player.id.value,
                    name = name,
                    size = SAMPConstants.MAX_PLAYER_NAME
            )
            name.value?.let { this.name = it }
        }
        return name ?: "<Player ${player.id.value}>"
    }

}