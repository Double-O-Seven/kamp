package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.command.CommandListDialogFactory
import javax.inject.Inject

abstract class Commands {

    @Inject
    internal lateinit var commandListDialogFactory: CommandListDialogFactory

    @Inject
    protected lateinit var textProvider: TextProvider

    private val commandListDialog: Dialog by lazy {
        commandListDialogFactory.create(this, commandListDialogPageSize)
    }

    var groupName: String? = null
        internal set

    lateinit var definitions: List<CommandDefinition>
        internal set

    open val commandListDialogPageSize: Int = 30

    open fun getCommandListDialogTitle(player: Player): String =
            textProvider.getText(player.locale, TextKeys.command.dialog.title.generic)

    @JvmOverloads
    fun showCommandList(player: Player, showAsNavigationRoot: Boolean = true) {
        if (showAsNavigationRoot) {
            player.dialogNavigation.setRoot(commandListDialog)
        } else {
            player.dialogNavigation.push(commandListDialog)
        }
    }

}