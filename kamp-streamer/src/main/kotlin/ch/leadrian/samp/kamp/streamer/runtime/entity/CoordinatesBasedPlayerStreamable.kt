package ch.leadrian.samp.kamp.streamer.runtime.entity

import com.conversantmedia.util.collection.spatial.HyperRect

abstract class CoordinatesBasedPlayerStreamable<S : CoordinatesBasedPlayerStreamable<S, T>, T : HyperRect<*>> :
        SpatiallyIndexedStreamable<S, T>(), DistanceBasedPlayerStreamable
