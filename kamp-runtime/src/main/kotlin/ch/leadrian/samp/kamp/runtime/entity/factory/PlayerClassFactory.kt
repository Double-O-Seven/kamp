package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.data.SpawnInfo
import ch.leadrian.samp.kamp.api.entity.PlayerClass
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.PlayerClassImpl
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerClassRegistry
import javax.inject.Inject

internal class PlayerClassFactory
@Inject
constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val playerClassRegistry: PlayerClassRegistry
) {

    fun create(spawnInfo: SpawnInfo): PlayerClass {
        val playerClass = PlayerClassImpl(spawnInfo, nativeFunctionExecutor)
        // If the limit of 320 is reached, the last one is overwritten all the time, so we need to unregister it
        playerClassRegistry[playerClass.id]?.let { playerClassRegistry.unregister(it) }
        playerClassRegistry.register(playerClass)
        return playerClass
    }
}