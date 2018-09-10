package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerClickMapListener {

    fun onPlayerClickMap(player: Player, coordinates: Vector3D): Boolean

}
