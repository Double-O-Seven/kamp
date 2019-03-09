package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.base.HasModelId
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Quaternion
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorStates
import ch.leadrian.samp.kamp.core.api.data.VehicleParameters
import ch.leadrian.samp.kamp.core.api.data.VehicleWindowStates
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEnterVehicleReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerExitVehicleReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleDeathReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehiclePaintjobReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleResprayReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleSpawnReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleStreamInReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnVehicleStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleAngleProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleAngledLocationProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleCoordinatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleDamageStatusProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleDoorStatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleHealthProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleLocationProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleParametersProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehiclePositionProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleRotationQuaternionProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleVelocityProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.VehicleWindowStatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry

class Vehicle
internal constructor(
        val model: VehicleModel,
        colors: VehicleColors,
        coordinates: Vector3D,
        rotation: Float,
        addSiren: Boolean,
        respawnDelay: Int?,
        vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onVehicleSpawnReceiver: OnVehicleSpawnReceiverDelegate = OnVehicleSpawnReceiverDelegate(),
        private val onVehicleDeathReceiver: OnVehicleDeathReceiverDelegate = OnVehicleDeathReceiverDelegate(),
        private val onPlayerEnterVehicleReceiver: OnPlayerEnterVehicleReceiverDelegate = OnPlayerEnterVehicleReceiverDelegate(),
        private val onPlayerExitVehicleReceiver: OnPlayerExitVehicleReceiverDelegate = OnPlayerExitVehicleReceiverDelegate(),
        private val onVehicleStreamInReceiver: OnVehicleStreamInReceiverDelegate = OnVehicleStreamInReceiverDelegate(),
        private val onVehicleStreamOutReceiver: OnVehicleStreamOutReceiverDelegate = OnVehicleStreamOutReceiverDelegate(),
        private val onVehicleResprayReceiver: OnVehicleResprayReceiverDelegate = OnVehicleResprayReceiverDelegate(),
        private val onVehiclePaintjobReceiver: OnVehiclePaintjobReceiverDelegate = OnVehiclePaintjobReceiverDelegate()
) : Entity<VehicleId>,
        AbstractDestroyable(),
        Extendable<Vehicle>,
        HasModelId by model,
        OnVehicleSpawnReceiver by onVehicleSpawnReceiver,
        OnVehicleDeathReceiver by onVehicleDeathReceiver,
        OnPlayerEnterVehicleReceiver by onPlayerEnterVehicleReceiver,
        OnPlayerExitVehicleReceiver by onPlayerExitVehicleReceiver,
        OnVehicleStreamInReceiver by onVehicleStreamInReceiver,
        OnVehicleStreamOutListener by onVehicleStreamOutReceiver,
        OnVehicleResprayReceiver by onVehicleResprayReceiver,
        OnVehiclePaintjobReceiver by onVehiclePaintjobReceiver {

    override val extensions: EntityExtensionContainer<Vehicle> = EntityExtensionContainer(this)

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

    var coordinates: Vector3D by VehicleCoordinatesProperty(nativeFunctionExecutor)

    var angle: Float by VehicleAngleProperty(nativeFunctionExecutor)

    val rotationQuaternion: Quaternion by VehicleRotationQuaternionProperty(nativeFunctionExecutor)

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

    var position: Position by VehiclePositionProperty(nativeFunctionExecutor)

    var location: Location by VehicleLocationProperty(nativeFunctionExecutor)

    var angledLocation: AngledLocation by VehicleAngledLocationProperty(nativeFunctionExecutor)

    val sirenState: VehicleSirenState
        get() = nativeFunctionExecutor.getVehicleParamsSirenState(id.value).let { VehicleSirenState[it] }

    var parameters: VehicleParameters by VehicleParametersProperty(nativeFunctionExecutor)

    var doorStates: VehicleDoorStates by VehicleDoorStatesProperty(nativeFunctionExecutor)

    var windowStates: VehicleWindowStates by VehicleWindowStatesProperty(nativeFunctionExecutor)

    private val initialColors: VehicleColors = colors.toVehicleColors()

    private var _colors: VehicleColors = initialColors

    var colors: VehicleColors
        get() = _colors
        set(value) {
            nativeFunctionExecutor.changeVehicleColor(
                    vehicleid = id.value,
                    color1 = value.color1.value,
                    color2 = value.color2.value
            )
            _colors = value.toVehicleColors()
        }

    private var _paintjob: Int? = null

    var paintjob: Int?
        get() = _paintjob
        set(value) {
            if (value != null) {
                nativeFunctionExecutor.changeVehiclePaintjob(vehicleid = id.value, paintjobid = value)
                _paintjob = value
            }
        }

    var health: Float by VehicleHealthProperty(nativeFunctionExecutor)

    var numberPlate: String? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                nativeFunctionExecutor.setVehicleNumberPlate(id.value, value)
                field = value
            }
        }

    var velocity: Vector3D by VehicleVelocityProperty(nativeFunctionExecutor)

    var damageStatus: VehicleDamageStatus by VehicleDamageStatusProperty(nativeFunctionExecutor)

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isVehicleStreamedIn(vehicleid = id.value, forplayerid = forPlayer.id.value)

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

    fun repair() {
        nativeFunctionExecutor.repairVehicle(id.value)
    }

    fun setAngularVelocity(velocity: Vector3D) {
        nativeFunctionExecutor.setVehicleAngularVelocity(
                vehicleid = id.value,
                X = velocity.x,
                Y = velocity.y,
                Z = velocity.z
        )
    }

    operator fun contains(player: Player): Boolean = player.isInVehicle(this)

    internal fun onSpawn() {
        _colors = initialColors
        _paintjob = null
        onVehicleSpawnReceiver.onVehicleSpawn(this)
    }

    internal fun onEnter(player: Player, isPassenger: Boolean) {
        onPlayerEnterVehicleReceiver.onPlayerEnterVehicle(player, this, isPassenger)
    }

    internal fun onExit(player: Player) {
        onPlayerExitVehicleReceiver.onPlayerExitVehicle(player, this)
    }

    internal fun onDeath(killer: Player?) {
        onVehicleDeathReceiver.onVehicleDeath(this, killer)
    }

    internal fun onStreamIn(player: Player) {
        onVehicleStreamInReceiver.onVehicleStreamIn(this, player)
    }

    internal fun onStreamOut(player: Player) {
        onVehicleStreamOutReceiver.onVehicleStreamOut(this, player)
    }

    internal fun onRespray(player: Player, colors: VehicleColors): OnVehicleResprayListener.Result {
        val result = onVehicleResprayReceiver.onVehicleRespray(player, this, colors)
        if (result != OnVehicleResprayListener.Result.Desync) {
            _colors = colors
        }
        return result
    }

    internal fun onPaintjobChange(player: Player, paintjobId: Int) {
        _paintjob = paintjobId
        onVehiclePaintjobReceiver.onVehiclePaintjob(player, this, paintjobId)
    }

    override fun onDestroy() {
        extensions.destroy()
        nativeFunctionExecutor.destroyVehicle(id.value)
    }
}