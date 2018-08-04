package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class ActorId internal constructor(val value: Int) {

    companion object {

        val INVALID = ActorId(SAMPConstants.INVALID_ACTOR_ID)

        private val actorIds: Array<ActorId> = (0..SAMPConstants.MAX_ACTORS).map { ActorId(it) }.toTypedArray()

        fun valueOf(value: Int): ActorId =
                when {
                    0 <= value && value < actorIds.size -> actorIds[value]
                    value == INVALID.value -> INVALID
                    else -> ActorId(value)
                }
    }
}