package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerWeaponShotListener {

    @IgnoredReturnValue(Result.AllowDamage::class)
    fun onPlayerShotWeapon(player: Player, weaponModel: WeaponModel, hitTarget: Target, coordinates: Vector3D): Result

    sealed class Target(val type: BulletHitType) {

        data class PlayerTarget(val player: Player) : Target(BulletHitType.PLAYER)

        data class VehicleTarget(val vehicle: Vehicle) : Target(BulletHitType.VEHICLE)

        data class PlayerMapObjectTarget(val playerMapObject: PlayerMapObject) : Target(BulletHitType.PLAYER_OBJECT)

        data class MapObjectTarget(val mapObject: MapObject) : Target(BulletHitType.OBJECT)

        object NoTarget : Target(BulletHitType.NONE)
    }

    sealed class Result(val value: Boolean) {

        object AllowDamage : Result(true)

        object PreventDamage : Result(false)
    }

}
