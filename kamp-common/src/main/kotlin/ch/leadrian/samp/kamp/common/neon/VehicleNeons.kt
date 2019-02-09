package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.HasVehicle
import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

class VehicleNeons
internal constructor(
        override val vehicle: Vehicle,
        private val mapObjectProvider: VehicleNeonMapObjectProvider
) : AbstractDestroyable(), HasVehicle, OnVehicleSpawnListener {

    private var vehicleNeonMapObjects: VehicleNeonMapObjects? = null

    init {
        vehicle.addOnVehicleSpawnListener(this)
    }

    fun attach(neonColor: NeonColor): Boolean {
        destroyVehicleNeons()
        val offset1 = NeonOffset[vehicle.model] ?: return false
        val offset2 = offset1.toMutableVector3D().apply { x = -x }
        val neonMapObject1 = mapObjectProvider.createNeon(neonColor)
        val neonMapObject2 = mapObjectProvider.createNeon(neonColor)
        vehicleNeonMapObjects = VehicleNeonMapObjects(
                mapObject1 = neonMapObject1,
                mapObject2 = neonMapObject2,
                offset1 = offset1,
                offset2 = offset2
        ).apply { attach() }
        return true
    }

    fun remove(): Boolean {
        if (vehicleNeonMapObjects == null) {
            return false
        }
        destroyVehicleNeons()
        return true
    }

    override fun onVehicleSpawn(vehicle: Vehicle) {
        vehicleNeonMapObjects?.attach()
    }

    override fun onDestroy() {
        destroyVehicleNeons()
    }

    private fun destroyVehicleNeons() {
        vehicleNeonMapObjects?.destroy()
        vehicleNeonMapObjects = null
    }

    private inner class VehicleNeonMapObjects(
            private val mapObject1: MapObjectBase,
            private val mapObject2: MapObjectBase,
            private val offset1: Vector3D,
            private val offset2: Vector3D
    ) : AbstractDestroyable() {

        fun attach() {
            mapObject1.attachTo(vehicle, offset1, Vector3D.ORIGIN)
            mapObject2.attachTo(vehicle, offset2, Vector3D.ORIGIN)
        }

        override fun onDestroy() {
            mapObject1.destroy()
            mapObject2.destroy()
        }

    }

}

val Vehicle.neons: VehicleNeons
    get() = extensions[VehicleNeons::class]
