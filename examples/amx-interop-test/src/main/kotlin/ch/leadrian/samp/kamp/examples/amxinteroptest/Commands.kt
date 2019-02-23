package ch.leadrian.samp.kamp.examples.amxinteroptest

import ch.leadrian.samp.kamp.core.api.amx.AmxNativeFunction2
import ch.leadrian.samp.kamp.core.api.amx.AmxNativeFunction3
import ch.leadrian.samp.kamp.core.api.amx.AmxNativeFunction4
import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SanAndreasZone
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

class Commands
@Inject
constructor(private val messageSender: MessageSender) : Commands() {

    @Unlisted
    @Command
    fun cmds(player: Player) {
        showCommandList(player)
    }

    @Command
    fun setPos(player: Player, x: Float, y: Float, z: Float) {
        val coordinates = vector3DOf(x, y, z)
        player.setCoordinatesFindZ(coordinates)
        val zone = SanAndreasZone.getZone(coordinates) ?: "San Andreas"
        messageSender.sendMessageToPlayer(player, Colors.LIGHT_BLUE, "Welcome to {0}", zone)
    }

    private val SetPlayerPosFindZ by AmxNativeFunction4<Int, Float, Float, Float>()

    @Command
    fun setPos2(player: Player, x: Float, y: Float, z: Float) {
        val coordinates = vector3DOf(x, y, z)
        SetPlayerPosFindZ(player.id.value, x, y, z)
        val zone = SanAndreasZone.getZone(coordinates) ?: "San Andreas"
        messageSender.sendMessageToPlayer(player, Colors.PINK, "Welcome to {0}", zone)
    }

    private val GetPlayerName by AmxNativeFunction3<Int, OutputString, Int>()

    private val SendClientMessageToAll by AmxNativeFunction2<Int, String>()

    @Command
    fun say(player: Player, text: String) {
        val name = OutputString(SAMPConstants.MAX_PLAYER_NAME)
        GetPlayerName(player.id.value, name, SAMPConstants.MAX_PLAYER_NAME)
        SendClientMessageToAll(player.color.value, "* ${name.value} says: ${Colors.WHITE.toEmbeddedString()}$text")
    }

    private val GetPlayerPos by AmxNativeFunction4<Int, MutableFloatCell, MutableFloatCell, MutableFloatCell>()

    @Command
    fun jump(player: Player) {
        val x = MutableFloatCell()
        val y = MutableFloatCell()
        val z = MutableFloatCell()
        GetPlayerPos(player.id.value, x, y, z)
        player.coordinates = vector3DOf(x.value, y.value, z.value + 2f)
    }
}