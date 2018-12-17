package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.TextLabel
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextLabelRegistry
import javax.inject.Inject

internal class TextLabelFactory
@Inject
constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textLabelRegistry: TextLabelRegistry,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) {

    fun create(
            coordinates: Vector3D,
            virtualWorldId: Int,
            text: String,
            color: Color,
            drawDistance: Float,
            testLOS: Boolean
    ): TextLabel {
        val textLabel = TextLabel(
                coordinates = coordinates,
                text = text,
                color = color,
                drawDistance = drawDistance,
                testLOS = testLOS,
                nativeFunctionExecutor = nativeFunctionExecutor,
                virtualWorldId = virtualWorldId
        )
        textLabelRegistry.register(textLabel)
        textLabel.onDestroy { textLabelRegistry.unregister(this) }
        return textLabel
    }

}