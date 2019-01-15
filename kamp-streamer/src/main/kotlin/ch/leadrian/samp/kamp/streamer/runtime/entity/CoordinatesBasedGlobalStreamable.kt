package ch.leadrian.samp.kamp.streamer.runtime.entity

import com.conversantmedia.util.collection.spatial.HyperRect

abstract class CoordinatesBasedGlobalStreamable<S : CoordinatesBasedGlobalStreamable<S, T>, T : HyperRect<*>> :
        SpatiallyIndexedStreamable<S, T>(), DistanceBasedGlobalStreamable
