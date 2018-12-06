package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import javax.inject.Inject

internal class MapObjectFactory
@Inject
constructor(
        private val mapObjectRegistry: MapObjectRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(model: Int, coordinates: Vector3D, rotation: Vector3D, drawDistance: Float): MapObject {
        val mapObject = MapObject(
                modelId = model,
                coordinates = coordinates,
                rotation = rotation,
                drawDistance = drawDistance,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        mapObjectRegistry.register(mapObject)
        mapObject.onDestroy { mapObjectRegistry.unregister(this) }
        return mapObject
    }

}