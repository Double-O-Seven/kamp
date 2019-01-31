package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionHook
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionHookFactory
import javax.inject.Inject
import javax.inject.Provider

/**
 * Example implementation of [SAMPNativeFunctionHookFactory].
 * @see [SAMPNativeFunctionHook]
 */
class ClientMessageLoggerFactory
@Inject
constructor(private val playerService: Provider<PlayerService>) : SAMPNativeFunctionHookFactory {

    override val priority: Int = 0

    override fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook {
        return ClientMessageLogger(playerService, nativeFunctionExecutor)
    }

}