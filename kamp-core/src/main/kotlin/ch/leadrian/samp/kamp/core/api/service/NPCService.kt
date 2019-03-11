package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

class NPCService
@Inject
constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun connectNPC(name: String, script: String) {
        nativeFunctionExecutor.connectNPC(name, script)
    }

}