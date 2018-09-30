package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.GangZoneFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.GangZoneRegistry
import javax.inject.Inject

class GangZoneService
@Inject
internal constructor(
        private val gangZoneFactory: GangZoneFactory,
        private val gangZoneRegistry: GangZoneRegistry
) {

    fun createGangZone(area: Rectangle): GangZone = gangZoneFactory.create(area)

    fun isValid(gangZoneId: GangZoneId): Boolean = gangZoneRegistry[gangZoneId] != null

    fun getGangZone(gangZoneId: GangZoneId): GangZone =
            gangZoneRegistry[gangZoneId] ?: throw NoSuchEntityException("No gang zone with ID ${gangZoneId.value}")

    fun getAllGangZones(): List<GangZone> = gangZoneRegistry.getAll()

}