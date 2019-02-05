package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class Area3DStreamer
@Inject
constructor(asyncExecutor: AsyncExecutor) : AreaStreamer<Rect3d>(asyncExecutor, SpatialIndex3D())
