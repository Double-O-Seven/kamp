package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnPlayerWeaponShotListener {

    fun onPlayerShotWeapon(player: Player, weaponModel: WeaponModel, hitTarget: Target, coordinates: Vector3D): Result

    sealed class Target(val type: BulletHitType) {

        class PlayerTarget(val target: Player) : Target(BulletHitType.PLAYER)

        class VehicleTarget(val target: Vehicle) : Target(BulletHitType.VEHICLE)

        class PlayerMapObjectTarget(val target: PlayerMapObject) : Target(BulletHitType.PLAYER_OBJECT)

        class MapObjectTarget(val target: MapObject) : Target(BulletHitType.OBJECT)

        object NoTarget : Target(BulletHitType.NONE)
    }

    sealed class Result(val value: Boolean) {

        object AllowDamage : Result(true)

        object PreventDamage : Result(false)
    }

}
