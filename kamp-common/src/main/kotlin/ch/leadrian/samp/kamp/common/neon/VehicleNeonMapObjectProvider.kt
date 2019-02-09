package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase

interface VehicleNeonMapObjectProvider {

    fun createNeon(neonColor: NeonColor): MapObjectBase

}