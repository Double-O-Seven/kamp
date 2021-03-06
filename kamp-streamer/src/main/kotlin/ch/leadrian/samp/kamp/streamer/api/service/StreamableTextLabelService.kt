package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableTextLabel
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import java.util.Locale
import javax.inject.Inject

class StreamableTextLabelService
@Inject
internal constructor(
        private val textLabelStreamer: TextLabelStreamer,
        private val textProvider: TextProvider
) {

    @JvmOverloads
    fun createStreamableTextLabel(
            color: Color,
            coordinates: Vector3D,
            interiorId: Int,
            virtualWorldId: Int,
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true,
            textSupplier: (Locale) -> String
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = textSupplier,
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            testLOS = testLOS
    )

    @JvmOverloads
    fun createStreamableTextLabel(
            color: Color,
            coordinates: Vector3D,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true,
            textSupplier: (Locale) -> String
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = textSupplier,
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            testLOS = testLOS
    )

    @JvmOverloads
    fun createStreamableTextLabel(
            text: String,
            color: Color,
            coordinates: Vector3D,
            interiorId: Int,
            virtualWorldId: Int,
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = { text },
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            testLOS = testLOS
    )

    @JvmOverloads
    fun createStreamableTextLabel(
            text: String,
            color: Color,
            coordinates: Vector3D,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = { text },
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            testLOS = testLOS
    )

    @JvmOverloads
    fun createStreamableTextLabel(
            textKey: TextKey,
            color: Color,
            coordinates: Vector3D,
            interiorId: Int,
            virtualWorldId: Int,
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = { locale -> textProvider.getText(locale, textKey) },
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            testLOS = testLOS
    )

    @JvmOverloads
    fun createStreamableTextLabel(
            textKey: TextKey,
            color: Color,
            coordinates: Vector3D,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            streamDistance: Float = 50f,
            priority: Int = 0,
            testLOS: Boolean = true
    ): StreamableTextLabel = textLabelStreamer.createTextLabel(
            color = color,
            textSupplier = { locale -> textProvider.getText(locale, textKey) },
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            testLOS = testLOS
    )

    fun setMaxStreamedInTextLabels(maxStreamedInTextLabels: Int) {
        textLabelStreamer.capacity = maxStreamedInTextLabels
    }

}