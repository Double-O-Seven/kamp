package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import com.google.inject.Inject

internal class DefaultVehicleNeonMapObjectProvider
@Inject
constructor(private val mapObjectService: MapObjectService) : VehicleNeonMapObjectProvider {

    override fun createNeon(neonColor: NeonColor): MapObjectBase =
            mapObjectService.createMapObject(neonColor.modelId, Vector3D.ORIGIN, Vector3D.ORIGIN)

}