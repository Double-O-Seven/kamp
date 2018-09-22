package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Result
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerWeaponShotListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerWeaponShotListener>(OnPlayerWeaponShotListener::class), OnPlayerWeaponShotListener {

    override fun onPlayerShotWeapon(player: Player, weaponModel: WeaponModel, hitTarget: OnPlayerWeaponShotListener.Target<*>, coordinates: Vector3D): Result {
        return listeners.map {
            it.onPlayerShotWeapon(player, weaponModel, hitTarget, coordinates)
        }.firstOrNull { it == Result.PreventDamage } ?: Result.AllowDamage
    }

}
