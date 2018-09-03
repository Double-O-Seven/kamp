package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.*
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.runtime.types.ReferenceInt

internal class VehicleImpl(
        override val model: VehicleModel,
        colors: VehicleColors,
        coordinates: Vector3D,
        rotation: Float,
        addSiren: Boolean,
        respawnDelay: Int,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Vehicle {

    private val onSpawnHandlers: MutableList<Vehicle.() -> Unit> = mutableListOf()

    private val onDeathHandlers: MutableList<Vehicle.(Player?) -> Unit> = mutableListOf()

    private val onEnterHandlers: MutableList<Vehicle.(Player, Boolean) -> Unit> = mutableListOf()

    private val onExitHandlers: MutableList<Vehicle.(Player) -> Unit> = mutableListOf()

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
                respawn_delay = respawnDelay
        )

        if (vehicleId == SAMPConstants.INVALID_VEHICLE_ID) {
            throw CreationFailedException("Could not create vehicle")
        }

        id = VehicleId.valueOf(vehicleId)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isVehicleStreamedIn(vehicleid = id.value, forplayerid = forPlayer.id.value)

    override var coordinates: Vector3D
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

    override var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getVehicleZAngle(vehicleid = id.value, z_angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setVehicleZAngle(vehicleid = id.value, z_angle = value)
        }

    override var interiorId: Int = 0
        set(value) {
            nativeFunctionExecutor.linkVehicleToInterior(vehicleid = id.value, interiorid = value)
            field = value
        }

    override var virtualWorldId: Int
        get() = nativeFunctionExecutor.getVehicleVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setVehicleVirtualWorld(vehicleid = id.value, worldid = value)
        }

    override var position: Position
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

    override var location: Location
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

    override var angledLocation: AngledLocation
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

    override fun respawn() {
        nativeFunctionExecutor.setVehicleToRespawn(id.value)
    }

    override fun setParametersForPlayer(forPlayer: Player, objective: Boolean, locked: Boolean) {
        nativeFunctionExecutor.setVehicleParamsForPlayer(
                vehicleid = id.value,
                playerid = forPlayer.id.value,
                objective = if (objective) 1 else 0,
                doorslocked = if (locked) 1 else 0
        )
    }

    override val sirenState: VehicleSirenState
        get() = nativeFunctionExecutor.getVehicleParamsSirenState(id.value).let { VehicleSirenState[it] }

    override var parameters: VehicleParameters
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

    override var doorStates: VehicleDoorStates
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

    override var windowStates: VehicleWindowStates
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

    override val components: VehicleComponents =
            VehicleComponentsImpl(
                    vehicle = this,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

    override var colors: VehicleColors = colors.toVehicleColors()
        set(value) {
            nativeFunctionExecutor.changeVehicleColor(
                    vehicleid = id.value,
                    color1 = value.color1.value,
                    color2 = value.color2.value
            )
            field = value.toVehicleColors()
        }

    override var paintjob: Int? = null
        set(value) {
            nativeFunctionExecutor.changeVehiclePaintjob(vehicleid = id.value, paintjobid = value ?: -1)
            field = value
        }

    override var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getVehicleHealth(vehicleid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setVehicleHealth(vehicleid = id.value, health = value)
        }

    override val trailer: VehicleTrailer = VehicleTrailerImpl(
            vehicle = this,
            nativeFunctionExecutor = nativeFunctionExecutor,
            vehicleRegistry = vehicleRegistry
    )

    override var numberPlate: String? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                nativeFunctionExecutor.setVehicleNumberPlate(id.value, value)
                field = value
            }
        }

    override fun repair() {
        nativeFunctionExecutor.repairVehicle(id.value)
    }

    override var velocity: Vector3D
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

    override fun setAngularVelocity(velocity: Vector3D) {
        nativeFunctionExecutor.setVehicleAngularVelocity(
                vehicleid = id.value,
                X = velocity.x,
                Y = velocity.y,
                Z = velocity.z
        )
    }

    override var damageStatus: VehicleDamageStatus
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

    override fun onSpawn(onSpawn: Vehicle.() -> Unit) {
        onSpawnHandlers += onSpawn
    }

    internal fun onSpawn() {
        onSpawnHandlers.forEach { it.invoke(this) }
    }

    override fun onDeath(onDeath: Vehicle.(Player?) -> Unit) {
        onDeathHandlers += onDeath
    }

    internal fun onDeath(killer: Player?) {
        onDeathHandlers.forEach { it.invoke(this, killer) }
    }

    override fun onEnter(onEnter: Vehicle.(Player, Boolean) -> Unit) {
        onEnterHandlers += onEnter
    }

    internal fun onEnter(player: Player, isPassenger: Boolean) {
        onEnterHandlers.forEach { it.invoke(this, player, isPassenger) }
    }

    override fun onExit(onExit: Vehicle.(Player) -> Unit) {
        onExitHandlers += onExit
    }

    internal fun onExit(player: Player) {
        onExitHandlers.forEach { it.invoke(this, player) }
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        nativeFunctionExecutor.destroyVehicle(id.value)
        vehicleRegistry.unregister(this)
        isDestroyed = true
    }
}