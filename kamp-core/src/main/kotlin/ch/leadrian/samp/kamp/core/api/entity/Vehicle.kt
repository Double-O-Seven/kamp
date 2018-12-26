package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorStates
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleLightsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehiclePanelDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleParameters
import ch.leadrian.samp.kamp.core.api.data.VehicleTiresDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleWindowStates
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleDoorStatesOf
import ch.leadrian.samp.kamp.core.api.data.vehicleParametersOf
import ch.leadrian.samp.kamp.core.api.data.vehicleWindowStatesOf
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt

class Vehicle
internal constructor(
        val model: VehicleModel,
        colors: VehicleColors,
        coordinates: Vector3D,
        rotation: Float,
        addSiren: Boolean,
        respawnDelay: Int?,
        vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<VehicleId>, AbstractDestroyable() {

    private val onSpawnHandlers: MutableList<Vehicle.() -> Unit> = mutableListOf()

    private val onDeathHandlers: MutableList<Vehicle.(Player?) -> Unit> = mutableListOf()

    private val onEnterHandlers: MutableList<Vehicle.(Player, Boolean) -> Unit> = mutableListOf()

    private val onExitHandlers: MutableList<Vehicle.(Player) -> Unit> = mutableListOf()

    private val onVehicleStreamInListeners: MutableList<OnVehicleStreamInListener> = mutableListOf()

    private val onVehicleStreamOutListeners: MutableList<OnVehicleStreamOutListener> = mutableListOf()

    val components: VehicleComponents = VehicleComponents(this, nativeFunctionExecutor)

    val trailer: VehicleTrailer = VehicleTrailer(this, vehicleRegistry, nativeFunctionExecutor)

    override val id: VehicleId
        get() = requireNotDestroyed { field }

    init {
        val vehicleId: Int = nativeFunctionExecutor.createVehicle(
                vehicletype = model.value,
                color1 = colors.color1.value,
                color2 = colors.color2.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                rotation = rotation,
                addsiren = addSiren,
                respawn_delay = respawnDelay ?: -1
        )

        if (vehicleId == SAMPConstants.INVALID_VEHICLE_ID) {
            throw CreationFailedException("Could not create vehicle")
        }

        id = VehicleId.valueOf(vehicleId)
    }

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isVehicleStreamedIn(vehicleid = id.value, forplayerid = forPlayer.id.value)

    var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehiclePos(vehicleid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setVehiclePos(vehicleid = id.value, x = value.x, y = value.y, z = value.z)
        }

    var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getVehicleZAngle(vehicleid = id.value, z_angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setVehicleZAngle(vehicleid = id.value, z_angle = value)
        }

    var interiorId: Int = 0
        set(value) {
            nativeFunctionExecutor.linkVehicleToInterior(vehicleid = id.value, interiorid = value)
            field = value
        }

    var virtualWorldId: Int
        get() = nativeFunctionExecutor.getVehicleVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setVehicleVirtualWorld(vehicleid = id.value, worldid = value)
        }

    var position: Position
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehiclePos(vehicleid = id.value, x = x, y = y, z = z)
            return positionOf(x = x.value, y = y.value, z = z.value, angle = angle)
        }
        set(value) {
            coordinates = value
            angle = value.angle
        }

    var location: Location
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehiclePos(vehicleid = id.value, x = x, y = y, z = z)
            return locationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId
            )
        }
        set(value) {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
        }

    var angledLocation: AngledLocation
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehiclePos(vehicleid = id.value, x = x, y = y, z = z)
            return angledLocationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId,
                    angle = angle
            )
        }
        set(value) {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
            angle = value.angle
        }

    fun respawn() {
        nativeFunctionExecutor.setVehicleToRespawn(id.value)
    }

    fun setParametersForPlayer(forPlayer: Player, objective: Boolean, locked: Boolean) {
        nativeFunctionExecutor.setVehicleParamsForPlayer(
                vehicleid = id.value,
                playerid = forPlayer.id.value,
                objective = if (objective) 1 else 0,
                doorslocked = if (locked) 1 else 0
        )
    }

    val sirenState: VehicleSirenState
        get() = nativeFunctionExecutor.getVehicleParamsSirenState(id.value).let { VehicleSirenState[it] }

    var parameters: VehicleParameters
        get() {
            val engine = ReferenceInt()
            val objective = ReferenceInt()
            val boot = ReferenceInt()
            val bonnet = ReferenceInt()
            val lights = ReferenceInt()
            val alarm = ReferenceInt()
            val doors = ReferenceInt()
            nativeFunctionExecutor.getVehicleParamsEx(
                    vehicleid = id.value,
                    engine = engine,
                    objective = objective,
                    boot = boot,
                    bonnet = bonnet,
                    lights = lights,
                    alarm = alarm,
                    doors = doors
            )
            return vehicleParametersOf(
                    engine = VehicleEngineState[engine.value],
                    objective = VehicleObjectiveState[objective.value],
                    boot = VehicleBootState[boot.value],
                    bonnet = VehicleBonnetState[bonnet.value],
                    lights = VehicleLightsState[lights.value],
                    alarm = VehicleAlarmState[alarm.value],
                    doorLock = VehicleDoorLockState[doors.value]
            )
        }
        set(value) {
            nativeFunctionExecutor.setVehicleParamsEx(
                    vehicleid = id.value,
                    engine = value.engine.value,
                    objective = value.objective.value,
                    boot = value.boot.value,
                    bonnet = value.bonnet.value,
                    lights = value.lights.value,
                    alarm = value.alarm.value,
                    doors = value.doorLock.value
            )
        }

    var doorStates: VehicleDoorStates
        get() {
            val driver = ReferenceInt()
            val passenger = ReferenceInt()
            val backLeft = ReferenceInt()
            val backRight = ReferenceInt()
            nativeFunctionExecutor.getVehicleParamsCarDoors(
                    vehicleid = id.value,
                    driver = driver,
                    passenger = passenger,
                    backleft = backLeft,
                    backright = backRight
            )
            return vehicleDoorStatesOf(
                    driver = VehicleDoorState[driver.value],
                    passenger = VehicleDoorState[passenger.value],
                    backLeft = VehicleDoorState[backLeft.value],
                    backRight = VehicleDoorState[backRight.value]
            )
        }
        set(value) {
            nativeFunctionExecutor.setVehicleParamsCarDoors(
                    vehicleid = id.value,
                    driver = value.driver.value,
                    passenger = value.passenger.value,
                    backleft = value.backLeft.value,
                    backright = value.backRight.value
            )
        }

    var windowStates: VehicleWindowStates
        get() {
            val driver = ReferenceInt()
            val passenger = ReferenceInt()
            val backLeft = ReferenceInt()
            val backRight = ReferenceInt()
            nativeFunctionExecutor.getVehicleParamsCarWindows(
                    vehicleid = id.value,
                    driver = driver,
                    passenger = passenger,
                    backleft = backLeft,
                    backright = backRight
            )
            return vehicleWindowStatesOf(
                    driver = VehicleWindowState[driver.value],
                    passenger = VehicleWindowState[passenger.value],
                    backLeft = VehicleWindowState[backLeft.value],
                    backRight = VehicleWindowState[backRight.value]
            )
        }
        set(value) {
            nativeFunctionExecutor.setVehicleParamsCarWindows(
                    vehicleid = id.value,
                    driver = value.driver.value,
                    passenger = value.passenger.value,
                    backleft = value.backLeft.value,
                    backright = value.backRight.value
            )
        }

    var colors: VehicleColors = colors.toVehicleColors()
        set(value) {
            nativeFunctionExecutor.changeVehicleColor(
                    vehicleid = id.value,
                    color1 = value.color1.value,
                    color2 = value.color2.value
            )
            field = value.toVehicleColors()
        }

    var paintjob: Int? = null
        set(value) {
            nativeFunctionExecutor.changeVehiclePaintjob(vehicleid = id.value, paintjobid = value ?: -1)
            field = value
        }

    var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getVehicleHealth(vehicleid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setVehicleHealth(vehicleid = id.value, health = value)
        }

    var numberPlate: String? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                nativeFunctionExecutor.setVehicleNumberPlate(id.value, value)
                field = value
            }
        }

    fun repair() {
        nativeFunctionExecutor.repairVehicle(id.value)
    }

    var velocity: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehicleVelocity(vehicleid = id.value, X = x, Y = y, Z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setVehicleVelocity(vehicleid = id.value, X = value.x, Y = value.y, Z = value.z)
        }

    fun setAngularVelocity(velocity: Vector3D) {
        nativeFunctionExecutor.setVehicleAngularVelocity(
                vehicleid = id.value,
                X = velocity.x,
                Y = velocity.y,
                Z = velocity.z
        )
    }

    var damageStatus: VehicleDamageStatus
        get() {
            val lights = ReferenceInt()
            val doors = ReferenceInt()
            val panels = ReferenceInt()
            val tires = ReferenceInt()
            nativeFunctionExecutor.getVehicleDamageStatus(
                    vehicleid = id.value,
                    lights = lights,
                    doors = doors,
                    panels = panels,
                    tires = tires
            )
            return VehicleDamageStatus(
                    tires = VehicleTiresDamageStatus(tires.value),
                    panels = VehiclePanelDamageStatus(panels.value),
                    doors = VehicleDoorsDamageStatus(doors.value),
                    lights = VehicleLightsDamageStatus(lights.value)
            )
        }
        set(value) {
            nativeFunctionExecutor.updateVehicleDamageStatus(
                    vehicleid = id.value,
                    tires = value.tires.value,
                    panels = value.panels.value,
                    doors = value.doors.value,
                    lights = value.lights.value
            )
        }

    operator fun contains(player: Player): Boolean = player.isInVehicle(this)

    fun onSpawn(onSpawn: Vehicle.() -> Unit) {
        onSpawnHandlers += onSpawn
    }

    internal fun onSpawn() {
        onSpawnHandlers.forEach { it.invoke(this) }
    }

    fun onDeath(onDeath: Vehicle.(Player?) -> Unit) {
        onDeathHandlers += onDeath
    }

    internal fun onDeath(killer: Player?) {
        onDeathHandlers.forEach { it.invoke(this, killer) }
    }

    fun onEnter(onEnter: Vehicle.(Player, Boolean) -> Unit) {
        onEnterHandlers += onEnter
    }

    internal fun onEnter(player: Player, isPassenger: Boolean) {
        onEnterHandlers.forEach { it.invoke(this, player, isPassenger) }
    }

    fun onExit(onExit: Vehicle.(Player) -> Unit) {
        onExitHandlers += onExit
    }

    internal fun onExit(player: Player) {
        onExitHandlers.forEach { it.invoke(this, player) }
    }

    fun addOnVehicleStreamInListener(listener: OnVehicleStreamInListener) {
        onVehicleStreamInListeners += listener
    }

    fun removeOnVehicleStreamInListener(listener: OnVehicleStreamInListener) {
        onVehicleStreamInListeners -= listener
    }

    inline fun onStreamIn(crossinline onStreamIn: Vehicle.(Player) -> Unit): OnVehicleStreamInListener {
        val listener = object : OnVehicleStreamInListener {

            override fun onVehicleStreamIn(vehicle: Vehicle, forPlayer: Player) {
                onStreamIn.invoke(vehicle, forPlayer)
            }
        }
        addOnVehicleStreamInListener(listener)
        return listener
    }

    internal fun onStreamIn(player: Player) {
        onVehicleStreamInListeners.forEach { it.onVehicleStreamIn(this, player) }
    }

    fun addOnVehicleStreamOutListener(listener: OnVehicleStreamOutListener) {
        onVehicleStreamOutListeners += listener
    }

    fun removeOnVehicleStreamOutListener(listener: OnVehicleStreamOutListener) {
        onVehicleStreamOutListeners -= listener
    }

    inline fun onStreamOut(crossinline onStreamOut: Vehicle.(Player) -> Unit): OnVehicleStreamOutListener {
        val listener = object : OnVehicleStreamOutListener {

            override fun onVehicleStreamOut(vehicle: Vehicle, forPlayer: Player) {
                onStreamOut.invoke(vehicle, forPlayer)
            }
        }
        addOnVehicleStreamOutListener(listener)
        return listener
    }

    internal fun onStreamOut(player: Player) {
        onVehicleStreamOutListeners.forEach { it.onVehicleStreamOut(this, player) }
    }

    override fun onDestroy() {
        nativeFunctionExecutor.destroyVehicle(id.value)
    }
}