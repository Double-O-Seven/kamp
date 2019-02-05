package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.CheckpointBase
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableRaceCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableRaceCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableRaceCheckpointStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableRaceCheckpointStreamOutReceiver

interface StreamableRaceCheckpoint :
        CheckpointBase,
        OnPlayerEnterStreamableRaceCheckpointReceiver,
        OnPlayerLeaveStreamableRaceCheckpointReceiver,
        OnStreamableRaceCheckpointStreamInReceiver,
        OnStreamableRaceCheckpointStreamOutReceiver {

    var type: RaceCheckpointType

    var nextCoordinates: Vector3D?

    var virtualWorldIds: MutableSet<Int>

    var interiorIds: MutableSet<Int>

    fun isStreamedIn(forPlayer: Player): Boolean

    fun isVisible(forPlayer: Player): Boolean

    fun visibleWhen(condition: StreamableRaceCheckpoint.(Player) -> Boolean)

}