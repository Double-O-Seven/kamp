package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerClassRegistry
@Inject
constructor() : EntityRegistry<PlayerClass, PlayerClassId>(arrayOfNulls(320))
