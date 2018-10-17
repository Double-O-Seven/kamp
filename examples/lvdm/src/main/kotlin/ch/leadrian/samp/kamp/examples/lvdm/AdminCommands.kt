package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.AdminCommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.SanAndreasZone
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AccessCheck([AdminCommandAccessChecker::class])
class AdminCommands
@Inject
constructor(
        private val dialogService: DialogService,
        private val vehicleService: VehicleService,
        private val messageSender: MessageSender,
        private val mapObjectService: MapObjectService
) : Commands() {

    private val objectsByName: MutableMap<String, MapObject> = mutableMapOf()

    @Unlisted
    @Command
    fun admcmds(player: Player) {
        showCommandList(player)
    }

    @Command
    fun setPos(player: Player, x: Float, y: Float, z: Float) {
        val coordinates = vector3DOf(x, y, z)
        player.setCoordinatesFindZ(coordinates)
        val zone = SanAndreasZone.getZone(coordinates) ?: "San Andreas"
        messageSender.sendMessageToPlayer(player, Colors.LIGHT_BLUE, TextKeys.lvdm.command.setpos.message, zone)
    }

    @Command(aliases = ["v", "veh"])
    fun vehicles(player: Player) {
        dialogService.createTabListDialog<Vehicle> {
            caption(TextKeys.lvdm.command.vehicles.dialog.caption)
            leftButton(TextKeys.lvdm.command.vehicles.dialog.button.select)
            headerContent(TextKeys.lvdm.command.vehicles.dialog.tab.model, TextKeys.lvdm.command.vehicles.dialog.tab.location)
            vehicleService.getAllVehicles().forEach { vehicle ->
                item {
                    value(vehicle)
                    val locationTextKey = SanAndreasZone.getZone(vehicle.coordinates)?.textKey
                            ?: ch.leadrian.samp.kamp.core.TextKeys.zone.name.sanandreas
                    tabbedContent(vehicle.model.textKey, locationTextKey)
                }
            }
            onSelectItem { _, tabListDialogItem, _ ->
                val vehicle = tabListDialogItem.value
                player.putInVehicle(vehicle, 0)
            }
        }.show(player)
    }

    @Command
    fun createobj(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("model ID") modelId: Int
    ) {
        val coordinates = player.coordinates
        val mapObject = mapObjectService.createMapObject(modelId, coordinates, vector3DOf(0f, 0f, 0f))
        val oldMapObject = objectsByName.put(name, mapObject)
        oldMapObject?.destroy()
        mapObject.onMoved {
            messageSender.sendMessageToAll(Colors.YELLOW, "$name stopped moving")
        }
    }

    @Command
    fun moveobj(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("x") x: Float,
            @Parameter("y") y: Float,
            @Parameter("z") z: Float,
            @Parameter("speed") speed: Float
    ) {
        val mapObject = objectsByName[name] ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.moveTo(vector3DOf(x, y, z), speed)
    }

    @Command
    fun stopobj(player: Player, name: String) {
        val mapObject = objectsByName[name] ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.stop()
    }

    @Command
    fun objpos(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("x") x: Float,
            @Parameter("y") y: Float,
            @Parameter("z") z: Float
    ) {
        val mapObject = objectsByName[name] ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.coordinates = vector3DOf(x, y, z)
    }

    @Command
    fun attachobj(player: Player, @Parameter("name") name: String) {
        val mapObject = objectsByName[name] ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.attachTo(player, vector3DOf(0f, 0f, 0f), vector3DOf(0f, 0f, 0f))
    }
}