package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.command.CommandListDialogFactory
import javax.inject.Inject

/**
 * Base class for all command implementations.
 * It is recommended to implement any concrete implementation with [javax.inject.Singleton] in order to
 * be able to inject a fully configured instance if necessary.
 *
 * @see [ch.leadrian.samp.kamp.core.api.inject.KampModule.newCommandsSetBinder]
 */
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

    val listedDefinitions: List<CommandDefinition> by lazy { definitions.filter { it.isListed } }

    /**
     * If some of the commands are cut off due to character limitations in a dialog,
     * you may override this property to decrease the limit.
     * The property may only be overridden and not set since the default command list dialog
     * is created once, lazily.
     */
    open val commandListDialogPageSize: Int = 30

    /**
     * Override this function to provide a custom dialog title for the command list dialog.
     * Use the [textProvider] and the [player]'s locale to provide a translated title.
     * The function may only be overridden and not set since the default command list dialog
     * is created once, lazily.
     */
    open fun getCommandListDialogTitle(player: Player): String =
            textProvider.getText(player.locale, KampCoreTextKeys.command.dialog.title.generic)

    @JvmOverloads
    fun showCommandList(player: Player, showAsNavigationRoot: Boolean = true) {
        if (showAsNavigationRoot) {
            player.dialogNavigation.setRoot(commandListDialog)
        } else {
            player.dialogNavigation.push(commandListDialog)
        }
    }

}