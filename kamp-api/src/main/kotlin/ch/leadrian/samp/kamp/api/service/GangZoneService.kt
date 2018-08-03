package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Rectangle
import ch.leadrian.samp.kamp.api.entity.GangZone
import ch.leadrian.samp.kamp.api.entity.id.GangZoneId

interface GangZoneService {

    fun createGangZone(area: Rectangle): GangZone

    fun isValid(gangZoneId: GangZoneId): Boolean

    fun getGangZone(gangZoneId: GangZoneId): GangZone

    fun getAllGangZones(): List<GangZone>

}