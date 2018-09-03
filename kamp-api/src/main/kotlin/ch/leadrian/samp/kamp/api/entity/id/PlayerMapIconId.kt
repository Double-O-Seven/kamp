package ch.leadrian.samp.kamp.api.entity.id

data class PlayerMapIconId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val playerMapIconIds: Array<PlayerMapIconId> = (0..99).map { PlayerMapIconId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerMapIconId =
                when {
                    0 <= value && value < playerMapIconIds.size -> playerMapIconIds[value]
                    else -> throw IllegalArgumentException("Map icon ID must be between 0 and ${playerMapIconIds.size - 1}, but was $value")
                }
    }
}