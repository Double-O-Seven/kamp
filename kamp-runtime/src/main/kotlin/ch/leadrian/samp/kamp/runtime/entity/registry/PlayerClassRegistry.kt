package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.PlayerClass
import ch.leadrian.samp.kamp.api.entity.id.PlayerClassId
import javax.inject.Singleton

@Singleton
internal class PlayerClassRegistry : EntityRegistry<PlayerClass, PlayerClassId>(arrayOfNulls(320))
