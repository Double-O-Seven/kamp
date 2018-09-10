package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerRegistry
@Inject
constructor(nativeFunctionExecutor: SAMPNativeFunctionExecutor) : EntityRegistry<Player, PlayerId>(
        arrayOfNulls(nativeFunctionExecutor.getMaxPlayers())
)
