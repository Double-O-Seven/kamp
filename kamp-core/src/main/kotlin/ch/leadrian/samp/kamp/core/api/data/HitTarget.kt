package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

sealed class HitTarget(val type: BulletHitType)

data class PlayerHitTarget(val player: Player) : HitTarget(BulletHitType.PLAYER)

data class VehicleHitTarget(val vehicle: Vehicle) : HitTarget(BulletHitType.VEHICLE)

data class PlayerMapObjectHitTarget(val playerMapObject: PlayerMapObject) : HitTarget(BulletHitType.PLAYER_OBJECT)

data class MapObjectHitTarget(val mapObject: MapObject) : HitTarget(BulletHitType.OBJECT)

object NoHitTarget : HitTarget(BulletHitType.NONE)