package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class PlayerAnimation
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    @JvmOverloads
    fun apply(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int,
            forceSync: Boolean = false
    ) {
        nativeFunctionExecutor.applyAnimation(
                playerid = player.id.value,
                animlib = animation.library,
                animname = animation.animationName,
                fDelta = fDelta,
                loop = loop,
                lockx = lockX,
                locky = lockY,
                freeze = freeze,
                time = time,
                forcesync = forceSync
        )
    }

    @JvmOverloads
    fun clear(forceSync: Boolean = false) {
        nativeFunctionExecutor.clearAnimations(playerid = player.id.value, forcesync = forceSync)
    }

    val index: Int
        get() = nativeFunctionExecutor.getPlayerAnimationIndex(player.id.value)

}