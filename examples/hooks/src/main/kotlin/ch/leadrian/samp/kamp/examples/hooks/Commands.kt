package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Commands
@Inject
constructor(private val messageSender: MessageSender) : Commands() {

    @Unlisted
    @Command(name = "cmds", aliases = ["commands"])
    fun showCommands(player: Player) {
        showCommandList(player)
    }

    @Command
    fun me(player: Player, text: String) {
        messageSender.sendMessageToAll(player.color, "* {0} {1}", player.name, text)
    }

    @Command(aliases = ["ttm"])
    fun talkToMyself(player: Player, text: String) {
        messageSender.sendMessageToPlayer(player, player.color, "You said: $text")
    }

}