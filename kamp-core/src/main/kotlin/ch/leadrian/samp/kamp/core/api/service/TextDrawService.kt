package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.TextDrawFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import java.util.Locale
import javax.inject.Inject

class TextDrawService
@Inject
internal constructor(
        private val textDrawFactory: TextDrawFactory,
        private val textDrawRegistry: TextDrawRegistry,
        private val textProvider: TextProvider
) {

    fun createTextDraw(text: String, position: Vector2D, locale: Locale): TextDraw =
            textDrawFactory.create(text, position, locale)

    fun createTextDraw(textKey: TextKey, position: Vector2D, locale: Locale): TextDraw {
        val text = textProvider.getText(locale, textKey)
        return textDrawFactory.create(text, position, locale)
    }

    fun isValidTextDraw(textDrawId: TextDrawId): Boolean =
            textDrawRegistry[textDrawId] != null

    fun getTextDraw(textDrawId: TextDrawId): TextDraw =
            textDrawRegistry[textDrawId] ?: throw NoSuchEntityException(
                    "No text draw with ID ${textDrawId.value}"
            )

    fun getAllTextDraws(): List<TextDraw> = textDrawRegistry.getAll()

}