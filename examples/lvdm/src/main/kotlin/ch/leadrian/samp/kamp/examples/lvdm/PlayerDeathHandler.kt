package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.text.MessageArguments.coloredNameOf
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerDeathHandler
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val messageSender: MessageSender,
        private val playerService: PlayerService
) : OnPlayerDeathListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerDeath(player: Player, killer: Player?, reason: WeaponModel) {
        playerService.sendDeathMessage(player, reason, killer)
        if (killer != null) {
            if (reason == WeaponModel.FIST) {
                messageSender
                        .sendMessageToAll(
                                Colors.RED,
                                TextKeys.lvdm.player.killed.fists,
                                coloredNameOf(killer),
                                coloredNameOf(player)
                        )
            } else {
                messageSender
                        .sendMessageToAll(
                                Colors.RED,
                                TextKeys.lvdm.player.killed.weapon,
                                coloredNameOf(killer),
                                coloredNameOf(player),
                                reason
                        )
            }
            killer.giveMoney(player.money)
            player.resetMoney()
            killer.score = killer.score + 1
        } else {
            if (reason == WeaponModel.DROWN) {
                messageSender.sendMessageToAll(Colors.RED, TextKeys.lvdm.player.drown, coloredNameOf(player))
            } else {
                messageSender.sendMessageToAll(Colors.RED, TextKeys.lvdm.player.died, coloredNameOf(player), reason)
            }
        }
    }
}