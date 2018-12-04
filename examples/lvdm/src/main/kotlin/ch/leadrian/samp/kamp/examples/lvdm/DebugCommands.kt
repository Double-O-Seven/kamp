package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.streamer.api.service.StreamerService
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.navigation.viewNavigation
import ch.leadrian.samp.kamp.view.onClick
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugCommands
@Inject
constructor(
        private val messageSender: MessageSender,
        private val mapObjectService: MapObjectService,
        private val streamerService: StreamerService,
        private val viewFactory: ViewFactory
) : Commands() {

    private val objectsByName: MutableMap<String, MapObject> = mutableMapOf()

    @Unlisted
    @Command
    fun debugcmds(player: Player) {
        showCommandList(player)
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
            messageSender.sendMessageToAll(Colors.YELLOW, "$name stopped createMoving")
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

    @Command
    @Description("Creates cows with a fixed offset at height z")
    fun moo(player: Player, z: Float, offset: Int) {
        var numberOfObjects = 0
        for (x: Int in (-3000..3000) step offset) {
            for (y: Int in (-3000..3000) step offset) {
                streamerService.createStreamableMapObject(
                        modelId = 16442,
                        priority = 0,
                        streamDistance = 100f,
                        coordinates = vector3DOf(x.toFloat(), y.toFloat(), z),
                        rotation = vector3DOf(0f, 0f, 0f)
                )
                numberOfObjects++
            }
            messageSender.sendMessageToPlayer(player, Colors.GREEN, "Created $numberOfObjects objects")
        }
        messageSender.sendMessageToPlayer(player, Colors.GREEN, "Created $numberOfObjects objects")
    }

    @Command
    fun testview(player: Player, inputText: String, margin: Float, padding: Float) {
        with(viewFactory) {
            val view = view(player) {
                backgroundView {
                    setMargin(margin.pixels())
                    setPadding(padding.pixels())


                    color = colorOf(0xFF, 0, 0, 0x80)

                    textView {
                        text { inputText }
                        height = 25.percent()
                        letterHeight = 32.pixels()
                        enable()
                        onClick { messageSender.sendMessageToPlayer(player, Colors.PINK, "Clicked TextView: $inputText") }
                    }
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.PINK, "Clicked BackgroundView: $inputText") }
                }
            }
            player.viewNavigation.push(view)
        }
    }

}