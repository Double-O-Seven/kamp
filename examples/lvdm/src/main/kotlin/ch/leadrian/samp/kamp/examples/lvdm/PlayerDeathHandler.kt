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

/**
 * Example of a callback listener. A callback listener may implement any callback listener interface.
 * It should be bound as eager singleton inside a [com.google.inject.AbstractModule].
 * @see [com.google.inject.AbstractModule.bind]
 * @see [com.google.inject.binder.ScopedBindingBuilder.asEagerSingleton]
 */
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
            handleMurder(player, killer, reason)
        } else {
            handleSuicide(player, reason)
        }
    }

    private fun handleMurder(player: Player, killer: Player, reason: WeaponModel) {
        if (reason == WeaponModel.FIST) {
            messageSender.sendMessageToAll(
                    Colors.RED,
                    LvdmTextKeys.lvdm.player.killed.fists,
                    coloredNameOf(killer),
                    coloredNameOf(player)
            )
        } else {
            messageSender.sendMessageToAll(
                    Colors.RED,
                    LvdmTextKeys.lvdm.player.killed.weapon,
                    coloredNameOf(killer),
                    coloredNameOf(player),
                    reason
            )
        }
        killer.giveMoney(player.money)
        player.resetMoney()
        killer.score = killer.score + 1
    }

    private fun handleSuicide(player: Player, reason: WeaponModel) {
        if (reason == WeaponModel.DROWN) {
            messageSender.sendMessageToAll(Colors.RED, LvdmTextKeys.lvdm.player.drown, coloredNameOf(player))
        } else {
            messageSender.sendMessageToAll(Colors.RED, LvdmTextKeys.lvdm.player.died, coloredNameOf(player), reason)
        }
    }
}