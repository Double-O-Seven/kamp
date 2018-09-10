package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TextLabelService {

    fun createTextLabel(
            text: String,
            color: ch.leadrian.samp.kamp.core.api.data.Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = -1,
            testLOS: Boolean = false
    ): TextLabel

    fun createTextLabel(
            textKey: TextKey,
            color: ch.leadrian.samp.kamp.core.api.data.Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = -1,
            testLOS: Boolean = false
    ): TextLabel

    fun exists(textLabelId: TextLabelId): Boolean

    fun getTextLabel(textLabelId: TextLabelId): TextLabel

    fun getAllTextLabels(): List<TextLabel>

}