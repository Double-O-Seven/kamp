package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import java.util.Locale
import javax.inject.Inject

internal class StreamableTextLabelFactory
@Inject
constructor(
        private val textProvider: TextProvider,
        private val streamableTextLabelStateFactory: StreamableTextLabelStateFactory,
        private val onStreamableTextLabelStreamInHandler: OnStreamableTextLabelStreamInHandler,
        private val onStreamableTextLabelStreamOutHandler: OnStreamableTextLabelStreamOutHandler
) {

    fun create(
            coordinates: Vector3D,
            color: Color,
            streamDistance: Float,
            priority: Int,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            testLOS: Boolean,
            textLabelStreamer: TextLabelStreamer,
            textSupplier: (Locale) -> String
    ): StreamableTextLabelImpl = StreamableTextLabelImpl(
            coordinates = coordinates,
            textSupplier = textSupplier,
            color = color,
            streamDistance = streamDistance,
            priority = priority,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            testLOS = testLOS,
            textProvider = textProvider,
            textLabelStreamer = textLabelStreamer,
            streamableTextLabelStateFactory = streamableTextLabelStateFactory,
            onStreamableTextLabelStreamInHandler = onStreamableTextLabelStreamInHandler,
            onStreamableTextLabelStreamOutHandler = onStreamableTextLabelStreamOutHandler
    )

}