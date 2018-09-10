package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.PlayerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerRegistry
@Inject
constructor(nativeFunctionExecutor: SAMPNativeFunctionExecutor) : EntityRegistry<PlayerImpl, PlayerId>(
        arrayOfNulls(nativeFunctionExecutor.getMaxPlayers())
)
