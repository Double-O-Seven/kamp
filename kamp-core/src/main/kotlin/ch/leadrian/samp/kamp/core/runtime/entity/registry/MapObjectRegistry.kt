package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapObjectRegistry
@Inject
constructor() : EntityRegistry<MapObject, MapObjectId>(arrayOfNulls(SAMPConstants.MAX_OBJECTS))
