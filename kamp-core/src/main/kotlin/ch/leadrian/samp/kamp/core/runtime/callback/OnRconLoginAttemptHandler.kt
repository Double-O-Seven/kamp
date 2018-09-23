package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnRconLoginAttemptListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnRconLoginAttemptHandler
@Inject
constructor() : CallbackListenerRegistry<OnRconLoginAttemptListener>(OnRconLoginAttemptListener::class), OnRconLoginAttemptListener {

    override fun onRconLoginAttempt(ipAddress: String, password: String, success: Boolean) {
        listeners.forEach {
            it.onRconLoginAttempt(ipAddress, password, success)
        }
    }

}
