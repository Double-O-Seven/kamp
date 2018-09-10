package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class ActorId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = ActorId(SAMPConstants.INVALID_ACTOR_ID)

        private val actorIds: Array<ActorId> = (0 until SAMPConstants.MAX_ACTORS).map { ActorId(it) }.toTypedArray()

        fun valueOf(value: Int): ActorId =
                when {
                    0 <= value && value < actorIds.size -> actorIds[value]
                    value == INVALID.value -> INVALID
                    else -> ActorId(value)
                }
    }
}