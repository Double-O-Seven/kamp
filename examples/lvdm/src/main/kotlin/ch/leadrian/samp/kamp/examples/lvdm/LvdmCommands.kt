package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageArguments.coloredNameOf
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LvdmCommands
@Inject
constructor(private val messageSender: MessageSender) : Commands() {

    @Unlisted
    @Command(aliases = ["commands"])
    fun cmds(player: Player) {
        showCommandList(player)
    }

    @Command
    fun giveCash(
            player: Player,
            @Parameter(nameTextKey = TextKeys.lvdm.command.givecash.parameter.receiver_) receiver: Player,
            @Parameter(nameTextKey = TextKeys.lvdm.command.givecash.parameter.amount_) amount: Int
    ) {
        if (amount < player.money || amount < 1) {
            messageSender.sendMessageToPlayer(player, Colors.YELLOW, TextKeys.lvdm.command.givecash.invalid.transaction.amount)
            return
        }

        messageSender.sendMessageToPlayer(player, Colors.GREEN, TextKeys.lvdm.command.givecash.message.sender, coloredNameOf(receiver), amount)
        player.giveMoney(-amount)
        messageSender.sendMessageToPlayer(receiver, Colors.GREEN, TextKeys.lvdm.command.givecash.message.receiver, amount, coloredNameOf(player))
        receiver.giveMoney(amount)
    }

    @Command
    fun kill(player: Player) {
        player.health = 0f
    }

}