package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.SanAndreasZone
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.translateForText
import ch.leadrian.samp.kamp.streamer.api.service.StreamerService
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.composite.ListItemView
import ch.leadrian.samp.kamp.view.composite.ListViewAdapter
import ch.leadrian.samp.kamp.view.composite.ScrollBarAdapter
import ch.leadrian.samp.kamp.view.composite.ScrollBarView
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.navigation.viewNavigation
import ch.leadrian.samp.kamp.view.screenresolution.screenResolution
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugCommands
@Inject
constructor(
        private val messageSender: MessageSender,
        private val mapObjectService: MapObjectService,
        private val streamerService: StreamerService,
        private val viewFactory: ViewFactory,
        private val vehicleService: VehicleService
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
    fun screenRes(player: Player, w: Int, h: Int) {
        player.screenResolution.apply {
            width = w
            height = h
        }
    }

    @Command
    fun skinlistview(player: Player) {
        val adapter = object : ListViewAdapter<SkinModel> {

            override val numberOfItems: Int = SkinModel.values().size

            override val numberOfDisplayedItems: Int = 5

            override fun getItem(position: Int): SkinModel = SkinModel.values()[position]

            override fun createView(player: Player): ListItemView<SkinModel> = with(viewFactory) {
                SkinModelListItemView(player, viewContext, this)
            }

        }
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    horizontalListView(adapter)
                }
            }
            player.viewNavigation.push(view)
        }
    }

    private class SkinModelListItemView(
            player: Player,
            viewContext: ViewContext,
            viewFactory: ViewFactory
    ) : ListItemView<SkinModel>(player, viewContext) {

        private lateinit var skinModel: SkinModel

        init {
            setPadding(4.pixels())
            with(viewFactory) {
                this@SkinModelListItemView.backgroundView {
                    color = Colors.GREY.toMutableColor().apply { a = 0x80 }
                    val modelView = modelView {
                        bottom = 64.pixels()
                        modelId { skinModel.value }
                    }
                    textView {
                        topToBottomOf(modelView)
                        letterHeight = 16.pixels()
                        bottom = 0.pixels()
                        font = TextDrawFont.PRICEDOWN
                        alignment = TextDrawAlignment.CENTERED
                        text { skinModel.description }
                    }
                    enable()
                    onClick { player.skin = skinModel }
                }
            }
        }

        override fun setItem(position: Int, item: SkinModel) {
            skinModel = item
        }

    }

    @Command
    fun vehiclelistview(player: Player) {
        val adapter = object : ListViewAdapter<Vehicle> {

            override val numberOfItems: Int = vehicleService.getAllVehicles().size

            override val numberOfDisplayedItems: Int = 5

            override fun getItem(position: Int): Vehicle = vehicleService.getAllVehicles()[position]

            override fun createView(player: Player): ListItemView<Vehicle> = with(viewFactory) {
                VehicleListItemView(player, viewContext, this)
            }

        }
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    verticalListView(adapter)
                }
            }
            player.viewNavigation.push(view)
        }
    }

    private class VehicleListItemView(
            player: Player,
            viewContext: ViewContext,
            viewFactory: ViewFactory
    ) : ListItemView<Vehicle>(player, viewContext) {

        private lateinit var vehicle: Vehicle

        init {
            setPadding(4.pixels())
            with(viewFactory) {
                this@VehicleListItemView.backgroundView {
                    color = Colors.GREY.toMutableColor().apply { a = 0x80 }
                    val modelView = modelView {
                        left = 0.pixels()
                        width = pixels { parentArea.height }
                        modelId { vehicle.model.value }
                        vehicleColors { vehicle.colors }
                    }
                    val nameTextView = textView {
                        leftToRightOf(modelView)
                        top = 0.pixels()
                        height = 33.33f.percent()
                        letterHeight = 100.percent()
                        outlineSize = 1
                        font = TextDrawFont.BANK_GOTHIC
                        color { vehicle.colors.color1.color }
                        text { vehicle.model.modelName }
                    }
                    textView {
                        bottom = 0.pixels()
                        letterHeight = 50.percent()
                        leftToRightOf(modelView)
                        topToBottomOf(nameTextView)
                        setText(
                                "Vehicle ID: {0}${TextDrawCodes.NEW_LINE}Location: {1}",
                                translateForText { vehicle.id.value.toString() },
                                translateForText { SanAndreasZone.getZone(vehicle.coordinates)?.name ?: "San Andreas" }
                        )
                    }
                    enable()
                    onClick { player.putInVehicle(vehicle, 0) }
                }
            }
        }

        override fun setItem(position: Int, item: Vehicle) {
            this.vehicle = item
        }

    }

    @Command
    fun scrollbarview(player: Player, numberOfTicks: Int, windowSize: Int) {
        val adapter = object : ScrollBarAdapter {

            override val numberOfTicks: Int = numberOfTicks

            override val windowSize: Int = windowSize

            override fun onScroll(view: ScrollBarView, oldPosition: Int, newPosition: Int) {
                messageSender.sendMessageToPlayer(player, Colors.PINK, "Scroll to $newPosition/$numberOfTicks")
            }

        }
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    val verticalScrollBar = verticalScrollBarView(adapter) {
                        top = 0.pixels()
                        bottom = 0.pixels()
                        left = 0.pixels()
                        width = 12.pixels()
                    }
                    horizontalScrollBarView(adapter) {
                        leftToRightOf(verticalScrollBar)
                        bottom = 0.pixels()
                        right = 0.pixels()
                        height = 16.pixels()
                    }
                }
            }
            player.viewNavigation.push(view)
        }
    }

    @Command
    fun spriteview(player: Player) {
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    view {
                        left = 50.percent()
                        width = 50.percent()
                        spriteView {
                            setMargin(8.pixels())
                            spriteName = "loadsc12:loadsc12"
                        }
                    }
                    view {
                        right = 50.percent()
                        width = 50.percent()
                        spriteView {
                            setMargin(8.pixels())
                            spriteName = "loadsc13:loadsc13"
                        }
                    }
                }
            }
            player.viewNavigation.push(view)
        }
    }

    @Command
    fun modelview(player: Player) {
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    val bullet = modelView {
                        modelId = 541
                        vehicleColors = vehicleColorsOf(3, 6)
                        height = 50.percent()
                    }
                    modelView {
                        topToBottomOf(bullet)
                        modelId = 128
                        color = Colors.RED
                    }
                }
            }
            player.viewNavigation.push(view)
        }
    }

    @Command
    fun textview(player: Player) {
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                backgroundView {
                    val leftAligned = view {
                        width = 33.33f.percent()
                        height = 64.pixels()
                        left = 0.pixels()
                        top = 0.pixels()
                        backgroundView {
                            setMargin(8.pixels())
                            color = colorOf(0xDDDDDD80)
                            textView {
                                text { "Left aligned: x x x x x x x x x x" }
                                alignment = TextDrawAlignment.LEFT
                                enable()
                                onClick { messageSender.sendMessageToPlayer(player, Colors.GREY, "Left aligned clicked") }
                            }
                        }
                    }
                    val centerAligned = view {
                        width = 33.33f.percent()
                        height = 64.pixels()
                        leftToRightOf(leftAligned)
                        top = 0.pixels()
                        backgroundView {
                            setMargin(8.pixels())
                            color = colorOf(0xDDDDDD80)
                            textView {
                                text { "Center aligned: x x x x x x x x x x" }
                                alignment = TextDrawAlignment.CENTERED
                                enable()
                                onClick { messageSender.sendMessageToPlayer(player, Colors.GREY, "Center aligned clicked") }
                            }
                        }
                    }
                    view {
                        width = 33.33f.percent()
                        height = 64.pixels()
                        leftToRightOf(centerAligned)
                        top = 0.pixels()
                        backgroundView {
                            setMargin(8.pixels())
                            color = colorOf(0xDDDDDD80)
                            textView {
                                text { "Right aligned: x x x x x x x x x x" }
                                alignment = TextDrawAlignment.RIGHT
                                enable()
                                onClick { messageSender.sendMessageToPlayer(player, Colors.GREY, "Right aligned clicked") }
                            }
                        }
                    }
                    val font2 = textView {
                        topToBottomOf(leftAligned)
                        width = 25f.percent()
                        height = 64.pixels()
                        font = TextDrawFont.FONT2
                        text { "Font 2" }
                    }
                    val pricedown = textView {
                        topToBottomOf(leftAligned)
                        leftToRightOf(font2)
                        width = 25f.percent()
                        height = 64.pixels()
                        font = TextDrawFont.PRICEDOWN
                        text { "Pricedown" }
                    }
                    val diploma = textView {
                        topToBottomOf(leftAligned)
                        leftToRightOf(pricedown)
                        width = 25f.percent()
                        height = 64.pixels()
                        font = TextDrawFont.DIPLOMA
                        text { "Diploma" }
                    }
                    textView {
                        topToBottomOf(leftAligned)
                        leftToRightOf(diploma)
                        width = 25f.percent()
                        height = 64.pixels()
                        font = TextDrawFont.BANK_GOTHIC
                        text { "Bank Gothic" }
                    }
                }
            }
            player.viewNavigation.push(view)
        }
    }

    @Command
    fun backgroundview(player: Player) {
        with(viewFactory) {
            val view = view(player) {
                setPadding(100.pixels())
                val center = backgroundView {
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.GREY, "Center clicked") }
                }
                backgroundView {
                    color = Colors.RED.toMutableColor().apply { a = 0x80 }
                    topToTopOf(center)
                    bottomToBottomOf(center)
                    rightToLeftOf(center)
                    width = 64.pixels()
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.RED, "Left clicked") }
                }
                backgroundView {
                    color = Colors.GREEN.toMutableColor().apply { a = 0x80 }
                    topToTopOf(center)
                    bottomToBottomOf(center)
                    leftToRightOf(center)
                    width = 64.pixels()
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.GREEN, "Right clicked") }
                }
                backgroundView {
                    color = Colors.BLUE.toMutableColor().apply { a = 0x80 }
                    leftToLeftOf(center)
                    rightToRightOf(center)
                    topToBottomOf(center)
                    height = 64.pixels()
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.BLUE, "Bottom clicked") }
                }
                backgroundView {
                    color = Colors.YELLOW.toMutableColor().apply { a = 0x80 }
                    leftToLeftOf(center)
                    rightToRightOf(center)
                    bottomToTopOf(center)
                    height = 64.pixels()
                    enable()
                    onClick { messageSender.sendMessageToPlayer(player, Colors.YELLOW, "Top clicked") }
                }
            }
            player.viewNavigation.push(view)
        }
    }

}