package ch.leadrian.samp.kamp.api.entity.id

data class PlayerClassId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val playerClassIds: Array<PlayerClassId> = (0 until 320).map { PlayerClassId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerClassId =
                when {
                    0 <= value && value < playerClassIds.size -> playerClassIds[value]
                    else -> PlayerClassId(value)
                }
    }
}