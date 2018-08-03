package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.TextLabel
import ch.leadrian.samp.kamp.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.api.text.TextKey

interface TextLabelService {

    fun createTextLabel(
            text: String,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = -1,
            testLOS: Boolean = false
    ): TextLabel

    fun createTextLabel(
            textKey: TextKey,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = -1,
            testLOS: Boolean = false
    ): TextLabel

    fun exists(textLabelId: TextLabelId): Boolean

    fun getTextLabel(textLabelId: TextLabelId): TextLabel

    fun getAllTextLabels(): List<TextLabel>

}