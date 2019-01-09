package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawReceiver
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerClickTextDrawReceiverDelegate
import java.util.Locale

class TextDraw
internal constructor(
        text: String,
        position: Vector2D,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter,
        var locale: Locale,
        private val onPlayerClickTextDrawReceiver: OnPlayerClickTextDrawReceiverDelegate = OnPlayerClickTextDrawReceiverDelegate()
) : Entity<TextDrawId>,
        AbstractDestroyable(),
        TextDrawBase,
        OnPlayerClickTextDrawReceiver by onPlayerClickTextDrawReceiver {

    override val id: TextDrawId
        get() = requireNotDestroyed { field }

    init {
        val textDrawId = nativeFunctionExecutor.textDrawCreate(
                x = position.x,
                y = position.y,
                text = text
        )

        if (textDrawId == SAMPConstants.INVALID_TEXT_DRAW) {
            throw CreationFailedException("Could not create player text draw")
        }

        id = TextDrawId.valueOf(textDrawId)
    }

    override val position: Vector2D = position.toVector2D()

    override var letterSize: Vector2D = vector2DOf(1f, 1f)
        set(value) {
            nativeFunctionExecutor.textDrawLetterSize(
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    override var textSize: Vector2D = vector2DOf(0f, 0f)
        set(value) {
            nativeFunctionExecutor.textDrawTextSize(
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    override var alignment: TextDrawAlignment = TextDrawAlignment.LEFT
        set(value) {
            nativeFunctionExecutor.textDrawAlignment(
                    text = id.value,
                    alignment = value.value
            )
            field = value
        }

    override var color: Color = Colors.WHITE
        set(value) {
            nativeFunctionExecutor.textDrawColor(
                    text = id.value,
                    color = value.value
            )
            field = value.toColor()
        }

    override var useBox: Boolean = false
        set(value) {
            nativeFunctionExecutor.textDrawUseBox(
                    text = id.value,
                    use = value
            )
            field = value
        }

    override var boxColor: Color = Colors.TRANSPARENT
        set(value) {
            nativeFunctionExecutor.textDrawBoxColor(
                    text = id.value,
                    color = value.value
            )
            field = value
        }

    override var shadowSize: Int = 1
        set(value) {
            nativeFunctionExecutor.textDrawSetShadow(
                    text = id.value,
                    size = value
            )
            field = value
        }

    override var outlineSize: Int = 0
        set(value) {
            nativeFunctionExecutor.textDrawSetOutline(text = id.value, size = value)
            field = value
        }

    override var backgroundColor: Color = Colors.BLACK
        set(value) {
            nativeFunctionExecutor.textDrawBackgroundColor(text = id.value, color = value.value)
            field = value
        }

    override var font: TextDrawFont = TextDrawFont.FONT2
        set(value) {
            nativeFunctionExecutor.textDrawFont(text = id.value, font = value.value)
            field = value
        }

    override var isProportional: Boolean = false
        set(value) {
            nativeFunctionExecutor.textDrawSetProportional(text = id.value, set = value)
            field = value
        }

    override var isSelectable: Boolean = false
        set(value) {
            nativeFunctionExecutor.textDrawSetSelectable(text = id.value, set = value)
            field = value
        }

    fun show(forPlayer: Player) {
        nativeFunctionExecutor.textDrawShowForPlayer(playerid = forPlayer.id.value, text = id.value)
    }

    fun showForAll() {
        nativeFunctionExecutor.textDrawShowForAll(id.value)
    }

    fun hide(forPlayer: Player) {
        nativeFunctionExecutor.textDrawHideForPlayer(playerid = forPlayer.id.value, text = id.value)
    }

    fun hideForAll() {
        nativeFunctionExecutor.textDrawHideForAll(id.value)
    }

    override var text: String = text
        set(value) {
            nativeFunctionExecutor.textDrawSetString(text = id.value, string = value)
            field = value
        }

    override fun setText(text: String, vararg args: Any) {
        this.text = textFormatter.format(locale, text, *args)
    }

    override fun setText(textKey: TextKey) {
        text = textProvider.getText(locale, textKey)
    }

    override fun setText(textKey: TextKey, vararg args: Any) {
        val unformattedText = textProvider.getText(locale, textKey)
        text = textFormatter.format(locale, unformattedText, *args)
    }

    override var previewModelId: Int? = null
        set(value) {
            nativeFunctionExecutor.textDrawSetPreviewModel(text = id.value, modelindex = value ?: -1)
            field = value
        }

    override var previewModelRotation: Vector3D? = null
        private set(value) {
            field = value?.toVector3D()
        }

    override var previewModelZoom: Float? = null
        private set

    override fun setPreviewModelRotation(rotation: Vector3D, zoom: Float) {
        nativeFunctionExecutor.textDrawSetPreviewRot(
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
            nativeFunctionExecutor.textDrawSetPreviewVehCol(
                    text = id.value,
                    color1 = value.color1.value,
                    color2 = value.color2.value
            )
            field = value.toVehicleColors()
        }

    internal fun onClick(player: Player): OnPlayerClickTextDrawListener.Result =
            onPlayerClickTextDrawReceiver.onPlayerClickTextDraw(player, this)

    override fun onDestroy() {
        nativeFunctionExecutor.textDrawDestroy(id.value)
    }
}