package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnRconLoginAttemptListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnRconLoginAttemptListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnRconLoginAttemptListener>(OnRconLoginAttemptListener::class), OnRconLoginAttemptListener {

    override fun onRconLoginAttempt(ipAddress: kotlin.String, password: kotlin.String, success: kotlin.Boolean) {
        getListeners().forEach {
            it.onRconLoginAttempt(ipAddress, password, success)
        }
    }

}
