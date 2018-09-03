package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.InterceptablePlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerRegistry
@Inject
constructor(nativeFunctionExecutor: SAMPNativeFunctionExecutor) : EntityRegistry<InterceptablePlayer, PlayerId>(
        arrayOfNulls(nativeFunctionExecutor.getMaxPlayers())
)
