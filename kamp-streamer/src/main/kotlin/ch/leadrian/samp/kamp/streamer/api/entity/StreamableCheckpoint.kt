package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.entity.CheckpointBase
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableCheckpointStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableCheckpointStreamOutReceiver

interface StreamableCheckpoint :
        CheckpointBase,
        OnPlayerEnterStreamableCheckpointReceiver,
        OnPlayerLeaveStreamableCheckpointReceiver,
        OnStreamableCheckpointStreamInReceiver,
        OnStreamableCheckpointStreamOutReceiver {

    var virtualWorldIds: MutableSet<Int>

    var interiorIds: MutableSet<Int>

    fun isStreamedIn(forPlayer: Player): Boolean

    fun isVisible(forPlayer: Player): Boolean

    fun visibleWhen(condition: StreamableCheckpoint.(Player) -> Boolean)

}