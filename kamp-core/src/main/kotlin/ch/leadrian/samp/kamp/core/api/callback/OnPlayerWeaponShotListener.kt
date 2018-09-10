package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnPlayerWeaponShotListener {

    fun onPlayerShotWeapon(player: Player, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, hitTarget: Target<*>, coordinates: Vector3D): Result

    sealed class Target<V>(val target: V, val type: ch.leadrian.samp.kamp.core.api.constants.BulletHitType) {

        class PlayerTarget(target: Player) : Target<Player>(target = target, type = ch.leadrian.samp.kamp.core.api.constants.BulletHitType.PLAYER)

        class VehicleTarget(target: Vehicle) : Target<Vehicle>(target = target, type = ch.leadrian.samp.kamp.core.api.constants.BulletHitType.VEHICLE)

        class PlayerMapObjectTarget(target: PlayerMapObject) : Target<PlayerMapObject>(target = target, type = ch.leadrian.samp.kamp.core.api.constants.BulletHitType.PLAYER_OBJECT)

        class MapObjectTarget(target: MapObject) : Target<MapObject>(target = target, type = ch.leadrian.samp.kamp.core.api.constants.BulletHitType.OBJECT)

        class NoTarget : Target<Any?>(target = null, type = ch.leadrian.samp.kamp.core.api.constants.BulletHitType.NONE)
    }

    sealed class Result(val value: Boolean) {

        object AllowDamage : Result(true)

        object PreventDamage : Result(false)
    }

}
