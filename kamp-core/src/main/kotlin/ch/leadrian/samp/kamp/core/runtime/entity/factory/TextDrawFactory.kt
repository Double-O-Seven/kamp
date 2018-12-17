package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import java.util.*
import javax.inject.Inject

internal class TextDrawFactory
@Inject
constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textDrawRegistry: TextDrawRegistry,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) {

    fun create(text: String, position: Vector2D, locale: Locale): TextDraw {
        val textDraw = TextDraw(
                text = text,
                position = position,
                nativeFunctionExecutor = nativeFunctionExecutor,
                textProvider = textProvider,
                textFormatter = textFormatter,
                locale = locale
        )
        textDrawRegistry.register(textDraw)
        textDraw.onDestroy { textDrawRegistry.unregister(this) }
        return textDraw
    }

}