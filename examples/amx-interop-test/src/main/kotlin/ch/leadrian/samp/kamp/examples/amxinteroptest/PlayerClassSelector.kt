package ch.leadrian.samp.kamp.examples.amxinteroptest

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerClassSelector
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerRequestClassListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerRequestClass(player: Player, playerClass: PlayerClass): OnPlayerRequestClassListener.Result {
        player.apply {
            interiorId = 14
            position = positionOf(258.4893f, -41.4008f, 1002.0234f, 270f)
        }
        player.camera.apply {
            coordinates = vector3DOf(256.0815f, -43.0475f, 1004.0234f)
            lookAt(vector3DOf(258.4893f, -41.4008f, 1002.0234f))
        }
        return OnPlayerRequestClassListener.Result.Allow
    }

}