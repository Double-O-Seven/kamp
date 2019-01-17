package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import javax.inject.Inject
import kotlin.reflect.KClass

class SpatialIndexBasedStreamableContainerFactory
@Inject
internal constructor() {

    fun <S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>> create(
            spatialIndex: SpatialIndex<S, T>,
            streamableClass: KClass<S>
    ): SpatialIndexBasedStreamableContainer<S, T> = SpatialIndexBasedStreamableContainer(spatialIndex, streamableClass)

    inline fun <reified S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>> create(spatialIndex: SpatialIndex<S, T>): SpatialIndexBasedStreamableContainer<S, T> =
            create(spatialIndex, S::class)

}