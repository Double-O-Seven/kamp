package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import javax.inject.Inject
import kotlin.reflect.KClass

class CoordinatesBasedPlayerStreamerFactory
@Inject
internal constructor(
        private val distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory,
        private val spatialIndexBasedStreamableContainerFactory: SpatialIndexBasedStreamableContainerFactory
) {

    fun <S : CoordinatesBasedPlayerStreamable<S, T>, T : HyperRect<*>> create(
            spatialIndex: SpatialIndex<S, T>,
            maxCapacity: Int,
            streamableClass: KClass<S>
    ): CoordinatesBasedPlayerStreamer<S, T> =
            CoordinatesBasedPlayerStreamer(
                    spatialIndex,
                    streamableClass,
                    maxCapacity,
                    distanceBasedPlayerStreamerFactory,
                    spatialIndexBasedStreamableContainerFactory
            )

    inline fun <reified S : CoordinatesBasedPlayerStreamable<S, T>, T : HyperRect<*>> create(
            spatialIndex: SpatialIndex<S, T>,
            maxCapacity: Int
    ): CoordinatesBasedPlayerStreamer<S, T> = create(spatialIndex, maxCapacity, S::class)

}