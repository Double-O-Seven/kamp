package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.MapObject
import ch.leadrian.samp.kamp.api.entity.id.MapObjectId

interface MapObjectService {

    fun createMapObject(modelId: Int, coordinates: Vector3D, rotation: Vector3D, drawDistance: Float = 0f): MapObject

    fun isValid(mapObjectId: MapObjectId): Boolean

    fun getMapObject(mapObjectId: MapObjectId): MapObject

    fun getAllMapObjects(): List<MapObject>

    fun disableDefaultCameraCollision(disable: Boolean)

}