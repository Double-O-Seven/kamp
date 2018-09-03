package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.api.data.AngledLocation
import ch.leadrian.samp.kamp.api.data.Location
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.VehicleColors
import ch.leadrian.samp.kamp.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.api.data.VehicleDoorStates
import ch.leadrian.samp.kamp.api.data.VehicleParameters
import ch.leadrian.samp.kamp.api.data.VehicleWindowStates
import ch.leadrian.samp.kamp.api.entity.id.VehicleId

interface Vehicle : Destroyable, Entity<VehicleId> {

    override val id: VehicleId

    fun isStreamedIn(forPlayer: Player): Boolean

    var coordinates: Vector3D

    var angle: Float

    var interiorId: Int

    var virtualWorldId: Int

    var position: Position

    var location: Location

    var angledLocation: AngledLocation

    fun respawn()

    fun setParametersForPlayer(forPlayer: Player, objective: Boolean, locked: Boolean)

    val sirenState: VehicleSirenState

    var parameters: VehicleParameters

    var doorStates: VehicleDoorStates

    var windowStates: VehicleWindowStates

    val components: VehicleComponents

    var colors: VehicleColors

    var paintjob: Int?

    var health: Float

    val trailer: VehicleTrailer

    var numberPlate: String?

    val model: VehicleModel

    fun repair()

    var velocity: Vector3D

    fun setAngularVelocity(velocity: Vector3D)

    var damageStatus: VehicleDamageStatus

    fun onSpawn(onSpawn: Vehicle.() -> Unit)

    fun onDeath(onDeath: Vehicle.(Player?) -> Unit)

    fun onEnter(onEnter: Vehicle.(Player, Boolean) -> Unit)

    fun onExit(onExit: Vehicle.(Player) -> Unit)

}