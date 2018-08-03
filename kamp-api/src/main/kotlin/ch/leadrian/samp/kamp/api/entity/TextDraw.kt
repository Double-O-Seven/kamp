package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector2D
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.VehicleColors
import ch.leadrian.samp.kamp.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.api.text.TextKey

interface TextDraw : Destroyable {

    val id: TextDrawId

    var letterSize: Vector2D

    var textSize: Vector2D

    var alignment: TextDrawAlignment

    var color: Color

    var useBox: Boolean

    var boxColor: Color

    var shadowSize: Int

    var outlineSize: Int

    var backgroundColor: Color

    var font: TextDrawFont

    var isProportional: Boolean

    var isSelectable: Boolean

    fun show(forPlayer: Player)

    fun hide(forPlayer: Player)

    fun showForAll()

    fun hideForAll()

    val text: String

    fun setText(text: String, vararg args: Any)

    fun setText(textKey: TextKey)

    fun setText(textKey: TextKey, vararg args: Any)

    val previewModel: Int?

    fun setPreviewModelRotation(rotation: Vector3D, zoom: Float)

    fun setPreviewModelVehicleColors(vehicleColors: VehicleColors)

    fun onClick(onClick: TextDraw.(Player) -> Boolean)
}