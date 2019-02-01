package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.base.HasModelId
import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TextDrawBase : Destroyable {

    val position: Vector2D

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

    var text: String

    var previewModelId: Int?

    fun setPreviewModelId(hasModelId: HasModelId)

    val previewModelRotation: Vector3D?

    val previewModelZoom: Float?

    var previewModelVehicleColors: VehicleColors?

    fun setText(text: String, vararg args: Any)

    fun setText(textKey: TextKey)

    fun setText(textKey: TextKey, vararg args: Any)

    fun setPreviewModelRotation(rotation: Vector3D, zoom: Float = 1.0f)
}