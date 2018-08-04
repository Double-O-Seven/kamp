package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerClickMapListener {

    fun onPlayerClickMap(player: Player, coordinates: Vector3D): Boolean

}
