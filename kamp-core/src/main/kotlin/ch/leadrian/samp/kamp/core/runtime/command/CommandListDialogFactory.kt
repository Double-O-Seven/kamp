package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.service.DialogService
import java.util.*
import javax.inject.Inject

internal class CommandListDialogFactory
@Inject
constructor(private val dialogService: DialogService) {

    fun create(commands: Commands, maxCommandsPerPage: Int): Dialog {
        return if (commands.definitions.size <= maxCommandsPerPage) {
            createSinglePageDialog(commands)
        } else {
            createPagedDialog(commands, maxCommandsPerPage)
        }
    }

    private fun createSinglePageDialog(commands: Commands): Dialog {
        return dialogService.createTabListDialog<String> {
            buildSinglePageCaption(commands)
            buildHeaderContent()
            buildItems(commands.definitions.sortedBy { it.name })
            buildSinglePageButtons()
        }
    }

    private fun createPagedDialog(commands: Commands, maxCommandsPerPage: Int): Dialog {
        val pages = getPages(commands, maxCommandsPerPage)
        val dialogs: MutableList<Dialog> = ArrayList(pages.size)
        pages.forEachIndexed { index, definitions ->
            val dialog = dialogService.createTabListDialog<String> {
                buildPagedCaption(commands, index, pages)
                buildHeaderContent()
                buildItems(definitions)
                buildPagedButtons(index, pages.size, dialogs)
            }
            dialogs += dialog
        }
        return dialogs.first()
    }

    private fun getPages(commands: Commands, maxCommandsPerPage: Int) =
            commands.definitions.asSequence().sortedBy { it.name }.chunked(maxCommandsPerPage).toList()

    private fun TabListDialogBuilder<String>.buildSinglePageCaption(commands: Commands) {
        caption(commands::getCommandListDialogTitle)
    }

    private fun TabListDialogBuilder<String>.buildPagedCaption(commands: Commands, index: Int, pages: List<List<CommandDefinition>>) {
        caption { player ->
            "${commands.getCommandListDialogTitle(player)} (${index + 1}/${pages.size})"
        }
    }

    private fun TabListDialogBuilder<String>.buildHeaderContent() {
        headerContent(TextKeys.command.dialog.tab.command, TextKeys.command.dialog.tab.parameters)
    }

    private fun TabListDialogBuilder<String>.buildItems(definitions: List<CommandDefinition>) {
        definitions.forEach { definition ->
            item {
                value(definition.name)
                tabbedContent(
                        { definition.name },
                        { player ->
                            StringBuilder().apply {
                                definition.parameters.forEach {
                                    append('[')
                                    append(it.getName(player.locale))
                                    append("] ")
                                }
                            }.toString()
                        }
                )
            }
        }
    }

    private fun TabListDialogBuilder<String>.buildSinglePageButtons() {
        leftButton(TextKeys.dialog.button.close)
    }

    private fun TabListDialogBuilder<String>.buildPagedButtons(index: Int, numberOfPages: Int, dialogs: MutableList<Dialog>) {
        when (index) {
            0 -> {
                leftButton(TextKeys.dialog.button.next)
                rightButton(TextKeys.dialog.button.close)
            }
            numberOfPages - 1 -> {
                leftButton(TextKeys.dialog.button.close)
                rightButton(TextKeys.dialog.button.back)
            }
            else -> {
                leftButton(TextKeys.dialog.button.next)
                rightButton(TextKeys.dialog.button.back)
            }
        }
        onSelectItem { player, _, _ ->
            dialogs.getOrNull(index + 1)?.let { player.dialogNavigation.push(it) }
        }
    }

}