package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class MenuId internal constructor(val value: Int) {

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