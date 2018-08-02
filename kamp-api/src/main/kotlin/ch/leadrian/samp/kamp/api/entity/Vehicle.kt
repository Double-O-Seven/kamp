package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.VehicleColor
import ch.leadrian.samp.kamp.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.api.data.*
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

    var parameters: VehicleParameters

    var doorStates: VehicleDoorStates

    var windowStates: VehicleWindowStates

    fun addComponent(model: VehicleComponentModel)

    fun removeComponent(model: VehicleComponentModel)

    var color1: VehicleColor

    var color2: VehicleColor

    // TODO add more
}