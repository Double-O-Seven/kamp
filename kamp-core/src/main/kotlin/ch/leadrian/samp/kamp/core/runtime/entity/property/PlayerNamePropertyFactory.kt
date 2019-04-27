package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import com.netflix.governator.annotations.Configuration
import javax.inject.Inject

internal class PlayerNamePropertyFactory
@Inject
constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler
) {

    @Configuration("kamp.cache.player.names")
    var cacheNames: Boolean = true

    fun create(): PlayerNameProperty {
        return if (cacheNames) {
            CachingPlayerNameProperty(nativeFunctionExecutor, onPlayerNameChangeHandler)
        } else {
            NonCachingPlayerNameProperty(nativeFunctionExecutor, onPlayerNameChangeHandler)
        }
    }
}