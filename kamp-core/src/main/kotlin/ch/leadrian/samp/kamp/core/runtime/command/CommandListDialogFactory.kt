package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.service.DialogService
import java.util.ArrayList
import javax.inject.Inject

internal class CommandListDialogFactory
@Inject
constructor(private val dialogService: DialogService) {

    fun create(commands: Commands, maxCommandsPerPage: Int): Dialog {
        return if (commands.listedDefinitions.size <= maxCommandsPerPage) {
            createSinglePageDialog(commands)
        } else {
            createPagedDialog(commands, maxCommandsPerPage)
        }
    }

    private fun createSinglePageDialog(commands: Commands): Dialog {
        return dialogService.createTabListDialog<String> {
            buildSinglePageCaption(commands)
            buildHeaderContent()
            buildItems(commands.listedDefinitions.sortedBy { it.name })
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
            commands.listedDefinitions.asSequence().sortedBy { it.name }.chunked(maxCommandsPerPage).toList()

    private fun TabListDialogBuilder<String>.buildSinglePageCaption(commands: Commands) {
        caption(commands::getCommandListDialogTitle)
    }

    private fun TabListDialogBuilder<String>.buildPagedCaption(
            commands: Commands,
            index: Int,
            pages: List<List<CommandDefinition>>
    ) {
        caption { player ->
            "${commands.getCommandListDialogTitle(player)} (${index + 1}/${pages.size})"
        }
    }

    private fun TabListDialogBuilder<String>.buildHeaderContent() {
        headerContent(KampCoreTextKeys.command.dialog.tab.command, KampCoreTextKeys.command.dialog.tab.parameters)
    }

    private fun TabListDialogBuilder<String>.buildItems(definitions: List<CommandDefinition>) {
        definitions.forEach { definition -> buildItem(definition) }
    }

    private fun TabListDialogBuilder<String>.buildItem(definition: CommandDefinition) {
        item {
            value(definition.name)
            tabbedContent(
                    {
                        val groupName = definition.groupName
                        when {
                            groupName != null -> "$groupName ${definition.name}"
                            else -> definition.name
                        }
                    },
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

    private fun TabListDialogBuilder<String>.buildSinglePageButtons() {
        leftButton(KampCoreTextKeys.dialog.button.close)
    }

    private fun TabListDialogBuilder<String>.buildPagedButtons(
            index: Int,
            numberOfPages: Int,
            dialogs: MutableList<Dialog>
    ) {
        when (index) {
            0 -> {
                leftButton(KampCoreTextKeys.dialog.button.next)
                rightButton(KampCoreTextKeys.dialog.button.close)
            }
            numberOfPages - 1 -> {
                leftButton(KampCoreTextKeys.dialog.button.close)
                rightButton(KampCoreTextKeys.dialog.button.back)
            }
            else -> {
                leftButton(KampCoreTextKeys.dialog.button.next)
                rightButton(KampCoreTextKeys.dialog.button.back)
            }
        }
        onSelectItem { player, _, _ ->
            dialogs.getOrNull(index + 1)?.let { player.dialogNavigation.push(it) }
        }
    }

}