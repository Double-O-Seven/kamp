package ch.leadrian.samp.kamp.api.entity.id

data class DialogId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val dialogIds: Array<DialogId> = (0 until 255).map { DialogId(it) }.toTypedArray()

        fun valueOf(value: Int): DialogId =
                when {
                    0 <= value && value < dialogIds.size -> dialogIds[value]
                    else -> DialogId(value)
                }
    }
}