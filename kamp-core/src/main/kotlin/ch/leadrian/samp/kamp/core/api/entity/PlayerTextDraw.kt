package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface PlayerTextDraw : HasPlayer, Destroyable, Entity<PlayerTextDrawId> {

    override val id: PlayerTextDrawId

    val position: Vector2D

    var letterSize: Vector2D

    var textSize: Vector2D

    var alignment: ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment

    var color: ch.leadrian.samp.kamp.core.api.data.Color

    var useBox: Boolean

    var boxColor: ch.leadrian.samp.kamp.core.api.data.Color

    var shadowSize: Int

    var outlineSize: Int

    var backgroundColor: ch.leadrian.samp.kamp.core.api.data.Color

    var font: ch.leadrian.samp.kamp.core.api.constants.TextDrawFont

    var isProportional: Boolean

    var isSelectable: Boolean

    fun show()

    fun hide()

    var text: String

    fun setText(text: String, vararg args: Any)

    fun setText(textKey: TextKey)

    fun setText(textKey: TextKey, vararg args: Any)

    var previewModel: Int?

    fun setPreviewModelRotation(rotation: Vector3D, zoom: Float)

    fun setPreviewModelVehicleColors(vehicleColors: VehicleColors)

    fun onClick(onClick: PlayerTextDraw.() -> Boolean)
}