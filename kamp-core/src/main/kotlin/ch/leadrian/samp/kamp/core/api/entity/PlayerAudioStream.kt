package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class PlayerAudioStream
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    @JvmOverloads
    fun play(url: String, position: Sphere, usePosition: Boolean = true) {
        nativeFunctionExecutor.playAudioStreamForPlayer(
                playerid = player.id.value,
                url = url,
                posX = position.x,
                posY = position.y,
                posZ = position.z,
                distance = position.radius,
                usepos = usePosition
        )
    }

    fun play(url: String) {
        nativeFunctionExecutor.playAudioStreamForPlayer(
                playerid = player.id.value,
                url = url,
                posX = 0f,
                posY = 0f,
                posZ = 0f,
                distance = 0f,
                usepos = false
        )
    }

    fun stop() {
        nativeFunctionExecutor.stopAudioStreamForPlayer(player.id.value)
    }

}