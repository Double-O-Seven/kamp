package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import com.google.inject.ImplementedBy

@ImplementedBy(EntityResolverImpl::class)
interface EntityResolver {

    fun Int.toPlayer(): Player

    fun Int.toPlayerOrNull(): Player?

    fun Int.toPlayerClass(): PlayerClass

    fun Int.toVehicle(): Vehicle

    fun Int.toMapObject(): MapObject

    fun Int.toPlayerMapObject(player: Player): PlayerMapObject

    fun Int.toPickup(): Pickup

    fun Int.toActor(): Actor

    fun Int.toTextDrawOrNull(): TextDraw?

    fun Int.toPlayerTextDraw(player: Player): PlayerTextDraw

}