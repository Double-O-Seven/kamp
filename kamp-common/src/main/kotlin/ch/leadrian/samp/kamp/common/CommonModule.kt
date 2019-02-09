package ch.leadrian.samp.kamp.common

import ch.leadrian.samp.kamp.common.neon.VehicleNeonsFactory
import ch.leadrian.samp.kamp.core.api.inject.KampModule

class CommonModule : KampModule() {

    override fun configure() {
        newVehicleExtensionFactorySetBinder().apply {
            addBinding().to(VehicleNeonsFactory::class.java)
        }
    }

}