package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal class NonCachingPlayerNameProperty(
        nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        onPlayerNameChangeHandler: OnPlayerNameChangeHandler
) : PlayerNameProperty(nativeFunctionExecutor, onPlayerNameChangeHandler) {

    override fun afterNameChange(newName: String) {}

    override fun getName(player: Player): String {
        val name = ReferenceString()
        nativeFunctionExecutor.getPlayerName(
                playerid = player.id.value,
                name = name,
                size = SAMPConstants.MAX_PLAYER_NAME
        )
        return name.value ?: "<Player ${player.id.value}>"
    }

}