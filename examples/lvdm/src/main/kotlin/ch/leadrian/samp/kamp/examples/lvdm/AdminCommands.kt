package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.AdminCommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.Interior
import ch.leadrian.samp.kamp.core.api.constants.SanAndreasZone
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

@AccessCheck([AdminCommandAccessChecker::class])
class AdminCommands
@Inject
constructor(
        private val dialogService: DialogService,
        private val vehicleService: VehicleService,
        private val messageSender: MessageSender
) : Commands() {

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
            headerContent(
                    TextKeys.lvdm.command.vehicles.dialog.tab.model,
                    TextKeys.lvdm.command.vehicles.dialog.tab.location
            )
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

    @Command(aliases = ["i", "int"])
    fun interiors(player: Player) {
        dialogService.createListDialog<Interior> {
            caption(TextKeys.lvdm.command.interiors.dialog.caption)
            leftButton(TextKeys.lvdm.command.interiors.dialog.button.teleport)
            Interior.values().forEach { interior ->
                item {
                    value(interior)
                    content(interior.description)
                }
            }
            onSelectItem { _, listDialogItem, _ ->
                player.coordinates = listDialogItem.value.coordinates
                player.interiorId = listDialogItem.value.interiorId
                messageSender.sendMessageToPlayer(
                        player,
                        Colors.LIGHT_BLUE,
                        TextKeys.lvdm.command.interiors.message.teleport,
                        listDialogItem.value.description
                )
            }
        }.show(player)
    }
}