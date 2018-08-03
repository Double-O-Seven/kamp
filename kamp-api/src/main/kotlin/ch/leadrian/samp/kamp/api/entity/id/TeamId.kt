package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class TeamId internal constructor(val value: Int) {

    companion object {

        val NO_TEAM = TeamId(SAMPConstants.NO_TEAM)

        private val teamIds: Array<TeamId> = (0..256).map { TeamId(it) }.toTypedArray()

        fun valueOf(value: Int): TeamId =
                when {
                    0 <= value && value < teamIds.size -> teamIds[value]
                    value == NO_TEAM.value -> NO_TEAM
                    else -> TeamId(value)
                }
    }
}