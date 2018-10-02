package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
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
import java.util.*

class TextDraw
internal constructor(
        text: String,
        position: Vector2D,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter,
        var locale: Locale
) : Entity<TextDrawId>, AbstractDestroyable() {

    private val onClickHandlers: MutableList<TextDraw.(Player) -> OnPlayerClickTextDrawListener.Result> = mutableListOf()

    private val onDestroyHandlers: MutableList<TextDraw.() -> Unit> = mutableListOf()

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

    val position: Vector2D = position.toVector2D()

    var letterSize: Vector2D = vector2DOf(1f, 1f)
        set(value) {
            nativeFunctionExecutor.textDrawLetterSize(
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    var textSize: Vector2D = vector2DOf(0f, 0f)
        set(value) {
            nativeFunctionExecutor.textDrawTextSize(
                    text = id.value,
                    x = value.x,
                    y = value.y
            )
            field = value.toVector2D()
        }

    var alignment: TextDrawAlignment = TextDrawAlignment.LEFT
        set(value) {
            nativeFunctionExecutor.textDrawAlignment(
                    text = id.value,
                    alignment = value.value
            )
            field = value
        }

    var color: Color = Colors.WHITE
        set(value) {
            nativeFunctionExecutor.textDrawColor(
                    text = id.value,
                    color = value.value
            )
            field = value.toColor()
        }

    var useBox: Boolean = false
        set(value) {
            nativeFunctionExecutor.textDrawUseBox(
                    text = id.value,
                    use = value
            )
            field = value
        }

    var boxColor: Color = Colors.TRANSPARENT
        set(value) {
            nativeFunctionExecutor.textDrawBoxColor(
                    text = id.value,
                    color = value.value
            )
            field = value
        }

    var shadowSize: Int = 1
        set(value) {
            nativeFunctionExecutor.textDrawSetShadow(
                    text = id.value,
                    size = value
            )
            field = value
        }

    var outlineSize: Int = 0
        set(value) {
            nativeFunctionExecutor.textDrawSetOutline(text = id.value, size = value)
            field = value
        }

    var backgroundColor: Color = Colors.BLACK
        set(value) {
            nativeFunctionExecutor.textDrawBackgroundColor(text = id.value, color = value.value)
            field = value
        }

    var font: TextDrawFont = TextDrawFont.FONT2
        set(value) {
            nativeFunctionExecutor.textDrawFont(text = id.value, font = value.value)
            field = value
        }

    var isProportional: Boolean = false
        set(value) {
            nativeFunctionExecutor.textDrawSetProportional(text = id.value, set = value)
            field = value
        }

    var isSelectable: Boolean = false
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

    var text: String = text
        set(value) {
            nativeFunctionExecutor.textDrawSetString(text = id.value, string = value)
            field = value
        }

    fun setText(text: String, vararg args: Any) {
        this.text = textFormatter.format(locale, text, *args)
    }

    fun setText(textKey: TextKey) {
        text = textProvider.getText(locale, textKey)
    }

    fun setText(textKey: TextKey, vararg args: Any) {
        val unformattedText = textProvider.getText(locale, textKey)
        text = textFormatter.format(locale, unformattedText, *args)
    }

    var previewModel: Int? = null
        set(value) {
            nativeFunctionExecutor.textDrawSetPreviewModel(text = id.value, modelindex = value ?: -1)
            field = value
        }

    @JvmOverloads
    fun setPreviewModelRotation(rotation: Vector3D, zoom: Float = 1.0f) {
        nativeFunctionExecutor.textDrawSetPreviewRot(
                text = id.value,
                fRotX = rotation.x,
                fRotY = rotation.y,
                fRotZ = rotation.z,
                fZoom = zoom
        )
    }

    fun setPreviewModelVehicleColors(vehicleColors: VehicleColors) {
        nativeFunctionExecutor.textDrawSetPreviewVehCol(
                text = id.value,
                color1 = vehicleColors.color1.value,
                color2 = vehicleColors.color2.value
        )
    }

    fun onClick(onClick: TextDraw.(Player) -> OnPlayerClickTextDrawListener.Result) {
        onClickHandlers += onClick
    }

    internal fun onClick(player: Player): OnPlayerClickTextDrawListener.Result =
            onClickHandlers
                    .asSequence()
                    .map { it.invoke(this, player) }
                    .firstOrNull { it == OnPlayerClickTextDrawListener.Result.Processed }
                    ?: OnPlayerClickTextDrawListener.Result.NotFound

    internal fun onDestroy(onDestroy: TextDraw.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.textDrawDestroy(id.value)
        isDestroyed = true
    }
}