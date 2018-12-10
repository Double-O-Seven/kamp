package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface CheckpointBase : Destroyable {

    var coordinates: Vector3D

    var size: Float

    operator fun contains(player: Player): Boolean

}