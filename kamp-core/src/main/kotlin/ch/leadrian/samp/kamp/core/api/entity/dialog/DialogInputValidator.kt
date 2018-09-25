package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player

interface DialogInputValidator {

    fun validate(player: Player, inputText: String): Any?

}