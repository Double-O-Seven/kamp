package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerWeaponShotListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerWeaponShotListener>(OnPlayerWeaponShotListener::class), OnPlayerWeaponShotListener {

    override fun onPlayerShotWeapon(player: ch.leadrian.samp.kamp.core.api.entity.Player, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, hitTarget: ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target<*>, coordinates: ch.leadrian.samp.kamp.core.api.data.Vector3D): ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Result {
        getListeners().forEach {
            it.onPlayerShotWeapon(player, weaponModel, hitTarget, coordinates)
        }
    }

}
