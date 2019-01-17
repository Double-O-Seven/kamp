package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedGlobalStreamable
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import javax.inject.Inject
import kotlin.reflect.KClass

class CoordinatesBasedGlobalStreamerFactory
@Inject
internal constructor(
        private val distanceBasedGlobalStreamerFactory: DistanceBasedGlobalStreamerFactory,
        private val spatialIndexBasedStreamableContainerFactory: SpatialIndexBasedStreamableContainerFactory
) {

    fun <S : CoordinatesBasedGlobalStreamable<S, T>, T : HyperRect<*>> create(
            spatialIndex: SpatialIndex<S, T>,
            maxCapacity: Int,
            streamableClass: KClass<S>
    ): CoordinatesBasedGlobalStreamer<S, T> =
            CoordinatesBasedGlobalStreamer(
                    spatialIndex,
                    streamableClass,
                    maxCapacity,
                    distanceBasedGlobalStreamerFactory,
                    spatialIndexBasedStreamableContainerFactory
            )

    inline fun <reified S : CoordinatesBasedGlobalStreamable<S, T>, T : HyperRect<*>> create(
            spatialIndex: SpatialIndex<S, T>,
            maxCapacity: Int
    ): CoordinatesBasedGlobalStreamer<S, T> = create(spatialIndex, maxCapacity, S::class)

}