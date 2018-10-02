package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.MapObjectFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import javax.inject.Inject

class MapObjectService
@Inject
internal constructor(
        private val mapObjectFactory: MapObjectFactory,
        private val mapObjectRegistry: MapObjectRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    @JvmOverloads
    fun createMapObject(modelId: Int, coordinates: Vector3D, rotation: Vector3D, drawDistance: Float = 0f): MapObject =
            mapObjectFactory.create(modelId, coordinates, rotation, drawDistance)

    fun isValidMapObject(mapObjectId: MapObjectId): Boolean =
            nativeFunctionExecutor.isValidObject(mapObjectId.value)

    fun getMapObject(mapObjectId: MapObjectId): MapObject =
            mapObjectRegistry[mapObjectId] ?: throw NoSuchEntityException("No map object with ID ${mapObjectId.value}")

    fun getAllMapObjects(): List<MapObject> = mapObjectRegistry.getAll()

    fun enableCameraCollisions() =
            nativeFunctionExecutor.setObjectsDefaultCameraCol(false)

    fun disableCameraCollisions() =
            nativeFunctionExecutor.setObjectsDefaultCameraCol(true)

}