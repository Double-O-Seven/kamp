package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import javax.inject.Inject

internal class MapObjectCommandParameterResolver
@Inject
constructor(mapObjectRegistry: MapObjectRegistry) :
        EntityCommandParameterResolver<MapObject, MapObjectId>(mapObjectRegistry) {

    override val parameterType: Class<MapObject> = MapObject::class.java

}