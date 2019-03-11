package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerWeaponShotListener {

    @IgnoredReturnValue(Result.AllowDamage::class)
    fun onPlayerShotWeapon(
            player: Player,
            weaponModel: WeaponModel,
            hitTarget: HitTarget,
            coordinates: Vector3D
    ): Result

    sealed class Result(val value: Boolean) {

        object AllowDamage : Result(true)

        object PreventDamage : Result(false)
    }

}
