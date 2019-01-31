package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionHook
import javax.inject.Provider

/**
 * An example implementation of [ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionHook].
 * @see [ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionHookFactory]
 */
class ClientMessageLogger(
        playerService: Provider<PlayerService>,
        hookedNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : SAMPNativeFunctionHook(hookedNativeFunctionExecutor) {

    private companion object {

        val log = loggerFor<ClientMessageLogger>()

    }

    private val playerService: PlayerService by lazy { playerService.get() }

    override fun sendClientMessage(playerid: Int, color: Int, message: String): Boolean {
        val playerName = playerService.getPlayer(PlayerId.valueOf(playerid)).name
        log.info("Message to {}: {}", playerName, message)
        return super.sendClientMessage(playerid, color, message)
    }

    override fun sendClientMessageToAll(color: Int, message: String): Boolean {
        log.info("Message to all: {}", message)
        return super.sendClientMessageToAll(color, message)
    }

}