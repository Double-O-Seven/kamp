package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.callback.onMoved
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugCommands
@Inject
constructor(
        private val messageSender: MessageSender,
        private val mapObjectService: MapObjectService
) : Commands() {

    private val objectsByName: MutableMap<String, MapObject> = mutableMapOf()

    @Unlisted
    @Command
    fun debugCmds(player: Player) {
        showCommandList(player)
    }

    @Command
    fun createObj(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("modelId ID") modelId: Int
    ) {
        val coordinates = player.coordinates
        val mapObject = mapObjectService.createMapObject(modelId, coordinates, vector3DOf(0f, 0f, 0f))
        val oldMapObject = objectsByName.put(name, mapObject)
        oldMapObject?.destroy()
        mapObject.onMoved {
            messageSender.sendMessageToAll(Colors.YELLOW, "$name stopped createMoving")
        }
    }

    @Command
    fun moveObj(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("x") x: Float,
            @Parameter("y") y: Float,
            @Parameter("z") z: Float,
            @Parameter("speed") speed: Float
    ) {
        val mapObject = objectsByName[name]
                ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.moveTo(vector3DOf(x, y, z), speed)
    }

    @Command
    fun stopObj(player: Player, name: String) {
        val mapObject = objectsByName[name]
                ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.stop()
    }

    @Command
    fun objPos(
            player: Player,
            @Parameter("name") name: String,
            @Parameter("x") x: Float,
            @Parameter("y") y: Float,
            @Parameter("z") z: Float
    ) {
        val mapObject = objectsByName[name]
                ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.coordinates = vector3DOf(x, y, z)
    }

    @Command
    fun attachObj(player: Player, @Parameter("name") name: String) {
        val mapObject = objectsByName[name]
                ?: return messageSender.sendMessageToPlayer(player, Colors.RED, "No such object")
        mapObject.attachTo(player, vector3DOf(0f, 0f, 0f), vector3DOf(0f, 0f, 0f))
    }
}