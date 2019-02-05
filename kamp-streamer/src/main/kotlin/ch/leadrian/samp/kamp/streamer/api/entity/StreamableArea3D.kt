package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.data.Shape3D

interface StreamableArea3D : StreamableArea, Shape3D {

    /**
     * Defines the order in which [ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableAreaListener]s and
     * [ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableAreaListener]s are called.
     * However, the priorities are only applied with respect to other [StreamableArea3D]s.
     * There is no defined order between [StreamableArea3D]s and [StreamableArea2D]s.
     *
     * @see [Streamable.priority]
     */
    override val priority: Int

}
