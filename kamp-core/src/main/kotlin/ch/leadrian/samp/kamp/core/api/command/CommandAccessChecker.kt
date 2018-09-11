package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import java.lang.reflect.Method

interface CommandAccessChecker {

    fun isAccessGranted(player: Player, command: String, parameters: List<String>, method: Method): Boolean

}