package ch.leadrian.samp.kamp.core.runtime.entity.registry

import com.google.inject.AbstractModule

internal class EntityRegistryModule : AbstractModule() {

    override fun configure() {
        bind(ActorRegistry::class.java)
        bind(DialogRegistry::class.java)
        bind(GangZoneRegistry::class.java)
        bind(MapObjectRegistry::class.java)
        bind(MenuRegistry::class.java)
        bind(PickupRegistry::class.java)
        bind(PlayerClassRegistry::class.java)
        bind(PlayerRegistry::class.java)
        bind(PlayerSearchIndex::class.java)
        bind(TextDrawRegistry::class.java)
        bind(TextLabelRegistry::class.java)
        bind(VehicleRegistry::class.java)
    }
}