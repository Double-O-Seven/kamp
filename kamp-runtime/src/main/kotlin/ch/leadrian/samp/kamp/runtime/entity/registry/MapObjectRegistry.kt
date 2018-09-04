package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.runtime.entity.MapObjectImpl
import javax.inject.Singleton

@Singleton
internal class MapObjectRegistry : EntityRegistry<MapObjectImpl, MapObjectId>(arrayOfNulls(SAMPConstants.MAX_OBJECTS))
