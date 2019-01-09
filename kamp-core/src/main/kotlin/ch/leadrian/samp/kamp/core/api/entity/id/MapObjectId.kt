package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class MapObjectId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = MapObjectId(SAMPConstants.INVALID_OBJECT_ID)

        private val mapObjectId: Array<MapObjectId> = (0 until SAMPConstants.MAX_OBJECTS).map { MapObjectId(it) }
                .toTypedArray()

        fun valueOf(value: Int): MapObjectId =
                when {
                    0 <= value && value < mapObjectId.size -> mapObjectId[value]
                    value == INVALID.value -> INVALID
                    else -> MapObjectId(value)
                }
    }
}