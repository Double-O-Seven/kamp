package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.inject.KampModule

internal class EntityFactoryModule : KampModule() {

    override fun configure() {
        bind(ActorFactory::class.java)
        bind(CheckpointFactory::class.java)
        bind(GangZoneFactory::class.java)
        bind(MapObjectFactory::class.java)
        bind(MenuFactory::class.java)
        bind(PickupFactory::class.java)
        bind(PlayerFactory::class.java)
        bind(PlayerClassFactory::class.java)
        bind(PlayerMapIconFactory::class.java)
        bind(PlayerMapObjectFactory::class.java)
        bind(PlayerTextDrawFactory::class.java)
        bind(PlayerTextLabelFactory::class.java)
        bind(RaceCheckpointFactory::class.java)
        bind(TextDrawFactory::class.java)
        bind(TextLabelFactory::class.java)
        bind(VehicleFactory::class.java)
        newPlayerExtensionFactorySetBinder() // required in order to have at least an empty binding
    }

}