package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class ActorId internal constructor(val value: Int) {

    companion object {

        val INVALID = ActorId(SAMPConstants.INVALID_ACTOR_ID)

        private val actorIds: Array<ActorId> = (0..SAMPConstants.MAX_PLAYERS).map { ActorId(it) }.toTypedArray()

        fun valueOf(value: Int): ActorId =
                when {
                    value == SAMPConstants.INVALID_ACTOR_ID -> INVALID
                    0 <= value && value < actorIds.size -> actorIds[value]
                    else -> ActorId(value)
                }
    }
}