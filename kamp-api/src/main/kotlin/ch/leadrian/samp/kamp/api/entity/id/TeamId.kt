package ch.leadrian.samp.kamp.api.entity.id

data class TeamId internal constructor(val value: Int) {

    companion object {

        private val teamIds: Array<TeamId> = (0..256).map { TeamId(it) }.toTypedArray()

        fun valueOf(value: Int): TeamId =
                when {
                    0 <= value && value < teamIds.size -> teamIds[value]
                    else -> TeamId(value)
                }
    }
}