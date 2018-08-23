package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.MapObject
import javax.inject.Singleton

@Singleton
internal class MapObjectRegistry {

    private val mapObjects: Array<MapObject?> = arrayOfNulls(SAMPConstants.MAX_OBJECTS)

    fun register(mapObject: MapObject) {
        if (mapObjects[mapObject.id.value] != null) {
            throw IllegalStateException("There is already a map object with ID ${mapObject.id.value} registered")
        }
        mapObjects[mapObject.id.value] = mapObject
    }

    fun unregister(mapObject: MapObject) {
        if (mapObjects[mapObject.id.value] !== mapObject) {
            throw IllegalStateException("Trying to unregister map object with ID ${mapObject.id.value} that is not registered")
        }
        mapObjects[mapObject.id.value] = null
    }

    fun getMapObject(mapObjectId: Int): MapObject? =
            when (mapObjectId) {
                in (0 until mapObjects.size) -> mapObjects[mapObjectId]
                else -> null
            }

    fun getAllMapObjects(): List<MapObject> = mapObjects.filterNotNull()

}