package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.GangZoneRegistry
import javax.inject.Inject

internal class GangZoneFactory
@Inject
constructor(
        private val gangZoneRegistry: GangZoneRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(area: Rectangle): GangZone {
        val gangZone = GangZone(area, nativeFunctionExecutor)
        gangZoneRegistry.register(gangZone)
        gangZone.onDestroy { gangZoneRegistry.unregister(this) }
        return gangZone
    }

}