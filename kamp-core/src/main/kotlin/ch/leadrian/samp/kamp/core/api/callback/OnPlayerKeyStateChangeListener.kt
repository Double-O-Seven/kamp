package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerKeys

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerKeyStateChangeListener {

    fun onPlayerKeyStateChange(oldKeys: PlayerKeys, newKeys: PlayerKeys)

}
