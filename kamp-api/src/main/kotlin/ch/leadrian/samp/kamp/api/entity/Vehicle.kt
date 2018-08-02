package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.api.data.AngledLocation
import ch.leadrian.samp.kamp.api.data.Location
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.VehicleId

interface Vehicle : Destroyable {

    val id: VehicleId

    fun isStreamedIn(forPlayer: Player)

    var coordinates: Vector3D

    var position: Position

    var location: Location

    var angledLocation: AngledLocation

    var angle: Float

    var interiorId: Int

    var virtualWorld: Int

    fun respawn()

    fun setParametersForPlayer(forPlayer: Player, objective: Boolean, locked: Boolean)

    val sirenState: VehicleSirenState

    // TODO add more
}