package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.TextLabelFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextLabelRegistry
import java.util.Locale
import javax.inject.Inject

class TextLabelService
@Inject
internal constructor(
        private val textLabelFactory: TextLabelFactory,
        private val textLabelRegistry: TextLabelRegistry,
        private val textProvider: TextProvider
) {

    @JvmOverloads
    fun createTextLabel(
            text: String,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = 0,
            testLOS: Boolean = false
    ): TextLabel = textLabelFactory.create(
            coordinates = coordinates,
            text = text,
            color = color,
            drawDistance = drawDistance,
            testLOS = testLOS,
            virtualWorldId = virtualWorldId
    )

    @JvmOverloads
    fun createTextLabel(
            textKey: TextKey,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            virtualWorldId: Int = 0,
            testLOS: Boolean = false,
            locale: Locale = Locale.getDefault()
    ): TextLabel {
        val text = textProvider.getText(locale, textKey)
        return textLabelFactory.create(
                coordinates = coordinates,
                text = text,
                color = color,
                drawDistance = drawDistance,
                testLOS = testLOS,
                virtualWorldId = virtualWorldId
        )
    }

    fun isValidTextLabel(textLabelId: TextLabelId): Boolean =
            textLabelRegistry[textLabelId] != null

    fun getTextLabel(textLabelId: TextLabelId): TextLabel =
            textLabelRegistry[textLabelId] ?: throw NoSuchEntityException(
                    "No text label with ID ${textLabelId.value}"
            )

    fun getAllTextLabels(): List<TextLabel> = textLabelRegistry.getAll()

}