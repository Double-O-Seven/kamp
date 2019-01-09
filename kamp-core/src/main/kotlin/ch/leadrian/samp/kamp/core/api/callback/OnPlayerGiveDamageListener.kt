package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerGiveDamageListener {

    fun onPlayerGiveDamage(
            player: Player,
            damagedPlayer: Player,
            amount: Float,
            weaponModel: WeaponModel,
            bodyPart: BodyPart
    )

}
