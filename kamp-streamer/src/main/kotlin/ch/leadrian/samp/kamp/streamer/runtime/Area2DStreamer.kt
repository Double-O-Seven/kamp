package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex2D
import com.conversantmedia.util.collection.geometry.Rect2d
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class Area2DStreamer
@Inject
constructor(asyncExecutor: AsyncExecutor) : AreaStreamer<Rect2d>(asyncExecutor, SpatialIndex2D())
