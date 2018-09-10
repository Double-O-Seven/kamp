package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class MenuId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = MenuId(SAMPConstants.INVALID_MENU)

        private val menuIds: Array<MenuId> = (0 until SAMPConstants.MAX_MENUS).map { MenuId(it) }.toTypedArray()

        fun valueOf(value: Int): MenuId =
                when {
                    0 <= value && value < menuIds.size -> menuIds[value]
                    value == INVALID.value -> INVALID
                    else -> MenuId(value)
                }
    }
}