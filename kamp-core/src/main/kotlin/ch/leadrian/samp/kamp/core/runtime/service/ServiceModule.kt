package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.GangZoneService
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.MenuService
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.core.api.service.PlayerClassService
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.core.api.service.ServerService
import ch.leadrian.samp.kamp.core.api.service.TextLabelService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.service.WorldService

internal class ServiceModule : KampModule() {

    override fun configure() {
        bind(ActorService::class.java)
        bind(CheckpointService::class.java)
        bind(DialogService::class.java)
        bind(GangZoneService::class.java)
        bind(MapObjectService::class.java)
        bind(MenuService::class.java)
        bind(PickupService::class.java)
        bind(PlayerClassService::class.java)
        bind(PlayerMapObjectService::class.java)
        bind(PlayerService::class.java)
        bind(PlayerTextLabelService::class.java)
        bind(RaceCheckpointService::class.java)
        bind(ServerService::class.java)
        bind(TextLabelService::class.java)
        bind(VehicleService::class.java)
        bind(WorldService::class.java)
    }

}