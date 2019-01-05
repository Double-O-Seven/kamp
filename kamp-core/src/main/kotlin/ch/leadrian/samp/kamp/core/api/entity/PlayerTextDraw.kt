package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawReceiver
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerClickPlayerTextDrawReceiverDelegate

class PlayerTextDraw
internal constructor(
        text: String,
        position: Vector2D,
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter,
        private val onPlayerClickPlayerTextDrawReceiver: OnPlayerClickPlayerTextDrawReceiverDelegate = OnPlayerClickPlayerTextDrawReceiverDelegate()
) : Entity<PlayerTextDrawId>,
        AbstractDestroyable(),
        HasPlayer,
        TextDrawBase,
        OnPlayerClickPlayerTextDrawReceiver by onPlayerClickPlayerTextDrawReceiver {

    override val id: PlayerTextDrawId
        get() = requireNotDestroyed { field }

    init {
        val playerTextDrawId = nativeFunctionExecutor.createPlayerTextDraw(
                playerid = player.id.value,
                x = position.x,
                y = position.y,
                text = text
        )

        if (playerTextDrawId == SAMPConstants.INVALID_TEXT_DRAW) {
            throw CreationFailedException("Could not create player text draw")
        }

        id = PlayerTextDrawId.valueOf(playerTextDrawId)
    }

    override val position: Vector2D = position.toVector2D()

    override var letterSize: Vector2D = vector2DOf(1f, 1f)
        set(value) {
            nativeFunctionExecutor.playerTextDrawLetterSize(
                    playerid = player.id.value,
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    override var textSize: Vector2D = vector2DOf(0f, 0f)
        set(value) {
            nativeFunctionExecutor.playerTextDrawTextSize(
                    playerid = player.id.value,
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    override var alignment: TextDrawAlignment = TextDrawAlignment.LEFT
        set(value) {
            nativeFunctionExecutor.playerTextDrawAlignment(
                    playerid = player.id.value,
                    text = id.value,
                    alignment = value.value
            )
            field = value
        }

    override var color: Color = Colors.WHITE
        set(value) {
            nativeFunctionExecutor.playerTextDrawColor(
                    playerid = player.id.value,
                    text = id.value,
                    color = value.value
            )
            field = value.toColor()
        }

    override var useBox: Boolean = false
        set(value) {
            nativeFunctionExecutor.playerTextDrawUseBox(
                    playerid = player.id.value,
                    text = id.value,
                    use = value
            )
            field = value
        }

    override var boxColor: Color = Colors.TRANSPARENT
        set(value) {
            nativeFunctionExecutor.playerTextDrawBoxColor(
                    playerid = player.id.value,
                    text = id.value,
                    color = value.value
            )
            field = value.toColor()
        }

    override var shadowSize: Int = 1
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetShadow(
                    playerid = player.id.value,
                    text = id.value,
                    size = value
            )
            field = value
        }

    override var outlineSize: Int = 0
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetOutline(
                    playerid = player.id.value,
                    text = id.value,
                    size = value
            )
            field = value
        }

    override var backgroundColor: Color = Colors.BLACK
        set(value) {
            nativeFunctionExecutor.playerTextDrawBackgroundColor(
                    playerid = player.id.value,
                    text = id.value,
                    color = value.value
            )
            field = value.toColor()
        }

    override var font: TextDrawFont = TextDrawFont.FONT2
        set(value) {
            nativeFunctionExecutor.playerTextDrawFont(
                    playerid = player.id.value,
                    text = id.value,
                    font = value.value
            )
            field = value
        }

    override var isProportional: Boolean = false
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetProportional(
                    playerid = player.id.value,
                    text = id.value,
                    set = value
            )
            field = value
        }

    override var isSelectable: Boolean = false
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetSelectable(
                    playerid = player.id.value,
                    text = id.value,
                    set = value
            )
            field = value
        }

    fun show() {
        nativeFunctionExecutor.playerTextDrawShow(playerid = player.id.value, text = id.value)
    }

    fun hide() {
        nativeFunctionExecutor.playerTextDrawHide(playerid = player.id.value, text = id.value)
    }

    override var text: String = text
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetString(
                    playerid = player.id.value,
                    text = id.value,
                    string = value
            )
            field = value
        }

    override fun setText(text: String, vararg args: Any) {
        this.text = textFormatter.format(player.locale, text, *args)
    }

    override fun setText(textKey: TextKey) {
        text = textProvider.getText(player.locale, textKey)
    }

    override fun setText(textKey: TextKey, vararg args: Any) {
        val unformattedText = textProvider.getText(player.locale, textKey)
        text = textFormatter.format(player.locale, unformattedText, *args)
    }

    override var previewModelId: Int? = null
        set(value) {
            nativeFunctionExecutor.playerTextDrawSetPreviewModel(
                    playerid = player.id.value,
                    text = id.value,
                    modelindex = value ?: -1
            )
            field = value
        }

    override var previewModelRotation: Vector3D? = null
        private set(value) {
            field = value?.toVector3D()
        }

    override var previewModelZoom: Float? = null
        private set

    override fun setPreviewModelRotation(rotation: Vector3D, zoom: Float) {
        nativeFunctionExecutor.playerTextDrawSetPreviewRot(
                playerid = player.id.value,
                text = id.value,
                fRotX = rotation.x,
                fRotY = rotation.y,
                fRotZ = rotation.z,
                fZoom = zoom
        )
        previewModelRotation = rotation
        previewModelZoom = zoom
    }

    override var previewModelVehicleColors: VehicleColors? = null
        set(value) {
            if (value == null) {
                return
            }
            nativeFunctionExecutor.playerTextDrawSetPreviewVehCol(
                    playerid = player.id.value,
                    text = id.value,
                    color1 = value.color1.value,
                    color2 = value.color2.value
            )
            field = value.toVehicleColors()
        }

    internal fun onClick(): OnPlayerClickPlayerTextDrawListener.Result =
            onPlayerClickPlayerTextDrawReceiver.onPlayerClickPlayerTextDraw(this)

    override var isDestroyed: Boolean = false
        get() = field || !player.isConnected

    override fun onDestroy() {
        nativeFunctionExecutor.playerTextDrawDestroy(playerid = player.id.value, text = id.value)
    }
}